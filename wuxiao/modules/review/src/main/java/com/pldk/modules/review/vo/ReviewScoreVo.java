package com.pldk.modules.review.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public class ReviewScoreVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
    // 成果ID
    private Long resultId;
    // 专家ID
    private Long userId;
    // 合计
    private Integer summation;
    // 评审意见
    private String reviewComments;
    
    private List<ReviewScoreDetailVo> detailList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSummation() {
		return summation;
	}

	public void setSummation(Integer summation) {
		this.summation = summation;
	}

	public String getReviewComments() {
		return reviewComments;
	}

	public void setReviewComments(String reviewComments) {
		this.reviewComments = reviewComments;
	}

	public List<ReviewScoreDetailVo> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ReviewScoreDetailVo> detailList) {
		this.detailList = detailList;
	}
    
    
    
}