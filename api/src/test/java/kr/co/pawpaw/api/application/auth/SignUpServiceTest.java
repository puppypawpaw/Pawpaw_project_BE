package kr.co.pawpaw.api.application.auth;

import kr.co.pawpaw.api.dto.auth.SignUpRequest;
import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.common.exception.auth.DuplicateEmailException;
import kr.co.pawpaw.common.exception.auth.DuplicatePhoneNumberException;
import kr.co.pawpaw.common.exception.auth.NotVerifiedPhoneNumberException;
import kr.co.pawpaw.common.exception.term.NotAgreeAllRequiredTermException;
import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import kr.co.pawpaw.domainrdb.pet.service.command.PetCommand;
import kr.co.pawpaw.domainrdb.sms.domain.SmsUsagePurpose;
import kr.co.pawpaw.domainrdb.term.domain.Term;
import kr.co.pawpaw.domainrdb.term.domain.UserTermAgree;
import kr.co.pawpaw.domainrdb.term.service.command.TermCommand;
import kr.co.pawpaw.domainrdb.term.service.query.TermQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.service.command.UserCommand;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private OAuth2TempAttributesQuery oAuth2TempAttributesQuery;
    @Mock
    private MultipartFile file;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    @DisplayName("일반 회원가입 메소드 유저 중복 테스트")
    void 일반_회원가입_메소드_유저_중복_테스트() {
        //given
        String alreadyExistEmail = "aee";
        SignUpRequest request = SignUpRequest.builder()
            .email(alreadyExistEmail)
            .phoneNumber("01012345678")
            .build();

        when(userQuery.existsByEmailAndProvider(any(String.class), any())).thenReturn(true);
        //when
        assertThatThrownBy(() -> signUpService.signUp(request, file)).isInstanceOf(DuplicateEmailException.class);

        //then
        verify(userQuery, times(1)).existsByEmailAndProvider(request.getEmail(), null);
    }

    @Test
    @DisplayName("일반 회원가입 메소드 필수 약관 동의 테스트")
    void 일반_회원가입_메소드_필수_약관_동의_테스트() {
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
        assertThatThrownBy(() -> signUpService.signUp(request, file)).isInstanceOf(NotAgreeAllRequiredTermException.class);

        //then
        verify(userQuery, times(1)).existsByEmailAndProvider(request.getEmail(), null);
        verify(termQuery, times(1)).isAllRequiredTermIds(termAgreesSet);
    }

    @Test
    @DisplayName("일반 회원가입 메소드 핸드폰 번호 유효성 테스트")
    void 일반_회원가입_메소드_핸드폰_번호_유효성_테스트() {
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
        assertThatThrownBy(() -> signUpService.signUp(request1, file)).isInstanceOf(DuplicatePhoneNumberException.class);
        assertThatThrownBy(() -> signUpService.signUp(request2, file)).isInstanceOf(NotVerifiedPhoneNumberException.class);

        //then
        verify(userQuery, times(2)).existsByEmailAndProvider(email, null);
        verify(termQuery, times(2)).isAllRequiredTermIds(termAgreesSet);
        verify(userQuery, times(1)).existsByPhoneNumber(request1.getPhoneNumber());
        verify(userQuery, times(1)).existsByPhoneNumber(request2.getPhoneNumber());
        verify(verifiedPhoneNumberQuery, times(1)).existsByPhoneNumberAndUsagePurpose(request2.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name());
    }

    @Test
    @DisplayName("일반 회원가입 메소드 작동 테스트")
    void 일반_회원가입_메소드_작동_테스트() {
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

        User savedUser = request.toUser(passwordEncoded);

        when(userQuery.existsByEmailAndProvider(any(String.class), any())).thenReturn(false);
        when(termQuery.isAllRequiredTermIds(eq(termAgreesOrderSet))).thenReturn(true);
        when(userQuery.existsByPhoneNumber(eq(request.getPhoneNumber()))).thenReturn(false);
        when(verifiedPhoneNumberQuery.existsByPhoneNumberAndUsagePurpose(eq(request.getPhoneNumber()), eq(SmsUsagePurpose.SIGN_UP.name()))).thenReturn(true);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(passwordEncoded);
        when(userCommand.save(any(User.class))).thenReturn(savedUser);
        when(termQuery.findAllByOrderIsIn(eq(termAgreeOrders))).thenReturn(termAgrees);
        //when
        signUpService.signUp(request, file);

        //then
        verify(userQuery, times(1)).existsByEmailAndProvider(email, null);
        verify(termQuery, times(1)).isAllRequiredTermIds(termAgreesOrderSet);
        verify(userQuery, times(1)).existsByPhoneNumber(request.getPhoneNumber());
        verify(verifiedPhoneNumberQuery, times(1)).existsByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name());

        verify(passwordEncoder, times(1)).encode(request.getPassword());
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userCommand, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getPassword()).isEqualTo(passwordEncoded);
        assertThat(capturedUser.getNickname()).isEqualTo(request.getNickname());
        assertThat(capturedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(capturedUser.getPosition().getLatitude()).isEqualTo(request.getPosition().getLatitude());
        assertThat(capturedUser.getPosition().getLongitude()).isEqualTo(request.getPosition().getLongitude());
        assertThat(capturedUser.getPosition().getName()).isEqualTo(request.getPosition().getName());
        assertThat(capturedUser.getPhoneNumber()).isEqualTo(request.getPhoneNumber());

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
    @DisplayName("소셜 회원가입 테스트")
    void socialSignUp() {
        //given
    }
}