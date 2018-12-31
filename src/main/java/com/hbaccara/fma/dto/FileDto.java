package com.hbaccara.fma.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileDto {
	
	private Long id;
    
    private String name;
    
    private Integer size;
    
    @JsonProperty("isFolder")
    private boolean isFolder;
    
    private Long updateDate;
    
    private Long ownerId;
    
    private String ownerName;
    
    public FileDto() {}
    
    public FileDto(String name) {
    	this.name = name;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}
}


