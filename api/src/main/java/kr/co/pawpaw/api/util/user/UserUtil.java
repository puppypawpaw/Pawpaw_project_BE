package kr.co.pawpaw.api.util.user;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtil {
    private static final String userDefaultImageName = "유저 기본 이미지.jpg";
    public String getUserDefaultImageName() {
        return userDefaultImageName;
    }
}
