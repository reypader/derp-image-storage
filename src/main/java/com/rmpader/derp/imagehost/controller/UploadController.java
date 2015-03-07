package com.rmpader.derp.imagehost.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rmpader.derp.imagehost.config.WebConfig;

@RestController
public class UploadController {

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String handleFileUpload(
			@RequestParam("file") MultipartFile file) {
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

	private String getFileExtension(String fileName) {
		String[] nameItems = fileName.split("\\.");
		int lastElement = nameItems.length - 1;
		return "." + nameItems[lastElement];
	}
}
