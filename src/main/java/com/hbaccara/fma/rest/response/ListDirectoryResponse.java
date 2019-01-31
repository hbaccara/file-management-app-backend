package com.hbaccara.fma.rest.response;

import java.util.List;

import com.hbaccara.fma.dto.FileDto;

public class ListDirectoryResponse {

	List<FileDto> files;
	
	List<FileDto> hierarchy;

	public List<FileDto> getFiles() {
		return files;
	}

	public void setFiles(List<FileDto> files) {
		this.files = files;
	}

	public List<FileDto> getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(List<FileDto> hierarchy) {
		this.hierarchy = hierarchy;
	}

}
