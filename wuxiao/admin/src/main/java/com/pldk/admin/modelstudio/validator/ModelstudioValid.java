package com.pldk.admin.modelstudio.validator;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Pldk
 * @date 2019/07/10
 */
@Data
public class ModelstudioValid implements Serializable {
    /*@NotEmpty(message = "单位类型不能为空")
    private String flat_type;*/
    @NotEmpty(message = "所属行业不能为空")
    private String industry_involved;
    @NotEmpty(message = "工作室名称不能为空")
    private String studioName;
    @NotNull(message = "工作室创建时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date creation_time;
    @NotEmpty(message = "所在单位不能为空")
    private String place_the_unit;
    @NotEmpty(message = "工作室类型不能为空")
    private String studio_type;
    @NotEmpty(message = "专业领域不能为空")
    private String professional_field;
    @NotEmpty(message = "领衔人不能为空")
    private String lingxianrenId;
    /*@NotEmpty(message = "领衔人不能为空")
    private String lingxianren;*/
    @NotEmpty(message = "领衔人最高荣誉不能为空")
    private String blue_ribbon;
    @NotNull(message = "领衔人联系电话不能为空")
    private String phone;
    /*@NotEmpty(message = "所属单位（市/产业工会）不能为空")
    private String unit;*/
    @NotNull(message = "命名日期不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date named_date;
    @NotNull(message = "成员总数不能为空")
    private Integer total_number;
    @NotNull(message = "创新成果不能为空")
    private Integer innovative_product;
    @NotNull(message = "成果转化不能为空")
    private Integer achievement_transformation;
    @NotNull(message = "获奖等级国际不能为空")
    private Integer bear_international;
    @NotNull(message = "获奖等级国家不能为空")
    private Integer bear_state;
    @NotNull(message = "实用新型不能为空")
    private Integer utility_model;
    @NotNull(message = "发明不能为空")
    private Integer invent;
    @NotNull(message = "省部个不能为空")
    private Integer provincial;
    @NotNull(message = "前年经济收益不能为空")
    private Double before_economy;
    @NotNull(message = "去年经济收益不能为空")
    private Double lastyear_economy;
    @NotNull(message = "合计不能为空")
    private Double tatal;
    @NotNull(message = "培养人才总数不能为空")
    private Integer cultivate_talents;
    @NotNull(message = "双重职称不能为空")
    private Integer double_title;
    @NotNull(message = "高级技师不能为空")
    private Integer senior_technician;
    @NotNull(message = "技师不能为空")
    private Integer technician;
    @NotNull(message = "高级工不能为空")
    private Integer senior_engineer;
    @NotNull(message = "技术等级其他不能为空")
    private Integer technician_other;
    @NotNull(message = "全国劳模不能为空")
    private Integer national_model;
    @NotNull(message = "省部级劳模不能为空")
    private Integer provincial_model;
    @NotNull(message = "地级市劳模不能为空")
    private Integer prefecture_model;
    @NotNull(message = "荣誉其他不能为空")
    private Integer honor_other;
    /*@NotNull(message = "0劳模 1职工不能为空")
    private Byte state;*/
    /*@NotNull(message = "审核状态不能为空")
    private Byte audit_status;*/
    /*@NotNull(message = "创建时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;*/
    /*@NotNull(message = "更新时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateDate;*/
    /*@NotNull(message = "创建者不能为空")
    private Object createBy;*/
    /*@NotNull(message = "更新者不能为空")
    private Object updateBy;*/
   /* @NotNull(message = "数据状态不能为空")
    private Byte status;*/
}