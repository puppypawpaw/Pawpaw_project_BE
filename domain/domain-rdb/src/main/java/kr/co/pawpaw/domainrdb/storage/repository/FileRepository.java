package kr.co.pawpaw.domainrdb.storage.repository;

import kr.co.pawpaw.domainrdb.storage.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
