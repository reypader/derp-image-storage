package com.rmpader.derp.imagehost.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rmpader.derp.imagehost.config.WebConfig;
import com.rmpader.derp.imagehost.property.ImageHostProperty;

@RestController
@RequestMapping(value = "/file")
public class UploadController {

    @Autowired
    private ImageHostProperty imageHostProperties;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
                String filePath = createFilePath(fileName);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(bytes);
                stream.close();

                String imageURL = imageHostProperties.getBaseURL() + WebConfig.getResourceBaseURL() + fileName;

                return new ResponseEntity<String>(imageURL, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String createFilePath(String fileName) {
        return imageHostProperties.getUploadLocation() + fileName;
    }

    @RequestMapping(value = "/{fileName:.+}", method = RequestMethod.DELETE)
    public ResponseEntity<String> handleFileDelete(@PathVariable("fileName") String fileName) {
        String filePath = createFilePath(fileName);
        File file = new File(filePath);
        if (file.exists() && file.canWrite()) {
            file.delete();
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

    private String getFileExtension(String fileName) {
        String[] nameItems = fileName.split("\\.");
        int lastElement = nameItems.length - 1;
        return "." + nameItems[lastElement];
    }
}
