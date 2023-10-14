package kr.co.pawpaw.api.config.auth.service;

import kr.co.pawpaw.api.config.auth.object.OAuth2Attributes;
import kr.co.pawpaw.api.config.auth.object.OAuth2CustomUser;
import kr.co.pawpaw.mysql.user.domain.OAuth2Provider;
import kr.co.pawpaw.mysql.user.domain.Role;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserQuery userQuery;
    private static final String EmptyUserIdValue = "isEmpty";

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);

        Map<String, Object> originAttributes = new HashMap<>(oAuth2User.getAttributes());

        OAuth2Provider provider = OAuth2Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2Attributes attributes = OAuth2Attributes.of(provider, originAttributes);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.USER.getAuthority()));
        UserId userId = getUserId(provider, Objects.requireNonNull(attributes));

        if (userId.getValue().equals(EmptyUserIdValue)) {
            originAttributes.put("oAuth2Attributes", attributes);
        }

        return new OAuth2CustomUser(originAttributes, authorities, userId);
    }

    private UserId getUserId(
        final OAuth2Provider provider,
        final OAuth2Attributes authAttributes
    ) {
        return userQuery.findByEmailAndProvider(authAttributes.getEmail(), provider)
            .map(User::getUserId)
            .orElse(UserId.of(EmptyUserIdValue));
    }
}
