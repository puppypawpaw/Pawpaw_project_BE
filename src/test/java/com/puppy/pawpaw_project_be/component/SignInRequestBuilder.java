package com.puppy.pawpaw_project_be.component;

import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignInRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;

import static io.hypersistence.utils.hibernate.util.ReflectionUtils.setFieldValue;

@Builder
@AllArgsConstructor
public class SignInRequestBuilder {
    private boolean id;
    private boolean password;

    public SignInRequest convert() {
        SignInRequest target = new SignInRequest();

        // 존재하는 아이디인지
        if (id) {
            setFieldValue(target, "id", "chunsik");
        } else {
            setFieldValue(target, "id", "daesik");
        }

        // 비밀번호가 맞는지
        if (password) {
            setFieldValue(target, "password", "chunsikMom");
        } else {
            setFieldValue(target, "password", "chunsikDad");
        }

        return target;
    }
}