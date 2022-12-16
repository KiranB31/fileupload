package com.fileupload.fileupload.controller;

import com.fileupload.fileupload.entity.FileDB;
import com.fileupload.fileupload.repository.FileDBRepository;
import com.fileupload.fileupload.responce.ResponseFile;
import com.fileupload.fileupload.responce.ResponseMessage;
import com.fileupload.fileupload.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Slf4j
@Controller
@CrossOrigin("http://localhost:8081")
public class FileController {

    Logger logger = LoggerFactory.getLogger(FileController.class);


    @Autowired
    private FileStorageService storageService;

    @Autowired
    private FileDBRepository fileDBRepository;

    @PostMapping("/upload/files")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file) {
        String message = "";
        try {

            storageService.store(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename() + "!!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));


        } catch (Exception e) {
            e.printStackTrace();
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").path(dbFile.getId()).toUriString();

                    return new ResponseFile(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
                }
        ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

    @DeleteMapping("/deleteFile/{id}")
    public ResponseEntity<FileDB> deleteFiles(@PathVariable("id") String id) {
        FileDB existing = this.fileDBRepository.findById(id).get();
        this.fileDBRepository.delete(existing);
        logger.info("File Delete");
        return ResponseEntity.ok().build();
      //  return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/downloadFile/{id}")
//    public ResponseEntity<FileDB> downloadFile(@PathVariable String id, HttpServletRequest request) {
//        // Load file as Resource
//
//        FileDB fileDB = storageService.getFile(id);
//
//        // Try to determine file's content type
//        String contentType = null;
//        contentType = request.getServletContext().getMimeType(String.valueOf(fileDB.getData().hashCode()));
//
//        // Fallback to the default content type if type could not be determined
//        if(contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
//                .body(fileDB);
//    }




    }


























//    @DeleteMapping("/deleteFiles/{name}")
//    public String deleteFile(@PathVariable("name") String name,@RequestBody FileDB fileDB) {
//        FileDB existing = this.fileDBRepository.findByName(name);
//
//        if (existing.getName().equalsIgnoreCase(fileDB.getName()))
//        {
//            this.fileDBRepository.delete(existing);
//            return "Delete file";
//        }
//        else
//        {
//            return "Wrong Credentials/name";
//        }
//    }













//    @DeleteMapping("/deleteFile/files/{id}")
//    public String  deleteFiless(@PathVariable ("id") String id,@RequestBody FileDB fileDB)
//    {
//        FileDB existing = this.fileDBRepository.findById(id).get();
//
//        if (existing.getId().equalsIgnoreCase(fileDB.getId())) {
//            this.fileDBRepository.delete(existing);
//            return "Delete Successfuly";
//        } else {
//            return "Wrong Id ";
//        }

    //}
