package com.pldk.modules.review.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.StatusUtil;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name="tr_user_review_set")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.notDelete)
public class UserReviewSet {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long userId; 
	private String realname;
	private String resultName;
	private String reviewSetJsonStr;
	private String message;
	private Long resultId;
	private double score;
	// 数据状态
    private Byte status = StatusEnum.OK.getCode();
}
