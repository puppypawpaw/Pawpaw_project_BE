package kr.co.pawpaw.api.application.auth.command;

import kr.co.pawpaw.domainrdb.auth.OAuth2CustomUser;
import kr.co.pawpaw.domainrdb.auth.OAuth2Attributes;
import kr.co.pawpaw.domainrdb.auth.OAuth2Provider;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(
        final UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);

        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        OAuth2Provider provider = OAuth2Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2Attributes attributes = OAuth2Attributes.of(provider, originAttributes);
        User user = saveOrUpdate(provider, Objects.requireNonNull(attributes));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getAuthority()));

        return new OAuth2CustomUser(originAttributes, authorities, user.getUserId());
    }

    private User saveOrUpdate(
        final OAuth2Provider provider,
        final OAuth2Attributes authAttributes
    ) {
        User user = userRepository.findByIdAndProvider(authAttributes.getEmail(), provider)
            .map(entity -> entity.update(authAttributes.getName(), authAttributes.getProfileImageUrl()))
            .orElse(authAttributes.toEntity(""));

        return userRepository.save(user);
    }
}
