package com.puppy.pawpaw_project_be;

import com.puppy.pawpaw_project_be.application.auth.command.SignService;
import com.puppy.pawpaw_project_be.application.user.command.UserService;
import com.puppy.pawpaw_project_be.application.user.query.UserQuery;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignInRequest;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.exception.user.DuplicateIdException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SignServiceTest {
    @Autowired
    private SignService signService;
    @Autowired
    private UserQuery userQuery;
    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    private final SignUpRequest signUpRequest = SignUpRequest.builder()
        .id("testId")
        .password("1234qwer!@#$")
        .nickname("춘식이")
        .phoneNumber("01012344321")
        .build();

    private final SignInRequest signInRequest = SignInRequest.builder()
        .id("testId")
        .password("1234qwer!@#$")
        .build();

    @BeforeEach
    void beforeEach() {
        userService.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void signUpTest() {
        signService.signUp(signUpRequest);
        assertThatThrownBy(() -> userQuery.checkDuplication(signUpRequest)).isInstanceOf(DuplicateIdException.class);
    }

    @Test
    @DisplayName("회원 가입 아이디 중복 예외 테스트")
    void signUpDuplicateIdExceptionTest() {
        signService.signUp(signUpRequest);
        assertThatThrownBy(() -> signService.signUp(signUpRequest)).isInstanceOf(DuplicateIdException.class);
    }

    @Test
    @DisplayName("로그인 테스트")
    void signInTest() throws Exception {
        signService.signUp(signUpRequest);

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\""+signInRequest.getId()+"\",\"password\":\""+signInRequest.getPassword()+"\"}"))
                .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(signUpRequest.getId()))
            .andExpect(jsonPath("$.role").value("USER"))
            .andExpect(jsonPath("$.nickname").value(signUpRequest.getNickname()))
            .andExpect(jsonPath("$.phoneNumber").value(signUpRequest.getPhoneNumber()));
    }

    @Test
    @DisplayName("로그아웃 실패 테스트")
    void signOutFailTest() throws Exception {
        mockMvc.perform(delete("/api/auth"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("U003"))
            .andExpect(jsonPath("$.message").value("로그인 상태가 아닙니다."));
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    void signOutSuccessTest() throws Exception {
        signService.signUp(signUpRequest);

        MockHttpServletResponse loginResponse = mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\""+signInRequest.getId()+"\",\"password\":\""+signInRequest.getPassword()+"\"}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(signInRequest.getId()))
            .andExpect(jsonPath("$.role").value("USER"))
            .andExpect(jsonPath("$.nickname").value(signUpRequest.getNickname()))
            .andExpect(jsonPath("$.phoneNumber").value(signUpRequest.getPhoneNumber()))
            .andReturn().getResponse();

        mockMvc.perform(delete("/api/auth")
                .cookie(loginResponse.getCookies()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNoContent());
    }
}
