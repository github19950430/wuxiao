package com.pldk.admin.resultManager.validator;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import com.pldk.modules.foregoerManager.domain.Foregoer;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.JoinColumn;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Pldk
 * @date 2019/06/29
 */
@Data
public class ResultValid implements Serializable {
//    @NotNull(message = "主键ID不能为空")
//    private Long id;
    @NotNull(message = "申报日期不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date declareDate;
    @NotEmpty(message = "所属行业不能为空")
    private String industry;
    @NotEmpty(message = "成果类型不能为空")
    private String resultType;
//    @NotEmpty(message = "五小类型不能为空")
//    private String wxType;
    @NotEmpty(message = "成果名称不能为空")
    private String resultName;
    @NotEmpty(message = "完成单位不能为空")
    private String unit;
    @NotNull(message = "带头人不能为空")
    private Foregoer foregoer;
    @NotNull(message = "实施时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date implementDate;
    @NotEmpty(message = "是否获得专利不能为空")
    private String isPatent;
    @NotNull(message = "项目成果简介不能为空")
    private String resultIntro;
    @NotNull(message = "应用前景不能为空")
    private String prospects;
   /* @NotNull(message = "项目负责人不能为空")
    private String studioCharge;*/
    @Digits(integer=19, fraction=2)
    @NotNull(message = "创造价值不能为空")
    private String createPrice;
}