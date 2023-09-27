package kr.co.pawpaw.api.util.mail;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class ChangePasswordMailContent implements MailContent {
    private final String linkUrl;
    public static final String subject = "비밀번호 변경";

    @Override
    public String getContent() {
        StringBuilder sb = new StringBuilder();

        sb.append("<tr><td style='font-size:20px;font-weight:bold;line-height:27px;letter-spacing:-0.6px'>PawPaw 비밀번호 변경 주소를 보내드려요.</td></tr><tr><td height='8px'></td></tr>");
        sb.append("<tr><td style='font-size:15px;line-height:27px;letter-spacing:-0.6px'>아래 링크를 클릭하시면, 비밀번호를 변경할 수 있어요.</td></tr>");
        sb.append("<tr><td height='32px'></td></tr><tr><td style='line-height:27px;letter-spacing:-0.6px'><span style='font-size:15px;'>링크: </span>");
        sb.append(linkUrl);
        sb.append("</td></tr><tr><td height='48px'></td></tr>");

        return sb.toString();
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
