package com.ecommerceProject.springboot_ecommerceProject.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // File Names Of Current / Original File
        String originalFileName = file.getOriginalFilename();

        // To Avoid Name Conflicts I Will Generate A Random Unique (UUID) File Name
        String randomId = UUID.randomUUID().toString();

        // Append The Original File's Extension mat.jpg ---> 1234  ----> 1234.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        // Check If Path Exists  If Not Create The Path Of The Directory
        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdir();
        }

        // Upload To The Server
        // File Is The Image File Which I Received As Image
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}
