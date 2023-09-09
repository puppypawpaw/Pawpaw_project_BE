package kr.co.pawpaw.api.util.mail;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MailUtil {
    public String getText(final MailContent mailType) {
        StringBuilder sb = new StringBuilder();

        sb.append("<table width='100%' cellspacing='0' cellpadding='0' border='0' align='center' bgcolor='#ffffff' style='max-width:640px;margin:0 auto;padding:10px'><tbody><tr><td height='60px'></td></tr><tr>");
        sb.append("<td style='text-align:center; font-size:30px; font-weight:bold;'>PawPaw</td>");
        sb.append("</tr><tr><td height='40px'></td></tr>");
        sb.append(mailType.getContent());
        sb.append("<tr><td height='48px'></td></tr><tr><td height='2px' bgcolor='#212529'></td></tr><tr><td height='32px'></td></tr><tr><td style='font-size:13px;line-height:20px;letter-spacing:-0.26px;color:#868e96'>이 메일은 PawPaw의 발신 전용 메일이에요.</td></tr><tr><td height='80px'></td></tr></tbody></table>");

        return sb.toString();
    }
}
