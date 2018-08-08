package com.springboot.billing.app.model.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements UploadFileService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final static String UPLOAD_FOLDER = "uploads";

	@Override
	public Resource load(String filename) throws MalformedURLException {

		Path path = Paths.get(UPLOAD_FOLDER).resolve(filename).toAbsolutePath();

		Resource resource = new UrlResource(path.toUri());

		if (!resource.exists() || !resource.isReadable()) {
			throw new RuntimeException("Error: was not possible load " + path.getFileName());
		}

		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {

		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(uniqueFileName);

		Path absolutePath = rootPath.toAbsolutePath();

		logger.info("rootPath: " + rootPath);
		logger.info("absolutePath: " + absolutePath);

		Files.copy(file.getInputStream(), absolutePath);

		return uniqueFileName;
	}

	@Override
	public void delete(String filename) {

		Path path = Paths.get(UPLOAD_FOLDER).resolve(filename).toAbsolutePath();
		File photoFile = path.toFile();

		if (photoFile.exists() && photoFile.canRead()) {

			if (photoFile.delete()) {
				logger.info("File " + filename + " removed successfully");
			} else {
				logger.info("Was not possible remove " + filename);
			}
		}
	}

	@Override
	public void deleteAll() {

		FileSystemUtils.deleteRecursively(Paths.get(UPLOAD_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		
		Files.createDirectories(Paths.get(UPLOAD_FOLDER));
	}

}
