package com.puppy.pawpaw_project_be.component;

import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Arrays;

import static io.hypersistence.utils.hibernate.util.ReflectionUtils.setFieldValue;

@Builder
@AllArgsConstructor
public class SignUpRequestBuilder {
    private boolean termAgrees;
    private boolean passwordConfirm;
    private boolean petName;
    private boolean petIntroduction;

    public SignUpRequest convert() {
        SignUpRequest target = new SignUpRequest();

        // 필수 동의 여부 체크 용도
        if (termAgrees) {
            setFieldValue(target, "termAgrees", Arrays.asList(1L, 2L, 3L));
        } else {
            setFieldValue(target, "termAgrees", Arrays.asList(1L, 2L, 4L));
        }

        setFieldValue(target, "nickname", "춘식맘");
        setFieldValue(target, "id", "chunsik");
        setFieldValue(target, "password", "chunsikMom");

        // 비밀번호 확인 같은지 다른지
        if (passwordConfirm) {
            setFieldValue(target, "passwordConfirm", "chunsikMom");
        } else {
            setFieldValue(target, "passwordConfirm", "chunsikDad");
        }

        setFieldValue(target, "phoneNumber", "01012345678");

        setFieldValue(target, "position", "서울 강동구");

        // petName 길이 제한 확인
        if (petName) {
            setFieldValue(target, "petName", "춘식이1");
        } else {
            setFieldValue(target, "petName", "춘식이1234567891011121314151617181920212223242526272829303132333435363738394041424344");
        }

        // petIntroduction 길이 제한 확인
        if (petIntroduction) {
            setFieldValue(target, "petIntroduction", "니 이름은 이제부터 춘식이1이여");
        } else {
            setFieldValue(target, "petIntroduction", "니 이름은 이제부터 춘식이1234567891011121314151617181920212223242526272829303132333435363738394041424344이여");
        }

        return target;
    }
}