package com.puppy.pawpaw_project_be.domain.board.repository;

import com.puppy.pawpaw_project_be.domain.board.entity.Board;
import com.puppy.pawpaw_project_be.domain.board.entity.BoardLikes;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    Optional<BoardLikes> deleteBoardLikesByUserAndBoard(User user, Board board);
    Optional<BoardLikes> findByUserAndBoard(User user, Board board);

}
