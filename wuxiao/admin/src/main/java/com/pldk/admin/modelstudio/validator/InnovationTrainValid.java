package com.pldk.admin.modelstudio.validator;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Pldk
 * @date 2019/07/17
 */
@Data
public class InnovationTrainValid implements Serializable {
    @NotEmpty(message = "培训名称不能为空")
    private String trainName;
    @NotEmpty(message = "主图不能为空")
    private String trainImg;
    @NotNull(message = "培训性质不能为空")
    private Byte trainNature;
    @NotNull(message = "技能类型不能为空")
    private Byte skillType;
    @NotNull(message = "培训开始时间不能为空")
    private String trainBdate;
    @NotNull(message = "培训结束时间不能为空")
    private String trainEdate;
    @NotEmpty(message = "培训地点不能为空")
    private String trainAddr;
    @NotNull(message = "培训类型不能为空")
    private Byte trainType;
    @NotEmpty(message = "培训简介不能为空")
    private String trainDesc;
    @NotEmpty(message = "联系人不能为空")
    private String trainContcat;
    @NotEmpty(message = "联系电话不能为空")
    @Pattern(regexp = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message = "手机号码格式不正确")
    private String trainPhone;
}