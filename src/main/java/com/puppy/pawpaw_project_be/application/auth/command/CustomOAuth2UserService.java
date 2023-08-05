package com.puppy.pawpaw_project_be.application.auth.command;

import com.puppy.pawpaw_project_be.domain.auth.domain.OAuth2CustomUser;
import com.puppy.pawpaw_project_be.domain.auth.domain.OAuthAttributes;
import com.puppy.pawpaw_project_be.domain.auth.domain.Oauth2Provider;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
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

        Oauth2Provider provider = Oauth2Provider.valueOf(userRequest.getClientRegistration().getRegistrationId());

        OAuthAttributes attributes = OAuthAttributes.of(provider, originAttributes);
        User user = saveOrUpdate(provider, Objects.requireNonNull(attributes));
        String email = user.getId();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getAuthority()));

        return new OAuth2CustomUser(provider, originAttributes, authorities, email);
    }

    private User saveOrUpdate(
        final Oauth2Provider provider,
        final OAuthAttributes authAttributes
    ) {
        User user = userRepository.findByIdAndProvider(authAttributes.getEmail(), provider)
            .map(entity -> entity.update(authAttributes.getName(), authAttributes.getProfileImageUrl()))
            .orElse(authAttributes.toEntity(""));

        return userRepository.save(user);
    }
}
