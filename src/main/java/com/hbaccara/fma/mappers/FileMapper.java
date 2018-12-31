package com.hbaccara.fma.mappers;

import org.springframework.stereotype.Service;

import com.hbaccara.fma.dto.FileDto;
import com.hbaccara.fma.entities.File;

@Service
public class FileMapper {

	public FileDto fileToFileDto(File file) {

		FileDto fileDto = new FileDto();

		fileDto.setId(file.getId());
		fileDto.setName(file.getName());
		fileDto.setFolder(file.isFolder());
		fileDto.setSize(file.getSize());
		fileDto.setUpdateDate(file.getUpdateDate() != null ? file.getUpdateDate().getTime() : null);
		fileDto.setOwnerId(file.getOwner() != null ? file.getOwner().getId() : null);
		fileDto.setOwnerName(file.getOwner() != null ? file.getOwner().getUsername() : null);

		return fileDto;
	}

}
