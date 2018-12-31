package com.hbaccara.fma.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationDto {

	private Long id;

	private String message;
	
	private String type;

	@JsonProperty("isRead")
	private boolean read;
	
	private Long date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMesssage(String content) {
		this.message = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean isRead) {
		this.read = isRead;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
