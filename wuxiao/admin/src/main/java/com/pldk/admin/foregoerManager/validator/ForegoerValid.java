package com.pldk.admin.foregoerManager.validator;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Pldk
 * @date 2019/07/01
 */
@Data
public class ForegoerValid implements Serializable {
    @NotEmpty(message = "姓名不能为空")
    private String name;
    @NotEmpty(message = "性别不能为空")
    private String sex;
    @NotNull(message = "出生年月不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    @Size(min = 18, max = 18)
    @NotEmpty(message = "身份证号不能为空")
    private String identityNum;
    @NotEmpty(message = "工作单位不能为空")
    private String workUnit;
    @NotEmpty(message = "行业不能为空")
    private String industry;
    @NotNull(message = "参加工作时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date workStartTime;
    @NotEmpty(message = "免冠照片不能为空")
    private String bareheadedImg;
    @NotEmpty(message = "政治面貌不能为空")
    private String politicsStatus;
    @NotEmpty(message = "技术等级不能为空")
    private String skillLevel;
    @NotEmpty(message = "职称不能为空")
    private String professional;
    @NotEmpty(message = "文化程度不能为空")
    private String standardCulture;
    @NotEmpty(message = "联系电话不能为空")
    private String contactNum;
}