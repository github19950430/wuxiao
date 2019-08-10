package com.pldk.modules.modelstudio.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.StatusUtil;
import com.pldk.modules.system.domain.User;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author suhp
 * @date 2019/07/16
 */
@SuppressWarnings("serial")
@Data
@Entity
@Table(name="ts_master_studio")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.notDelete)
public class MasterStudio implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 工作室名称
    private String title;
    //姓名
    private String name;
    // 备注
    private String remark;
    // 创建时间
    @CreatedDate
    private Date createDate;
    
    // 骨干成员
    @Transient
    @Column(columnDefinition = "text")
    private String keyMember;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
 
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
    // 性别
    private String sex;
    // 生日
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    //  民族
    private String nation;
    // 政治面貌
    private String politicsStatus;
    // 工种
    private String major;
    // 荣誉称号
    private String honoraryTitle;
    // 联系电话
    private String phone;
    // 电子邮箱
    private String email;
    // 头像
    private String headImg;
    // 何时起从事本行业
    private String firstIndustry;
    // 学历
    private String education;
    // 带头人获奖情况
    private String leaderAwards;
    // 工作室成员获奖情况
    private String studioAwards;
    // 业绩简介
    private String performanceInfo;
    // 上传图片
    private String uploadImg;
    // 上传附件
    private String uploadFile;
    // 上传视频
    private String uploadVideo;
    //  单位意见
    private String unitIdea;
    // 市总意见
    private String cityIdea;
    // 省总意见
    private String provinceIdea;
    //  推荐单位
    private String recommendUnit;
   
}