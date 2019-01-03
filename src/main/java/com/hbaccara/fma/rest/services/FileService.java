package com.hbaccara.fma.rest.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hbaccara.fma.dto.FileDto;
import com.hbaccara.fma.entities.File;
import com.hbaccara.fma.entities.User;
import com.hbaccara.fma.mappers.FileMapper;
import com.hbaccara.fma.repository.FileRepository;
import com.hbaccara.fma.repository.UserRepository;
import com.hbaccara.fma.rest.resp.ListDirectoryResponse;

@Service
public class FileService {

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileMapper fileMapper;

	public FileDto handleFileUpload(Long userId, Long parentId, MultipartFile multipartFile) throws IOException {

		User user = userRepository.findById(userId).get();

		boolean isRootdirectory = parentId == -1;
		File parent = null;
		if (isRootdirectory) {
			parentId = null;
		} else {
			parent = fileRepository.findById(parentId).orElseGet(null);
		}

		File file = new File();

		file.setName(multipartFile.getOriginalFilename());
		file.setContent(multipartFile.getBytes());
		file.setSize(multipartFile.getBytes().length);
		file.setUpdateDate(new Date());
		file.setParent(parent);
		file.setFolder(false);
		file.setOwner(user);

		fileRepository.save(file);

		FileDto fileDto = fileMapper.fileToFileDto(file);

		return fileDto;

	}

	public Object[] download(Long id) throws IOException {

		String downloadFilename = null;
		byte[] fileContent = null;

		File file = fileRepository.findById(id).get();

		if (!file.isFolder()) {

			fileContent = file.getContent();

			downloadFilename = file.getName();
		}

		else { 
			// compress the folder
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ZipOutputStream zos = new ZipOutputStream(bos)) {

				compressFolder(file.getId(), "", zos);

				zos.finish();

				fileContent = bos.toByteArray();

			}

			downloadFilename = file.getName() + ".zip";
		}

		return new Object[] { downloadFilename, fileContent };
	}

	private List<File> getHierarchy(File folder) {

		List<File> result = new ArrayList<>();

		File parent = folder.getParent();

		if (parent == null) {
			result.add(folder);
			return result;
		}

		result.addAll(getHierarchy(parent));
		result.add(folder);
		return result;
	}

	public ListDirectoryResponse listDirectory(Long directoryId, Long userId) throws Exception {

		ListDirectoryResponse response = new ListDirectoryResponse();

		File folder = null;
		List<FileDto> hierarchyDtos = new ArrayList<>();

		boolean isRootdirectory = directoryId == -1;

		if (isRootdirectory) {
			directoryId = null;
		} else {
			folder = fileRepository.findById(directoryId).orElseGet(null);
			if (folder == null) {
				throw new Exception("Folder doesn't exist!");
			}
			List<File> hierarchy = getHierarchy(folder);

			for (File file : hierarchy) {

				FileDto fileDto = fileMapper.fileToFileDto(file);

				hierarchyDtos.add(fileDto);
			}
		}

		List<FileDto> fileDtos = null;

		List<File> files;

		if (isRootdirectory) {

			files = fileRepository.findAllRootElementsByOwner(userId);
			List<File> sharedFiles = fileRepository.findSharedElementsByUser(userId);
			files.addAll(sharedFiles);
		} else {
			files = fileRepository.findAllElementsByFolder(directoryId);
		}

		fileDtos = new ArrayList<>();

		for (File file : files) {

			FileDto fileDto = fileMapper.fileToFileDto(file);

			fileDtos.add(fileDto);
		}

		response.setFiles(fileDtos);
		response.setHierarchy(hierarchyDtos);

		return response;
	}

	public FileDto createFolder(Long userId, Long parentId, String name) {

		User user = userRepository.findById(userId).get();

		boolean isRootdirectory = parentId == -1;
		File parent = null;
		if (isRootdirectory) {
			parentId = null;
		} else {
			parent = fileRepository.findById(parentId).get();
		}

		File file = new File();

		file.setName(name);
		file.setContent(null);
		file.setUpdateDate(new Date());
		file.setParent(parent);
		file.setFolder(true);
		file.setOwner(user);

		fileRepository.save(file);

		FileDto fileDto = fileMapper.fileToFileDto(file);

		return fileDto;
	}

	public void delete(Long id) {

		File file = fileRepository.findById(id).get();

		fileRepository.delete(file);

	}

	public FileDto rename(Long id, String newName) {

		File file = fileRepository.findById(id).get();
		file.setName(newName);
		file.setUpdateDate(new Date());
		fileRepository.save(file);

		FileDto fileDto = fileMapper.fileToFileDto(file);

		return fileDto;
	}

	private void compressFolder(Long folderId, String path, ZipOutputStream zos) throws IOException {

		List<File> files = fileRepository.findAllElementsByFolder(folderId);

		// first, compress the files under the current folder
		for (File file : files.stream().filter(f -> !f.isFolder()).collect(Collectors.toList())) {

			byte[] fileContent = file.getContent();

			try (ByteArrayInputStream in = new ByteArrayInputStream(fileContent)) {

				ZipEntry ze = new ZipEntry(path + file.getName());
				zos.putNextEntry(ze);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
			}
		}

		// compress the folders under the current folder
		for (File file : files.stream().filter(f -> f.isFolder()).collect(Collectors.toList())) {
			compressFolder(file.getId(), path + file.getName() + "\\", zos);
		}

	}

}
