package com.hbaccara.fma.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileShareNotificationDto extends NotificationDto{

	private Long fileId;
	
	@JsonProperty("isFolder")
	private boolean isFolder;
	
	private String filename;
	
	private String sharer;

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSharer() {
		return sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}	
}
