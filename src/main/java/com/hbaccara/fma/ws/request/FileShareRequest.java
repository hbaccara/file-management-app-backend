package com.hbaccara.fma.ws.request;

public class FileShareRequest {

	 private Long userId;
	 
     private Long fileId;
     
     private Long userToShareWithId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getUserToShareWithId() {
		return userToShareWithId;
	}

	public void setUserToShareWithId(Long userToShareWithId) {
		this.userToShareWithId = userToShareWithId;
	}

}
