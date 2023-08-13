package com.puppy.pawpaw_project_be.domain.boardImg.repository;

import com.puppy.pawpaw_project_be.domain.boardImg.entity.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
}
