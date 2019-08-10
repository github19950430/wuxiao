package com.pldk.modules.resultManager.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.StatusUtil;
import com.pldk.modules.foregoerManager.domain.Foregoer;
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.User;

import lombok.Data;

/**
 * @author Pldk
 * @date 2019/06/29
 */
@Data
@Entity
@Table(name="ts_result"/*, uniqueConstraints = {
	      @UniqueConstraint(columnNames = {"archivesNo"})
	}*/)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.notDelete)
public class Result implements Serializable {
    // 主键ID
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    //档案编号
    private String archivesNo; 
    // 申报日期
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date declareDate;
    // 所属行业
    private String industry;
    // 成果类型
    private String resultType;
    // 五小类型
//    private String wxType;
    // 成果名称
    private String resultName;
    // 完成单位
    private String unit;
    
    // 工会名称
    @Transient
    private String title;
    // 用户名
    @Transient
    private String username;
    
    @Transient
    private Byte state;
    // 联系电话
    @Transient
    private String phone;
    // 实施时间
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date implementDate;
    // 是否获得专利
    private String isPatent;
    // 专利号
    private String patentNum;
    // 专利授权公告日
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date patentAccreditDate;
    // 项目成果简介
    @Lob
    @Column(columnDefinition="TEXT")
    private String resultIntro;
    // 应用前景
    @Lob
    @Column(columnDefinition="TEXT")
    private String prospects;
    // 创造的价值
    private BigDecimal createPrice;
    // 效益计算
    @Lob
    @Column(columnDefinition="TEXT")
    private String benefitClac;
    // 上传图片
    private String uploadResultImg;
    
    //上传专利图片
    private String uploadPatentImg;
    
    //上传带头人荣誉图片
    private String uploadHonorImg;
    
    // 上传视频
    private String uploadVideo;
    // 上传附件
    private String uploadFile;
    // 用户id
    private String userId;
    // 审核状态
    private String auditStatus;
    // 拒绝原因
    private String reason;
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
    // 带头人
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="foregoer_id")
    private Foregoer foregoer;
    
 // 相关成员
    @Transient
    @Column(columnDefinition = "text")
    private String keyMember;
    
    //所属工会
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="dept_id")
    @JsonIgnore
    private Dept dept;
    
    @Transient
    private String auditStatusList;
   //  单位类型: 1 工会 2 团省委 3 省科协
    private int unitType;
    
    private int addLevel;   //添加用户级别： 1：一级 2：二级 3：三级
    // 数据状态
    private Byte status = StatusEnum.OK.getCode();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tr_result_user",
            joinColumns = @JoinColumn(name="result_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private Set<User> users = new HashSet<>(0);
    
    //综合评分
    @Transient
    private double  score;
    //工作室类型(1:职工，劳模工作室  2： 大师工作室)
    private int studioType;
    //工作室ID
    private int studioId;
    //工作室ID+工作室类型
    private String studioIdAndType;
    //工作室负责人(输汉字)
    private String studioCharge;
    
    //工作室带头人（关联工作室表ID）
    private Long studioLeader;
    @Transient
    private String year;//申报年份
}