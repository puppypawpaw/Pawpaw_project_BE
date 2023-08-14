package com.puppy.pawpaw_project_be.domain.board.repository;

import com.puppy.pawpaw_project_be.domain.board.entity.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>{

    @EntityGraph(attributePaths = {"imgSet"}) // board와  Lazy 연관관계를 맺고 있는 img도 한번에 가져오게함
    @Query("select b from Board b where b.id =:id")
    Optional<Board> findByIdWithImages(@Param("id") Long id);
}
