package kr.co.pawpaw.api.config.auth.object;

import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class OAuth2CustomUser implements OAuth2User, Serializable {
    private final Map<String, Object> attributes;
    private final List<GrantedAuthority> authorities;
    private final UserId userId;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return this.userId.getValue();
    }
}
