package com.puppy.pawpaw_project_be.domain.board.service;

import com.puppy.pawpaw_project_be.domain.board.entity.Board;
import com.puppy.pawpaw_project_be.domain.board.entity.BoardLikes;
import com.puppy.pawpaw_project_be.domain.board.repository.BoardLikesRepository;
import com.puppy.pawpaw_project_be.domain.board.repository.BoardRepository;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import com.puppy.pawpaw_project_be.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikesRepository likesRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public boolean addOrDeleteLike(Long boardId, UserId userId){
        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        if (likesNotExist(user, board)){
           likesRepository.save(new BoardLikes(user, board));
           board.plusLikedCount();
            return true;
        }else {
            likesRepository.deleteBoardLikesByUserAndBoard(user, board);
            board.minusLikedCount();
            return false;
        }
    }

    private boolean likesNotExist(User user, Board board){
        return likesRepository.findByUserAndBoard(user, board).isEmpty();  // 좋아요가 없으면 true
    }
}
