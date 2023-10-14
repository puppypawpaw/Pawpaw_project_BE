package kr.co.pawpaw.api.config.annotation;

import kr.co.pawpaw.mysql.user.domain.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 유저의 권한을 확인하는 annotation default는 관리자
 * Method의 Parameter에 UserId가 존재해야 됨
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {
    Role role() default Role.ADMIN;
}
