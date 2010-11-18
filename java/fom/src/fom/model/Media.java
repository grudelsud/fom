package fom.model;

import org.joda.time.DateTime;

public class Media {
	private String relPath;
	private String fileName;
	private String fileType;
	private String description;
	private DateTime created;
	private DateTime modified;
	
	public Media(String relPath, String fileName, String fileType, String description, DateTime created, DateTime modified) {
		this.relPath = relPath;
		this.fileName = fileName;
		this.fileType = fileType;
		this.description = description;
		this.created = created;
		this.modified = modified;
	}
	
	public String getRelPath() {
		return relPath;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public String getDescription() {
		return description;
	}

	public DateTime getCreated() {
		return created;
	}

	public DateTime getModified() {
		return modified;
	}

}
