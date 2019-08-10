package com.pldk.component.sliceUpload.entity;

/**
 * 
 * @author suhp
 *
 */
public class UploadExtraParam {
	
	private Integer sliceCount;
	private Integer sliceIndex;
	private String hash;
	private String ok;
	
	public Integer getSliceCount() {
		return sliceCount;
	}

	public void setSliceCount(Integer sliceCount) {
		this.sliceCount = sliceCount;
	}

	public Integer getSliceIndex() {
		return sliceIndex;
	}

	public void setSliceIndex(Integer sliceIndex) {
		this.sliceIndex = sliceIndex;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}
}
