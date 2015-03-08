package com.rmpader.derp.imagehost.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rmpader.derp.imagehost.config.WebConfig;

@RestController
@RequestMapping(value = "/file")
public class UploadController {

	@RequestMapping(method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {

				byte[] bytes = file.getBytes();
				String fileName = UUID.randomUUID().toString()
						+ getFileExtension(file.getOriginalFilename());
				String path = "/home/reynald/public/" + fileName;
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(path)));
				stream.write(bytes);
				stream.close();
				String baseURL = ControllerLinkBuilder.linkTo(getClass())
						.withSelfRel().getHref();
				return baseURL + WebConfig.getResourceBaseURL() + fileName;
			} catch (Exception e) {
				return "Failed to upload => " + e.getMessage();
			}
		} else {
			return "File was empty.";
		}
	}

	@RequestMapping(value = "/{fileName:.+}", method = RequestMethod.DELETE)
	public ResponseEntity<String> handleFileDelete(
			@PathVariable("fileName") String fileName) {
		String path = "/home/reynald/public/" + fileName;
		File file = new File(path);
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
