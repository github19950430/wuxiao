package com.pldk.modules.review.vo;

import java.io.Serializable;

/**
 * @author Pldk
 * @date 2019/07/12
 */
public class ReviewSetVo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键ID
    private Long id;
    
    private Long pid;
    // 审核项内容
    private String content;
    // 权重最低分
    private Integer wiLowest;
    // 权重最高分
    private Integer wlHighest;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getWiLowest() {
		return wiLowest;
	}
	public void setWiLowest(Integer wiLowest) {
		this.wiLowest = wiLowest;
	}
	public Integer getWlHighest() {
		return wlHighest;
	}
	public void setWlHighest(Integer wlHighest) {
		this.wlHighest = wlHighest;
	}
    
    
}