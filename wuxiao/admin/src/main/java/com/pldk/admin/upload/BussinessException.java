package com.pldk.admin.upload;


/**
 * 业务异常的封装
 * 
 * @date 2016年11月12日 下午5:05:10
 */
@SuppressWarnings("serial")
public class BussinessException extends RuntimeException{

	/**
	 * 返回码
	 */
	private int code;
	
	/**
	 * 返回说明
	 */
	private String msg;

	
	public BussinessException(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public BussinessException(BizExceptionEnum bizExceptionEnum) {
		this.code = bizExceptionEnum.getCode();
		this.msg = bizExceptionEnum.getMsg();	
	}

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

	public BussinessException(String msg) {
		this.code = ResultStatusCodeConstant.BUSSINESS_EXCEPTION;
		this.msg = msg;
	}
	
	


}
