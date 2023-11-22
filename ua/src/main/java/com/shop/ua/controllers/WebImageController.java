package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.Image;
import com.shop.ua.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class WebImageController {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private RepositoryManager repositoryManager;
    @Autowired
    private final ResourceLoader resourceLoader;

    @GetMapping("/images/{id}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable Long id) throws IOException {
        Image image = repositoryManager.getGoodsService().getImageById(id);

        if (image != null) {
            File file = new File(image.getImagePath());
            return ResponseEntity.ok()
                    .header("Content-Type", image.getContentType())
                    .body(new FileSystemResource(file));
        } else {
            // Обробка випадку, коли зображення не знайдено
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/images/{id}")
//    private ResponseEntity<?> getImageById(@PathVariable Long id) {
//        Image image = repositoryManager.getImageRepository().findById(id).orElse(null);
//
//        if (image != null) {
//            String imagePath = "classpath:static/images/" + image.getImagePath(); // шлях до ресурсів
//            Resource resource = resourceLoader.getResource(imagePath);
//            logger.debug("0 Processing request for image with id: {}", id);
//
//
//            if (resource.exists()) {
//                try {
//                    return ResponseEntity.ok()
//                            .header("fileName", image.getOriginalFileName())
//                            .contentType(MediaType.valueOf(image.getContentType()))
//                            .contentLength(image.getSize())
//                            .body(new InputStreamResource(resource.getInputStream()));
//                } catch (IOException e) {
//                    // Обробка винятку, якщо не вдається отримати доступ до потоку вводу
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
//                }
//
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//
//
//    }


}
