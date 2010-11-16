package fom.model;

import org.joda.time.DateTime;

public class Media {
	private String relPath;
	private String fileName;
	private String fileType;
	private String description;
	private DateTime created;
	
	public Media(String relPath, String fileName, String fileType, String description, DateTime created) {
		this.relPath = relPath;
		this.fileName = fileName;
		this.fileType = fileType;
		this.description = description;
		this.created = created;
	}
	
	public String getRelPath() {
		return relPath;
	}
	public void setRelPath(String relPath) {
		this.relPath = relPath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public DateTime getCreated() {
		return created;
	}
	public void setCreated(DateTime created) {
		this.created = created;
	}

}
