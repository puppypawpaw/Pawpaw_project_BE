package kr.co.pawpaw.domainrdb.user.service.query;

import kr.co.pawpaw.domainrdb.user.domain.OAuth2Provider;
import kr.co.pawpaw.domainrdb.user.domain.Role;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQuery {
    private final UserRepository userRepository;

    public boolean existsByEmailAndProvider(final String email, final OAuth2Provider provider) {
        return userRepository.existsByEmailAndProvider(email, provider);
    }

    public boolean existsByUserIdAndRole(
        final UserId userId,
        final Role role
    ) {
        return userRepository.existsByUserIdAndRole(userId, role);
    }

    public Optional<User> findByUserId(final UserId userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByNameAndPhoneNumber(
        final String name,
        final String phoneNumber
    ) {
        return userRepository.findByNameAndPhoneNumber(name, phoneNumber);
    }

    public Optional<User> findByEmailAndProvider(
        final String email,
        final OAuth2Provider oAuth2Provider
    ) {
        return userRepository.findByEmailAndProvider(email, oAuth2Provider);
    }

    public boolean existsByPhoneNumber(final String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}