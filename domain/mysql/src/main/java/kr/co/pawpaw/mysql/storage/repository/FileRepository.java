package kr.co.pawpaw.mysql.storage.repository;

import kr.co.pawpaw.mysql.storage.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
