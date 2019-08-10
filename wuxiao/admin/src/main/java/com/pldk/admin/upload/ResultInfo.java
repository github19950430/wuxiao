package com.pldk.admin.upload;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 
 * @author liangcm
 *
 */
@Component
public class ResultInfo implements Serializable {
    /** 
     *  
     */
    private static final long serialVersionUID = 1L;
    /**
     * 返回状态码
     */
    protected int code;
    
    /**
     * 返回信息
     */
    protected String msg;
    
    /**
     * 当前时间
     */
    private String timeStamp;
    
    /**
     * 随机字符串
     */
    private String nonceStr;
    
    /**
     * 返回数据包
     */
    private Object data;
    
    /**
     * 签名
     */
    private String sign;
    
    
    public ResultInfo() {
    	this.code = 0;
    	this.msg = "success";

    }
    
    
    public ResultInfo(Object data) {
    	this.code = 0;
    	this.msg = "success";
    	this.data = data;
    }

    
    public static ResultInfo success(Object data) {
    	return new ResultInfo(data);
    }
    
    public ResultInfo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	


}
