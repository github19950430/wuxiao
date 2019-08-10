package com.pldk.modules.review.vo;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public class ReviewScoreDetailVo {

	 // 主键ID
    private Long id;
    // 评分ID
    private Long scoreId;
    // 评分项一级ID
    private Long typeId;
    // 分值
    private Integer score;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getScoreId() {
		return scoreId;
	}
	public void setScoreId(Long scoreId) {
		this.scoreId = scoreId;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
    
    
    
}
