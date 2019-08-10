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
import javax.persistence.*;

/**
 * @author Pldk
 * @date 2019/07/10
 */
@Data
@Entity
@Table(name="ts_modelstudio")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.notDelete)
public class Modelstudio implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 所属单位ID  例如太原市总工会  为  1
    private String flat_type;
    // 所属单位
    @Transient//不去数据库创建字段
    private String flat_name;
    // 所属行业
    private String industry_involved;
    // 工作室名称
    private String studioName;
    // 工作室创建时间
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date creation_time;
    // 所在单位
    private String place_the_unit;
    // 工作室类型
    private String studio_type;
    // 专业领域
    private String professional_field;
    // 领衔人ID  带头人ID
    private Long lingxianrenId;
    /*// 领衔人
    private String lingxianren;*/
    // 领衔人最高荣誉
    private String blue_ribbon;
    // 领衔人联系电话
    private String phone;
    // 所属单位（市/产业工会）
    private String unit;
    // 命名日期
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date named_date;
    // 大前年经费投入
    private Double three_years_ago_price;
    // 大前年经费投入来源
    private String three_years_ago_source;
    // 前年经费投入
    private Double two_years_ago_price;
    // 前年经费投入来源
    private String two_years_ago_source;
    // 去年经费投入
    private Double one_years_ago_price;
    // 去年经费投入来源
    private String one_years_ago_source;
    // 成员总数
    private Integer total_number;
    // 创新成果
    private Integer innovative_product;
    // 成果转化
    private Integer achievement_transformation;
    // 获奖等级国际
    private Integer bear_international;
    // 获奖等级国家
    private Integer bear_state;
    // 实用新型
    private Integer utility_model;
    // 发明
    private Integer invent;
    // 省部个
    private Integer provincial;
    // 前年经济收益
    private Double before_economy;
    // 去年经济收益
    private Double lastyear_economy;
    // 合计
    private Double tatal;
    // 培养人才总数
    private Integer cultivate_talents;
    // 双重职称
    private Integer double_title;
    // 高级技师
    private Integer senior_technician;
    // 技师
    private Integer technician;
    // 高级工
    private Integer senior_engineer;
    // 技术等级其他
    private Integer technician_other;
    // 全国劳模
    private Integer national_model;
    // 省部级劳模
    private Integer provincial_model;
    // 地级市劳模
    private Integer prefecture_model;
    // 荣誉其他
    private Integer honor_other;
    // 所在单位工会意见 图片
    private String unit_opinion;
    // 市总工会、省产业工会(工委)意见
    private String always_opinions;
    // 山西省总工会审批意见
    private String general_opinion;
    // 说明原因
    private String accountfor;
    // 0劳模 1职工
    private Byte state;
    // 审核状态 0 市/产业级待上报 1 省级待审核 2 省级审核已通过 3 省级审核未通过 4 省级审核已退回
    private Byte audit_status;
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