package com.pldk.modules.modelstudio.domain;

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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
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
 * @author Pldk
 * @date 2019/07/12
 */
@Data
@Entity
@Table(name="ts_creative_plan")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.notDelete)
public class CreativePlan implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 申报日期
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateofdeclaration;
    // 所属行业
    private String industryInvolved;
    // 项目类型
    private String projectType;
    // 五小类型
    private String fiveSmallType;
    // 项目名称
    private String projectName;
    // 完成单位
    private String completeUnit;
    // 项目带头人
    private String projectLeader;
    // 项目实施时间
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date projectTime;
    // 是否获得专利
    private String isPatent;
    // 是否所属工作室
    private String isStudio;
    // 工作室ID
    private String studioId;
    // 专利号
    private String patentNumber;
    // 专利授权公告日
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateofPatent;
    // 项目成果简介
    private String achievementBrief;
    // 应用前景
    private String applicationProspect;
    // 预计创造价值
    private Integer creationOfValue;
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
}