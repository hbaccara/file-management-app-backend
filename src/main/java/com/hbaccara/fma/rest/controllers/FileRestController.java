package com.hbaccara.fma.rest.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbaccara.fma.dto.FileDto;
import com.hbaccara.fma.entities.User;
import com.hbaccara.fma.rest.response.ListDirectoryResponse;
import com.hbaccara.fma.rest.services.FileRestService;

@RestController
public class FileRestController {

	@Autowired
	private FileRestService fileRestService;

	@PostMapping("/upload")
	public ResponseEntity<FileDto> handleFileUpload(@AuthenticationPrincipal User user,
			@RequestParam(value = "parentId") Long parentId, @RequestParam("file") MultipartFile multipartFile) {

		try {
			FileDto fileDto = fileRestService.handleFileUpload(user, parentId, multipartFile);

			return ResponseEntity.status(HttpStatus.OK).body(fileDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/directory/download/{id}")
	@ResponseBody
	public ResponseEntity<Resource> download(@PathVariable Long id) {

		try {
			Object[] fileDownloadData = fileRestService.download(id);

			String downloadFilename = (String) fileDownloadData[0];
			byte[] fileContent = (byte[]) fileDownloadData[1];

			Resource resource = new ByteArrayResource(fileContent);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFilename + "\"")
					.body(resource);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/directory/{directoryId}")
	public ResponseEntity<ListDirectoryResponse> listDirectory(@AuthenticationPrincipal User user,
			@PathVariable(value = "directoryId") Long directoryId) {

		try {

			ListDirectoryResponse response = fileRestService.listDirectory(directoryId, user);

			return ResponseEntity.ok().body(response);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/files/search")
	public ResponseEntity<List<FileDto>> searchFiles(@AuthenticationPrincipal User user,
			@RequestParam(required = true, value = "searchTerm") String searchTerm) {

		try {

			List<FileDto> response = fileRestService.searchFiles(searchTerm, user);

			return ResponseEntity.ok().body(response);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/files/shared-with-user")
	public ResponseEntity<List<FileDto>> getFilesSharedWithUser(@AuthenticationPrincipal User user) {

		try {

			List<FileDto> response = fileRestService.findFilesSharedWithUser(user);

			return ResponseEntity.ok().body(response);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/folder")
	public ResponseEntity<FileDto> createFolder(@AuthenticationPrincipal User user,
			@RequestParam(value = "parentId") Long parentId,
			@RequestParam(value = "folderName", defaultValue = "New folder") String name) {

		try {

			FileDto fileDto = fileRestService.createFolder(user, parentId, name);

			return ResponseEntity.status(HttpStatus.OK).body(fileDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	@DeleteMapping("directory/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {

		try {
			fileRestService.delete(id);

			return ResponseEntity.status(HttpStatus.OK).body(null);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/file/rename")
	public ResponseEntity<FileDto> rename(@RequestParam(value = "id") Long id,
			@RequestParam(value = "newName") String newName) {

		try {
			FileDto fileDto = fileRestService.rename(id, newName);

			return ResponseEntity.status(HttpStatus.OK).body(fileDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/file/move")
	public ResponseEntity<FileDto> move(@RequestParam(value = "id") Long id,
			@RequestParam(value = "newParentId") Long newParentId) {

		try {
			FileDto fileDto = fileRestService.move(id, newParentId);

			return ResponseEntity.status(HttpStatus.OK).body(fileDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
