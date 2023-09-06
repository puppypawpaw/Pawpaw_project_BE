package kr.co.pawpaw.api.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailUtil {
    public String getMaskedEmail(final String email) {
        String[] emailSplit = email.split("@");
        String front = emailSplit[0];
        String back = emailSplit[1];

        String needReplace = front.substring(Math.max(0, front.length() - 3));

        return front.replace(needReplace, "*".repeat(needReplace.length()))
            + "@" + back;
    }
}
