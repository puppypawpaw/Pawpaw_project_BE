package com.puppy.pawpaw_project_be;

import com.puppy.pawpaw_project_be.application.auth.command.SignService;
import com.puppy.pawpaw_project_be.application.pet.command.PetService;
import com.puppy.pawpaw_project_be.application.pet.query.PetQuery;
import com.puppy.pawpaw_project_be.application.term.command.TermService;
import com.puppy.pawpaw_project_be.application.term.query.TermQuery;
import com.puppy.pawpaw_project_be.application.user.command.UserService;
import com.puppy.pawpaw_project_be.application.user.query.UserQuery;
import com.puppy.pawpaw_project_be.component.CreateTermRequestBuilder;
import com.puppy.pawpaw_project_be.component.SignUpRequestBuilder;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.pet.domain.Pet;
import com.puppy.pawpaw_project_be.domain.term.dto.request.CreateTermRequest;
import com.puppy.pawpaw_project_be.exception.auth.NotEqualPasswordConfirmException;
import com.puppy.pawpaw_project_be.exception.pet.InvalidPetIntroductionException;
import com.puppy.pawpaw_project_be.exception.pet.InvalidPetNameException;
import com.puppy.pawpaw_project_be.exception.term.NotAgreeAllRequiredTermException;
import com.puppy.pawpaw_project_be.exception.user.DuplicateIdException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SignUpServiceTest {
    @Autowired
    private TermService termService;
    @Autowired
    private SignService signService;
    @Autowired
    private UserQuery userQuery;
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;
    @Autowired
    private PetQuery petQuery;
    @Autowired
    private TermQuery termQuery;

    private void intializeTerm() {
        CreateTermRequest request1 = CreateTermRequestBuilder.builder()
            .title("약관 1 제목")
            .content("약관 1 내용")
            .required(true)
            .order(1L)
            .build()
            .convert();

        CreateTermRequest request2 = CreateTermRequestBuilder.builder()
            .title("약관 2 제목")
            .content("약관 2 내용")
            .required(true)
            .order(2L)
            .build()
            .convert();

        CreateTermRequest request3 = CreateTermRequestBuilder.builder()
            .title("약관 3 제목")
            .content("약관 3 내용")
            .required(true)
            .order(3L)
            .build()
            .convert();

        CreateTermRequest request4 = CreateTermRequestBuilder.builder()
            .title("약관 4 제목")
            .content("약관 4 내용")
            .required(false)
            .order(4L)
            .build()
            .convert();

        termService.deleteAllTermAgree();
        termService.deleteAllTerm();
        termService.createTerm(request1);
        termService.createTerm(request2);
        termService.createTerm(request3);
        termService.createTerm(request4);
    }

    @BeforeEach
    void beforeEach() {
        intializeTerm();
        petService.deleteAll();
        userService.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(true)
            .petName(true)
            .petIntroduction(true)
            .build()
            .convert();

        signService.signUp(signUpRequest);
        assertThatThrownBy(() -> userQuery.checkDuplication(signUpRequest)).isInstanceOf(DuplicateIdException.class);
    }

    @Test
    @DisplayName("회원가입 아이디 중복 예외 테스트")
    void signUpDuplicateIdExceptionTest() {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(true)
            .petName(true)
            .petIntroduction(true)
            .build()
            .convert();

        signService.signUp(signUpRequest);
        assertThatThrownBy(() -> signService.signUp(signUpRequest)).isInstanceOf(DuplicateIdException.class);
    }

    @Test
    @DisplayName("회원가입 약관 필수 동의 예외 테스트")
    void signUpTermExceptionTest() {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(false)
            .passwordConfirm(true)
            .petName(true)
            .petIntroduction(true)
            .build()
            .convert();

        assertThatThrownBy(() -> signService.signUp(signUpRequest)).isInstanceOf(NotAgreeAllRequiredTermException.class);
    }

    @Test
    @DisplayName("회원가입 비밀번호 확인 예외 테스트")
    void signUpPasswordConfirmExceptionTest() {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(false)
            .petName(true)
            .petIntroduction(true)
            .build()
            .convert();

        assertThatThrownBy(() -> signService.signUp(signUpRequest)).isInstanceOf(NotEqualPasswordConfirmException.class);
    }

    @Test
    @DisplayName("회원가입 반려동물 이름 길이 테스트")
    void signUpPetNameExceptionTest() {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(true)
            .petName(false)
            .petIntroduction(true)
            .build()
            .convert();

        assertThatThrownBy(() -> signService.signUp(signUpRequest)).isInstanceOf(InvalidPetNameException.class);
    }

    @Test
    @DisplayName("회원가입 반려동물 소개 길이 테스트")
    void signUpPetIntroductionExceptionTest() {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(true)
            .petName(true)
            .petIntroduction(false)
            .build()
            .convert();

        assertThatThrownBy(() -> signService.signUp(signUpRequest)).isInstanceOf(InvalidPetIntroductionException.class);
    }

    @Test
    @DisplayName("회원가입 약관동의 여부 테스트")
    void termAgreeTest() {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(true)
            .petName(true)
            .petIntroduction(true)
            .build()
            .convert();

        signService.signUp(signUpRequest);

        Set<Long> agreeTermOrders = new HashSet<>(signUpRequest.getTermAgrees());
        Set<Long> agreeTermOrdersSaved = termQuery.getUserAgreeTermOrdersByUserId(signUpRequest.getId());

        assertThat(agreeTermOrdersSaved.equals(agreeTermOrders)).isEqualTo(true);
    }

    @Test
    @DisplayName("회원가입 펫 등록 여부 테스트")
    void petRegistrationTest() {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(true)
            .petName(true)
            .petIntroduction(true)
            .build()
            .convert();

        signService.signUp(signUpRequest);

        List<Pet> pets = petQuery.getAllPetByUserId(signUpRequest.getId());

        assertThat(pets.size()).isEqualTo(1);
        assertThat(pets.get(0).getName()).isEqualTo(signUpRequest.getPetName());
        assertThat(pets.get(0).getIntroduction()).isEqualTo(signUpRequest.getPetIntroduction());
    }
}
