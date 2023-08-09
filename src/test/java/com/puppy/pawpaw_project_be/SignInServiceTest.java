package com.puppy.pawpaw_project_be;

import com.puppy.pawpaw_project_be.application.auth.command.SignService;
import com.puppy.pawpaw_project_be.application.pet.command.PetService;
import com.puppy.pawpaw_project_be.application.term.command.TermService;
import com.puppy.pawpaw_project_be.application.user.command.UserService;
import com.puppy.pawpaw_project_be.component.CreateTermRequestBuilder;
import com.puppy.pawpaw_project_be.component.SignInRequestBuilder;
import com.puppy.pawpaw_project_be.component.SignUpRequestBuilder;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignInRequest;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.term.dto.request.CreateTermRequest;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SignInServiceTest {
    @Autowired
    private TermService termService;
    @Autowired
    private SignService signService;
    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PetService petService;

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
    @DisplayName("로그인 테스트")
    void signInTest() throws Exception {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(true)
            .petName(true)
            .petIntroduction(true)
            .build()
            .convert();

        signService.signUp(signUpRequest);

        SignInRequest signInRequest = SignInRequestBuilder.builder()
            .id(true)
            .password(true)
            .build()
            .convert();

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
    @DisplayName("로그인 실패 테스트")
    void signInFailTest() throws Exception {
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(true)
            .petName(true)
            .petIntroduction(true)
            .build()
            .convert();

        signService.signUp(signUpRequest);

        SignInRequest signInRequest = SignInRequestBuilder.builder()
            .id(true)
            .password(false)
            .build()
            .convert();

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\""+signInRequest.getId()+"\",\"password\":\""+signInRequest.getPassword()+"\"}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is4xxClientError());
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
        SignUpRequest signUpRequest = SignUpRequestBuilder.builder()
            .termAgrees(true)
            .passwordConfirm(true)
            .petName(true)
            .petIntroduction(true)
            .build()
            .convert();

        signService.signUp(signUpRequest);

        SignInRequest signInRequest = SignInRequestBuilder.builder()
            .id(true)
            .password(true)
            .build()
            .convert();

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
