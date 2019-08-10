package com.pldk.component.sliceUpload.entity;

import java.util.Date;

/**
 * 
 * @author suhp
 *
 */
public class FileInfo {
	  
	private Long id;
	private String hash;
	private String name;
	private Long size=0L;
	private String path;
	private String ext;
	private String mime;
	private Integer status;
	private String thumbPath;
    private Date mkAt=new Date();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getMime() {
		return mime;
	}
	public void setMime(String mime) {
		this.mime = mime;
	}
	public Date getMkAt() {
		return mkAt;
	}
	public void setMkAt(Date mkAt) {
		this.mkAt = mkAt;
	}
	
	public String getThumbPath() {
		return thumbPath;
	}
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
	@Override
	public String toString() {
		return "FileInfo [hash=" + hash + ", name=" + name + ", size=" + size + ", path=" + path + ", mkAt=" + mkAt + ", thumbPath="+thumbPath+"]";
	}
	    
}
