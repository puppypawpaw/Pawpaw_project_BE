package kr.co.pawpaw.api.application.auth;

import kr.co.pawpaw.api.application.file.FileService;
import kr.co.pawpaw.api.dto.auth.DuplicateEmailResponse;
import kr.co.pawpaw.api.dto.auth.SignUpRequest;
import kr.co.pawpaw.api.dto.auth.SocialSignUpInfoResponse;
import kr.co.pawpaw.api.dto.auth.SocialSignUpRequest;
import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.common.exception.auth.DuplicateEmailException;
import kr.co.pawpaw.common.exception.auth.DuplicatePhoneNumberException;
import kr.co.pawpaw.common.exception.auth.InvalidOAuth2TempKeyException;
import kr.co.pawpaw.common.exception.auth.NotVerifiedPhoneNumberException;
import kr.co.pawpaw.common.exception.term.NotAgreeAllRequiredTermException;
import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import kr.co.pawpaw.domainrdb.pet.service.command.PetCommand;
import kr.co.pawpaw.domainrdb.sms.domain.SmsUsagePurpose;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.term.domain.Term;
import kr.co.pawpaw.domainrdb.term.domain.UserTermAgree;
import kr.co.pawpaw.domainrdb.term.service.command.TermCommand;
import kr.co.pawpaw.domainrdb.term.service.query.TermQuery;
import kr.co.pawpaw.domainrdb.user.domain.OAuth2Provider;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.domain.UserImage;
import kr.co.pawpaw.domainrdb.user.service.command.UserCommand;
import kr.co.pawpaw.domainrdb.user.service.command.UserImageCommand;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import kr.co.pawpaw.domainredis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.domainredis.auth.domain.VerifiedPhoneNumber;
import kr.co.pawpaw.domainredis.auth.service.command.OAuth2TempAttributesCommand;
import kr.co.pawpaw.domainredis.auth.service.query.OAuth2TempAttributesQuery;
import kr.co.pawpaw.domainredis.auth.service.query.VerifiedPhoneNumberQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {
    @Mock
    private UserQuery userQuery;
    @Mock
    private UserCommand userCommand;
    @Mock
    private TermQuery termQuery;
    @Mock
    private TermCommand termCommand;
    @Mock
    private PetCommand petCommand;
    @Mock
    private VerifiedPhoneNumberQuery verifiedPhoneNumberQuery;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private FileService fileService;
    @Mock
    private UserImageCommand userImageCommand;
    @Mock
    private OAuth2TempAttributesQuery oAuth2TempAttributesQuery;
    @Mock
    private OAuth2TempAttributesCommand oAuth2TempAttributesCommand;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    @DisplayName("일반 회원가입 메서드 유저 중복 테스트")
    void 일반_회원가입_메서드_유저_중복_테스트() {
        //given
        String alreadyExistEmail = "aee";
        SignUpRequest request = SignUpRequest.builder()
            .email(alreadyExistEmail)
            .phoneNumber("01012345678")
            .build();

        when(userQuery.existsByEmailAndProvider(any(String.class), any())).thenReturn(true);
        //when
        assertThatThrownBy(() -> signUpService.signUp(request, multipartFile)).isInstanceOf(DuplicateEmailException.class);

        //then
        verify(userQuery, times(1)).existsByEmailAndProvider(request.getEmail(), null);
    }

    @Test
    @DisplayName("일반 회원가입 메서드 필수 약관 동의 테스트")
    void 일반_회원가입_메서드_필수_약관_동의_테스트() {
        //given
        List<Long> termAgrees = List.of(1L, 2L, 4L);
        Set<Long> termAgreesSet = new HashSet<>(termAgrees);
        SignUpRequest request = SignUpRequest.builder()
            .email("email")
            .termAgrees(termAgrees)
            .phoneNumber("01012345678")
            .build();

        when(userQuery.existsByEmailAndProvider(any(String.class), any())).thenReturn(false);
        when(termQuery.isAllRequiredTermIds(eq(termAgreesSet))).thenReturn(false);
        //when
        assertThatThrownBy(() -> signUpService.signUp(request, multipartFile)).isInstanceOf(NotAgreeAllRequiredTermException.class);

        //then
        verify(userQuery, times(1)).existsByEmailAndProvider(request.getEmail(), null);
        verify(termQuery, times(1)).isAllRequiredTermIds(termAgreesSet);
    }

    @Test
    @DisplayName("일반 회원가입 메서드 핸드폰 번호 유효성 테스트")
    void 일반_회원가입_메서드_핸드폰_번호_유효성_테스트() {
        //given
        List<Long> termAgrees = List.of(1L, 2L, 3L);
        Set<Long> termAgreesSet = new HashSet<>(termAgrees);
        String password = "password";
        String email = "email";
        SignUpRequest request1 = SignUpRequest.builder()
            .email(email)
            .termAgrees(termAgrees)
            .password(password)
            .phoneNumber("01012345678")
            .build();
        SignUpRequest request2 = SignUpRequest.builder()
            .email(email)
            .termAgrees(termAgrees)
            .password(password)
            .phoneNumber("01087654321")
            .build();


        when(userQuery.existsByEmailAndProvider(any(String.class), any())).thenReturn(false);
        when(termQuery.isAllRequiredTermIds(eq(termAgreesSet))).thenReturn(true);
        when(userQuery.existsByPhoneNumber(eq(request1.getPhoneNumber()))).thenReturn(true);
        when(userQuery.existsByPhoneNumber(eq(request2.getPhoneNumber()))).thenReturn(false);
        when(verifiedPhoneNumberQuery.existsByPhoneNumberAndUsagePurpose(eq(request2.getPhoneNumber()), eq(SmsUsagePurpose.SIGN_UP.name()))).thenReturn(false);
        //when
        assertThatThrownBy(() -> signUpService.signUp(request1, multipartFile)).isInstanceOf(DuplicatePhoneNumberException.class);
        assertThatThrownBy(() -> signUpService.signUp(request2, multipartFile)).isInstanceOf(NotVerifiedPhoneNumberException.class);

        //then
        verify(userQuery, times(2)).existsByEmailAndProvider(email, null);
        verify(termQuery, times(2)).isAllRequiredTermIds(termAgreesSet);
        verify(userQuery, times(1)).existsByPhoneNumber(request1.getPhoneNumber());
        verify(userQuery, times(1)).existsByPhoneNumber(request2.getPhoneNumber());
        verify(verifiedPhoneNumberQuery, times(1)).existsByPhoneNumberAndUsagePurpose(request2.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name());
    }

    @Test
    @DisplayName("일반 회원가입 메서드 이미지 없이 작동 테스트")
    void 일반_회원가입_메서드_이미지_없이_작동_테스트() {
        //given
        List<Long> termAgreeOrders = List.of(1L, 2L, 3L);
        List<Term> termAgrees = List.of(
            Term.builder()
                .title("term1")
                .content("term1-content")
                .order(1L)
                .required(true)
                .build(),
            Term.builder()
                .title("term2")
                .content("term2-content")
                .order(2L)
                .required(true)
                .build(),
            Term.builder()
                .title("term3")
                .content("term3-content")
                .order(3L)
                .required(true)
                .build()
        );
        Set<Long> termAgreesOrderSet = new HashSet<>(termAgreeOrders);
        String password = "password";
        String email = "email";
        String nickname = "nickname";
        PositionRequest position = PositionRequest.builder()
            .latitude(36.8)
            .longitude(36.8)
            .name("36.8")
            .build();
        SignUpRequest request = SignUpRequest.builder()
            .email(email)
            .termAgrees(termAgreeOrders)
            .password(password)
            .nickname(nickname)
            .position(position)
            .phoneNumber("01087654321")
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("루이")
                    .petType(PetType.DOG)
                    .build()
            ))
            .build();

        String passwordEncoded = "passwordEncoded";
        String name = "userName";

        User savedUser = request.toUser(passwordEncoded, name);

        File file = File.builder()
            .contentType("image/png")
            .byteSize(1234L)
            .uploader(savedUser)
            .build();

        VerifiedPhoneNumber vPhoneNo = VerifiedPhoneNumber.builder()
            .phoneNumber(request.getPhoneNumber())
            .usagePurpose(SmsUsagePurpose.SIGN_UP.name())
            .userName(name)
            .build();

        when(userQuery.existsByEmailAndProvider(any(String.class), any())).thenReturn(false);
        when(termQuery.isAllRequiredTermIds(eq(termAgreesOrderSet))).thenReturn(true);
        when(userQuery.existsByPhoneNumber(eq(request.getPhoneNumber()))).thenReturn(false);
        when(verifiedPhoneNumberQuery.existsByPhoneNumberAndUsagePurpose(eq(request.getPhoneNumber()), eq(SmsUsagePurpose.SIGN_UP.name()))).thenReturn(true);
        when(verifiedPhoneNumberQuery.findByPhoneNumberAndUsagePurpose(eq(request.getPhoneNumber()), eq(SmsUsagePurpose.SIGN_UP.name()))).thenReturn(Optional.of(vPhoneNo));
        when(passwordEncoder.encode(request.getPassword())).thenReturn(passwordEncoded);
        when(userCommand.save(any(User.class))).thenReturn(savedUser);
        when(termQuery.findAllByOrderIsIn(eq(termAgreeOrders))).thenReturn(termAgrees);

        UserImage userImage = UserImage.builder()
            .user(savedUser)
            .file(file)
            .build();
        //when
        signUpService.signUp(request, null);

        //then
        verify(userQuery, times(1)).existsByEmailAndProvider(email, null);
        verify(termQuery, times(1)).isAllRequiredTermIds(termAgreesOrderSet);
        verify(userQuery, times(1)).existsByPhoneNumber(request.getPhoneNumber());
        verify(verifiedPhoneNumberQuery, times(1)).existsByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name());
        verify(verifiedPhoneNumberQuery, times(1)).findByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name());

        verify(passwordEncoder, times(1)).encode(request.getPassword());
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userCommand, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser).usingRecursiveComparison().ignoringFieldsMatchingRegexes("userId").isEqualTo(savedUser);

        verify(fileService, times(0)).saveFileByMultipartFile(any(), any());
        verify(userImageCommand, times(0)).save(any());

        ArgumentCaptor<List<Pet>> petListCaptor = ArgumentCaptor.forClass(List.class);
        verify(petCommand, times(1)).saveAll(petListCaptor.capture());
        List<Pet> capturedPetList = petListCaptor.getValue();
        assertThat(capturedPetList.size()).isEqualTo(1);
        assertThat(capturedPetList.get(0).getName()).isEqualTo(request.getPetInfos().get(0).getPetName());
        assertThat(capturedPetList.get(0).getPetType()).isEqualTo(request.getPetInfos().get(0).getPetType());
        assertThat(capturedPetList.get(0).getParent()).isEqualTo(savedUser);

        ArgumentCaptor<Collection<UserTermAgree>> userTermAgreeListCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(termCommand, times(1)).saveAllUserTermAgrees(userTermAgreeListCaptor.capture());
        Collection<UserTermAgree> capturedUserTermAgreeList = userTermAgreeListCaptor.getValue();
        assertThat(capturedUserTermAgreeList.size()).isEqualTo(3);
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(0))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(1))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(2))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).contains(savedUser)).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("일반 회원가입 메서드 작동 테스트")
    void 일반_회원가입_메서드_작동_테스트() {
        //given
        List<Long> termAgreeOrders = List.of(1L, 2L, 3L);
        List<Term> termAgrees = List.of(
            Term.builder()
                .title("term1")
                .content("term1-content")
                .order(1L)
                .required(true)
                .build(),
            Term.builder()
                .title("term2")
                .content("term2-content")
                .order(2L)
                .required(true)
                .build(),
            Term.builder()
                .title("term3")
                .content("term3-content")
                .order(3L)
                .required(true)
                .build()
        );
        Set<Long> termAgreesOrderSet = new HashSet<>(termAgreeOrders);
        String password = "password";
        String email = "email";
        String nickname = "nickname";
        PositionRequest position = PositionRequest.builder()
            .latitude(36.8)
            .longitude(36.8)
            .name("36.8")
            .build();
        SignUpRequest request = SignUpRequest.builder()
            .email(email)
            .termAgrees(termAgreeOrders)
            .password(password)
            .nickname(nickname)
            .position(position)
            .phoneNumber("01087654321")
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("루이")
                    .petType(PetType.DOG)
                    .build()
            ))
            .build();

        String passwordEncoded = "passwordEncoded";
        String name = "userName";

        User savedUser = request.toUser(passwordEncoded, name);

        File file = File.builder()
            .contentType("image/png")
            .byteSize(1234L)
            .uploader(savedUser)
            .build();

        VerifiedPhoneNumber vPhoneNo = VerifiedPhoneNumber.builder()
            .phoneNumber(request.getPhoneNumber())
            .usagePurpose(SmsUsagePurpose.SIGN_UP.name())
            .userName(name)
            .build();

        when(userQuery.existsByEmailAndProvider(any(String.class), any())).thenReturn(false);
        when(termQuery.isAllRequiredTermIds(eq(termAgreesOrderSet))).thenReturn(true);
        when(userQuery.existsByPhoneNumber(eq(request.getPhoneNumber()))).thenReturn(false);
        when(verifiedPhoneNumberQuery.existsByPhoneNumberAndUsagePurpose(eq(request.getPhoneNumber()), eq(SmsUsagePurpose.SIGN_UP.name()))).thenReturn(true);
        when(verifiedPhoneNumberQuery.findByPhoneNumberAndUsagePurpose(eq(request.getPhoneNumber()), eq(SmsUsagePurpose.SIGN_UP.name()))).thenReturn(Optional.of(vPhoneNo));
        when(passwordEncoder.encode(request.getPassword())).thenReturn(passwordEncoded);
        when(userCommand.save(any(User.class))).thenReturn(savedUser);
        when(termQuery.findAllByOrderIsIn(eq(termAgreeOrders))).thenReturn(termAgrees);
        when(fileService.saveFileByMultipartFile(eq(multipartFile), eq(savedUser.getUserId()))).thenReturn(file);

        UserImage userImage = UserImage.builder()
            .user(savedUser)
            .file(file)
            .build();
        //when
        signUpService.signUp(request, multipartFile);

        //then
        verify(userQuery, times(1)).existsByEmailAndProvider(email, null);
        verify(termQuery, times(1)).isAllRequiredTermIds(termAgreesOrderSet);
        verify(userQuery, times(1)).existsByPhoneNumber(request.getPhoneNumber());
        verify(verifiedPhoneNumberQuery, times(1)).existsByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name());
        verify(verifiedPhoneNumberQuery, times(1)).findByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name());

        verify(passwordEncoder, times(1)).encode(request.getPassword());
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userCommand, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser).usingRecursiveComparison().ignoringFieldsMatchingRegexes("userId").isEqualTo(savedUser);

        verify(fileService).saveFileByMultipartFile(multipartFile, savedUser.getUserId());
        ArgumentCaptor<UserImage> userImageCaptor = ArgumentCaptor.forClass(UserImage.class);
        verify(userImageCommand).save(userImageCaptor.capture());
        assertThat(userImageCaptor.getValue()).usingRecursiveComparison().isEqualTo(userImage);

        ArgumentCaptor<List<Pet>> petListCaptor = ArgumentCaptor.forClass(List.class);
        verify(petCommand, times(1)).saveAll(petListCaptor.capture());
        List<Pet> capturedPetList = petListCaptor.getValue();
        assertThat(capturedPetList.size()).isEqualTo(1);
        assertThat(capturedPetList.get(0).getName()).isEqualTo(request.getPetInfos().get(0).getPetName());
        assertThat(capturedPetList.get(0).getPetType()).isEqualTo(request.getPetInfos().get(0).getPetType());
        assertThat(capturedPetList.get(0).getParent()).isEqualTo(savedUser);

        ArgumentCaptor<Collection<UserTermAgree>> userTermAgreeListCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(termCommand, times(1)).saveAllUserTermAgrees(userTermAgreeListCaptor.capture());
        Collection<UserTermAgree> capturedUserTermAgreeList = userTermAgreeListCaptor.getValue();
        assertThat(capturedUserTermAgreeList.size()).isEqualTo(3);
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(0))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(1))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(2))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).contains(savedUser)).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("소셜 회원가입 메서드 필수 약관 동의 테스트")
    void 소셜_회원가입_메서드_필수_약관_동의_테스트() {
        //given
        List<Long> termAgrees = List.of(1L, 2L, 4L);
        Set<Long> termAgreesSet = new HashSet<>(termAgrees);
        SocialSignUpRequest request = SocialSignUpRequest.builder()
            .termAgrees(termAgrees)
            .build();

        when(termQuery.isAllRequiredTermIds(eq(termAgreesSet))).thenReturn(false);
        //when
        assertThatThrownBy(() -> signUpService.socialSignUp(request, multipartFile)).isInstanceOf(NotAgreeAllRequiredTermException.class);

        //then
        verify(termQuery, times(1)).isAllRequiredTermIds(termAgreesSet);
    }

    @Test
    @DisplayName("소셜 회원가입 메서드 키 유효성 테스트")
    void 소셜_회원가입_메서드_키_유효성_테스트() {
        //given
        List<Long> termAgrees = List.of(1L, 2L, 3L);
        Set<Long> termAgreesSet = new HashSet<>(termAgrees);
        SocialSignUpRequest request = SocialSignUpRequest.builder()
            .termAgrees(termAgrees)
            .key("invalid key")
            .build();

        when(termQuery.isAllRequiredTermIds(eq(termAgreesSet))).thenReturn(true);
        when(oAuth2TempAttributesQuery.findById(request.getKey())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> signUpService.socialSignUp(request, multipartFile)).isInstanceOf(InvalidOAuth2TempKeyException.class);

        //then
        verify(termQuery, times(1)).isAllRequiredTermIds(termAgreesSet);
        verify(oAuth2TempAttributesQuery, times(1)).findById(request.getKey());
    }

    @Test
    @DisplayName("소셜 회원가입 메서드 이미지 저장 안함 테스트")
    void 소셜_회원가입_메서드_이미지_저장_안함_테스트() {
        //given
        List<Long> termAgreeOrders = List.of(1L, 2L, 3L);
        List<Term> termAgrees = List.of(
            Term.builder()
                .title("term1")
                .content("term1-content")
                .order(1L)
                .required(true)
                .build(),
            Term.builder()
                .title("term2")
                .content("term2-content")
                .order(2L)
                .required(true)
                .build(),
            Term.builder()
                .title("term3")
                .content("term3-content")
                .order(3L)
                .required(true)
                .build()
        );
        Set<Long> termAgreesSet = new HashSet<>(termAgreeOrders);

        PositionRequest position = PositionRequest.builder()
            .latitude(36.8)
            .longitude(36.8)
            .name("36.8")
            .build();

        SocialSignUpRequest request = SocialSignUpRequest.builder()
            .termAgrees(termAgreeOrders)
            .key("valid key")
            .noImage(true)
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("루이")
                    .petType(PetType.DOG)
                    .build()
            ))
            .position(position)
            .build();

        OAuth2TempAttributes oAuth2TempAttributes = OAuth2TempAttributes.builder()
            .name("이름")
            .provider("GOOGLE")
            .profileImageUrl("http://example.com")
            .email("email@example.com")
            .build();

        User user = request.toUser(oAuth2TempAttributes.getEmail(), OAuth2Provider.valueOf(oAuth2TempAttributes.getProvider()));

        when(termQuery.isAllRequiredTermIds(eq(termAgreesSet))).thenReturn(true);
        when(oAuth2TempAttributesQuery.findById(request.getKey())).thenReturn(Optional.of(oAuth2TempAttributes));
        when(userCommand.save(any(User.class))).thenReturn(user);
        when(termQuery.findAllByOrderIsIn(eq(termAgreeOrders))).thenReturn(termAgrees);
        //when
        UserId userId = signUpService.socialSignUp(request, multipartFile);

        //then
        verify(termQuery).isAllRequiredTermIds(termAgreesSet);
        verify(oAuth2TempAttributesQuery).findById(request.getKey());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userCommand).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).usingRecursiveComparison().ignoringFieldsMatchingRegexes("userId").isEqualTo(user);

        verify(fileService, times(0)).saveFileByMultipartFile(any(), any());
        verify(fileService, times(0)).saveFileByUrl(any(), any());

        ArgumentCaptor<List<Pet>> petListCaptor = ArgumentCaptor.forClass(List.class);
        verify(petCommand).saveAll(petListCaptor.capture());
        assertThat(petListCaptor.getValue().size()).isEqualTo(1);
        assertThat(petListCaptor.getValue().get(0).getName()).isEqualTo(request.getPetInfos().get(0).getPetName());
        assertThat(petListCaptor.getValue().get(0).getPetType()).isEqualTo(request.getPetInfos().get(0).getPetType());
        assertThat(petListCaptor.getValue().get(0).getParent()).usingRecursiveComparison().ignoringFieldsMatchingRegexes("userId").isEqualTo(user);

        ArgumentCaptor<Collection<UserTermAgree>> userTermAgreeListCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(termCommand, times(1)).saveAllUserTermAgrees(userTermAgreeListCaptor.capture());
        Collection<UserTermAgree> capturedUserTermAgreeList = userTermAgreeListCaptor.getValue();
        assertThat(capturedUserTermAgreeList.size()).isEqualTo(3);
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(0))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(1))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(2))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).contains(user)).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).size()).isEqualTo(1);

        verify(oAuth2TempAttributesCommand).deleteById(oAuth2TempAttributes.getKey());
    }

    @Test
    @DisplayName("소셜 회원가입 메서드 MultipartFile 이미지 저장 테스트")
    void 소셜_회원가입_메서드_MultipartFile_이미지_저장_테스트() throws IOException {
        //given
        List<Long> termAgreeOrders = List.of(1L, 2L, 3L);
        List<Term> termAgrees = List.of(
            Term.builder()
                .title("term1")
                .content("term1-content")
                .order(1L)
                .required(true)
                .build(),
            Term.builder()
                .title("term2")
                .content("term2-content")
                .order(2L)
                .required(true)
                .build(),
            Term.builder()
                .title("term3")
                .content("term3-content")
                .order(3L)
                .required(true)
                .build()
        );
        Set<Long> termAgreesSet = new HashSet<>(termAgreeOrders);

        PositionRequest position = PositionRequest.builder()
            .latitude(36.8)
            .longitude(36.8)
            .name("36.8")
            .build();

        SocialSignUpRequest request = SocialSignUpRequest.builder()
            .termAgrees(termAgreeOrders)
            .key("valid key")
            .noImage(false)
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("루이")
                    .petType(PetType.DOG)
                    .build()
            ))
            .position(position)
            .build();

        OAuth2TempAttributes oAuth2TempAttributes = OAuth2TempAttributes.builder()
            .name("이름")
            .provider("GOOGLE")
            .profileImageUrl("http://example.com")
            .email("email@example.com")
            .build();

        User user = request.toUser(oAuth2TempAttributes.getEmail(), OAuth2Provider.valueOf(oAuth2TempAttributes.getProvider()));

        when(termQuery.isAllRequiredTermIds(eq(termAgreesSet))).thenReturn(true);
        when(oAuth2TempAttributesQuery.findById(request.getKey())).thenReturn(Optional.of(oAuth2TempAttributes));
        when(userCommand.save(any(User.class))).thenReturn(user);
        when(termQuery.findAllByOrderIsIn(eq(termAgreeOrders))).thenReturn(termAgrees);
        when(multipartFile.getBytes()).thenReturn(new byte[12]);
        //when
        UserId userId = signUpService.socialSignUp(request, multipartFile);

        //then
        verify(termQuery).isAllRequiredTermIds(termAgreesSet);
        verify(oAuth2TempAttributesQuery).findById(request.getKey());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userCommand).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).usingRecursiveComparison().ignoringFieldsMatchingRegexes("userId").isEqualTo(user);

        verify(fileService, times(1)).saveFileByMultipartFile(multipartFile, userId);
        verify(fileService, times(0)).saveFileByUrl(any(), any());

        ArgumentCaptor<List<Pet>> petListCaptor = ArgumentCaptor.forClass(List.class);
        verify(petCommand).saveAll(petListCaptor.capture());
        assertThat(petListCaptor.getValue().size()).isEqualTo(1);
        assertThat(petListCaptor.getValue().get(0).getName()).isEqualTo(request.getPetInfos().get(0).getPetName());
        assertThat(petListCaptor.getValue().get(0).getPetType()).isEqualTo(request.getPetInfos().get(0).getPetType());
        assertThat(petListCaptor.getValue().get(0).getParent()).usingRecursiveComparison().ignoringFieldsMatchingRegexes("userId").isEqualTo(user);

        ArgumentCaptor<Collection<UserTermAgree>> userTermAgreeListCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(termCommand, times(1)).saveAllUserTermAgrees(userTermAgreeListCaptor.capture());
        Collection<UserTermAgree> capturedUserTermAgreeList = userTermAgreeListCaptor.getValue();
        assertThat(capturedUserTermAgreeList.size()).isEqualTo(3);
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(0))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(1))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(2))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).contains(user)).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).size()).isEqualTo(1);

        verify(oAuth2TempAttributesCommand).deleteById(oAuth2TempAttributes.getKey());
    }

    @Test
    @DisplayName("소셜 회원가입 메서드 URL 이미지 저장 테스트")
    void 소셜_회원가입_메서드_URL_이미지_저장_테스트() {
        //given
        List<Long> termAgreeOrders = List.of(1L, 2L, 3L);
        List<Term> termAgrees = List.of(
            Term.builder()
                .title("term1")
                .content("term1-content")
                .order(1L)
                .required(true)
                .build(),
            Term.builder()
                .title("term2")
                .content("term2-content")
                .order(2L)
                .required(true)
                .build(),
            Term.builder()
                .title("term3")
                .content("term3-content")
                .order(3L)
                .required(true)
                .build()
        );
        Set<Long> termAgreesSet = new HashSet<>(termAgreeOrders);

        PositionRequest position = PositionRequest.builder()
            .latitude(36.8)
            .longitude(36.8)
            .name("36.8")
            .build();

        SocialSignUpRequest request = SocialSignUpRequest.builder()
            .termAgrees(termAgreeOrders)
            .key("valid key")
            .noImage(false)
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("루이")
                    .petType(PetType.DOG)
                    .build()
            ))
            .position(position)
            .build();

        OAuth2TempAttributes oAuth2TempAttributes = OAuth2TempAttributes.builder()
            .name("이름")
            .provider("GOOGLE")
            .profileImageUrl("http://example.com")
            .email("email@example.com")
            .build();

        User user = request.toUser(oAuth2TempAttributes.getEmail(), OAuth2Provider.valueOf(oAuth2TempAttributes.getProvider()));

        when(termQuery.isAllRequiredTermIds(eq(termAgreesSet))).thenReturn(true);
        when(oAuth2TempAttributesQuery.findById(request.getKey())).thenReturn(Optional.of(oAuth2TempAttributes));
        when(userCommand.save(any(User.class))).thenReturn(user);
        when(termQuery.findAllByOrderIsIn(eq(termAgreeOrders))).thenReturn(termAgrees);
        //when
        UserId userId = signUpService.socialSignUp(request, null);

        //then
        verify(termQuery).isAllRequiredTermIds(termAgreesSet);
        verify(oAuth2TempAttributesQuery).findById(request.getKey());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userCommand).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).usingRecursiveComparison().ignoringFieldsMatchingRegexes("userId").isEqualTo(user);

        verify(fileService, times(0)).saveFileByMultipartFile(any(), any());
        verify(fileService, times(1)).saveFileByUrl(oAuth2TempAttributes.getProfileImageUrl(), userId);

        ArgumentCaptor<List<Pet>> petListCaptor = ArgumentCaptor.forClass(List.class);
        verify(petCommand).saveAll(petListCaptor.capture());
        assertThat(petListCaptor.getValue().size()).isEqualTo(1);
        assertThat(petListCaptor.getValue().get(0).getName()).isEqualTo(request.getPetInfos().get(0).getPetName());
        assertThat(petListCaptor.getValue().get(0).getPetType()).isEqualTo(request.getPetInfos().get(0).getPetType());
        assertThat(petListCaptor.getValue().get(0).getParent()).usingRecursiveComparison().ignoringFieldsMatchingRegexes("userId").isEqualTo(user);

        ArgumentCaptor<Collection<UserTermAgree>> userTermAgreeListCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(termCommand, times(1)).saveAllUserTermAgrees(userTermAgreeListCaptor.capture());
        Collection<UserTermAgree> capturedUserTermAgreeList = userTermAgreeListCaptor.getValue();
        assertThat(capturedUserTermAgreeList.size()).isEqualTo(3);
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(0))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(1))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getTerm).collect(Collectors.toList()).contains(termAgrees.get(2))).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).contains(user)).isTrue();
        assertThat(capturedUserTermAgreeList.stream().map(UserTermAgree::getUser).collect(Collectors.toSet()).size()).isEqualTo(1);

        verify(oAuth2TempAttributesCommand).deleteById(oAuth2TempAttributes.getKey());
    }

    @Test
    @DisplayName("이메일 중복회원가입 테스트")
    void checkDuplicateEmailTest() {
        //given
        String email = "hello@email.com";

        when(userQuery.existsByEmailAndProvider(email, null)).thenReturn(true);
        //when
        DuplicateEmailResponse response = signUpService.checkDuplicateEmail(email);

        //then
        verify(userQuery).existsByEmailAndProvider(email, null);
        assertThat(response.isDuplicate()).isTrue();
    }

    @Test
    @DisplayName("소셜 회원가입 임시정보 테스트")
    void getOAuth2SignUpTempInfo() {
        //given
        OAuth2TempAttributes oAuth2TempAttributes = OAuth2TempAttributes.builder()
            .email("hello@email.com")
            .provider("GOOGLE")
            .profileImageUrl("http://example.com")
            .name("name")
            .build();

        when(oAuth2TempAttributesQuery.findById(oAuth2TempAttributes.getKey())).thenReturn(Optional.of(oAuth2TempAttributes));

        //when
        SocialSignUpInfoResponse response = signUpService.getOAuth2SignUpTempInfo(oAuth2TempAttributes.getKey());
        assertThatThrownBy(() -> signUpService.getOAuth2SignUpTempInfo("123")).isInstanceOf(InvalidOAuth2TempKeyException.class);

        //then
        assertThat(response.getName()).isEqualTo(oAuth2TempAttributes.getName());
        assertThat(response.getProfileImageUrl()).isEqualTo(oAuth2TempAttributes.getProfileImageUrl());
    }
}