package kr.co.pawpaw.mysql.user.service.query;

import kr.co.pawpaw.mysql.chatroom.dto.ChatroomNonParticipantResponse;
import kr.co.pawpaw.mysql.user.domain.OAuth2Provider;
import kr.co.pawpaw.mysql.user.domain.Role;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.repository.UserCustomRepository;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQuery {
    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;

    public List<ChatroomNonParticipantResponse> searchChatroomNonParticipant(final Long chatroomId, final String nicknameSearchKeyword) {
        return userCustomRepository.searchChatroomNonParticipant(chatroomId, nicknameSearchKeyword);
    }

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

    public Optional<User> findByNameAndEmailAndProvider(
        final String name,
        final String email,
        final OAuth2Provider provider
    ) {
        return userRepository.findByNameAndEmailAndProvider(name, email, provider);
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

    public User getReferenceById(final UserId userId) {
        return userRepository.getReferenceById(userId);
    }
}