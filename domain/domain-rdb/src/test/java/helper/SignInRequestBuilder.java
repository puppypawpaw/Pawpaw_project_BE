package helper;

import kr.co.pawpaw.domainrdb.auth.dto.request.SignInRequest;

import java.lang.reflect.Field;

import static io.hypersistence.utils.hibernate.util.ReflectionUtils.setFieldValue;

public class SignInRequestBuilder {
    private String id;
    private String password;

    public static SignInRequestBuilder builder() {
        return new SignInRequestBuilder();
    }

    public SignInRequestBuilder id(final String id) {
        this.id = id;
        return this;
    }

    public SignInRequestBuilder password(final String password) {
        this.password = password;
        return this;
    }

    public SignInRequest build() {
        SignInRequest signInRequest = new SignInRequest();
        for (Field field : SignInRequestBuilder.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                setFieldValue(signInRequest, field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return signInRequest;
    }
}
