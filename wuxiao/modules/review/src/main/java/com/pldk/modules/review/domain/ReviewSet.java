package com.pldk.modules.review.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.StatusUtil;
import com.pldk.modules.system.domain.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Data
@Entity
@Table(name="tr_review_set")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update tr_review_set" + StatusUtil.sliceDelete)
@Where(clause = StatusUtil.notDelete)
public class ReviewSet implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private String nid; //英文标识
    
    private Long pid;
    // 审核项内容
    private String content;
    // 权重最低分
    private Integer wiLowest;
    // 权重最高分
    private Integer wlHighest;
    // 备注
    private String remark;
    // 创建时间
    @CreatedDate
    private Date createDate;
    // 更新时间
    @LastModifiedDate
    private Date updateDate;
    // 创建者
    @CreatedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="create_by")
    @JsonIgnore
    private User createBy;
    // 更新者
    @LastModifiedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="update_by")
    @JsonIgnore
    private User updateBy;
    // 数据状态
    private Byte status = StatusEnum.OK.getCode();
    //关联审核项
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id",foreignKey = @ForeignKey(name = "type_id"))
    private ReviewType type;
    
    
}