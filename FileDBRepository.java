package com.fileupload.fileupload.repository;

import com.fileupload.fileupload.entity.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface FileDBRepository extends JpaRepository<FileDB,String> {


}
