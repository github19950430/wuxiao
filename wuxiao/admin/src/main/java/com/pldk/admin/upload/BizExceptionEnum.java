package com.pldk.admin.upload;

/**
 * 所有业务异常的枚举
 * 
 * @date 2016年11月12日 下午5:04:51
 */
public enum BizExceptionEnum {
	
	/**
	 * 文件上传
	 */
	UPLOAD_ERROR(500,"上传图片出错"),
	
	/**
	 * 文件下载
	 */
	OVER_SIZE_FILE(510,"文件大小超出范围，无法进行相关操作！"),
    FILE_READING_ERROR(511,"文件读取失败，无法进行相关操作！"),
    FILE_NOT_FOUND(512,"文件未找到，无法进行相关操作！"),

	;
	
	
	BizExceptionEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	BizExceptionEnum(int code, String msg,String urlPath) {
		this.code = code;
		this.msg = msg;
		this.urlPath = urlPath;
	}

	private int code;

	private String msg;
	
	private String urlPath;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

}
