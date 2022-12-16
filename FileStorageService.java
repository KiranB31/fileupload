package com.fileupload.fileupload.service;

import com.fileupload.fileupload.entity.FileDB;
import com.fileupload.fileupload.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;

    public void store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
       fileDBRepository.save(FileDB);
    }


    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();

    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }



}