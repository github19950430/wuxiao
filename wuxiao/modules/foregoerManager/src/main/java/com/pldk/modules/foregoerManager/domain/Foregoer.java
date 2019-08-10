package com.pldk.modules.foregoerManager.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.StatusUtil;
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.User;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Pldk
 * @date 2019/07/01
 */
@Data
@Entity
@Table(name="ts_foregoer")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.notDelete)
public class Foregoer implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 用户id
//    private String userId;
    // 姓名
    private String name;
    // 性别
    private String sex;
    // 出生年月
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    // 身份证号
    private String identityNum;
    // 工作单位
    private String workUnit;
    // 行业
    private String industry;
    // 参加工作时间
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workStartTime;
    // 免冠照片
    private String bareheadedImg;
    // 政治面貌
    private String politicsStatus;
    // 技术等级
    private String skillLevel;
    // 职称
    private String professional;
    // 文化程度
    private String standardCulture;
    // 最高荣誉
    private String honor;
    // 联系电话
    private String contactNum;
    // 个人简介
    @Lob
    @Column(columnDefinition="TEXT")
    private String personalIntro;
    // 主要事迹
    @Lob
    @Column(columnDefinition="TEXT")
    private String mainEvent;
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
    
    //所属工会
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="dept_id")
    @JsonIgnore
    private Dept dept;
    
    //  单位类型: 1 工会 2 团省委 3 省科协
    private int unitType; 
}