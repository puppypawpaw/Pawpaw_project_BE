package helper;

import kr.co.pawpaw.domainrdb.auth.dto.request.SignUpRequest;

import java.lang.reflect.Field;
import java.util.List;

import static io.hypersistence.utils.hibernate.util.ReflectionUtils.setFieldValue;

public class SignUpRequestBuilder {
    private List<Long> termAgrees;
    private String nickname;
    private String id;
    private String password;
    private String passwordConfirm;
    private String phoneNumber;
    private String position;
    private String petName;
    private String petIntroduction;

    public static SignUpRequestBuilder builder() {
        return new SignUpRequestBuilder();
    }

    public SignUpRequestBuilder termAgrees(final List<Long> termAgrees) {
        this.termAgrees = termAgrees;
        return this;
    }

    public SignUpRequestBuilder nickname(final String nickname) {
        this.nickname = nickname;
        return this;
    }

    public SignUpRequestBuilder id(final String id) {
        this.id = id;
        return this;
    }

    public SignUpRequestBuilder password(final String password) {
        this.password = password;
        return this;
    }

    public SignUpRequestBuilder passwordConfirm(final String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
        return this;
    }

    public SignUpRequestBuilder phoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public SignUpRequestBuilder position(final String position) {
        this.position = position;
        return this;
    }

    public SignUpRequestBuilder petName(final String petName) {
        this.petName = petName;
        return this;
    }

    public SignUpRequestBuilder petIntroduction(final String petIntroduction) {
        this.petIntroduction = petIntroduction;
        return this;
    }

    public SignUpRequest build() {
        SignUpRequest signUpRequest = new SignUpRequest();
        for (Field field : SignUpRequestBuilder.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                setFieldValue(signUpRequest, field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return signUpRequest;
    }
}
