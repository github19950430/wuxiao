package com.pldk.admin.modelstudio.validator;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Pldk
 * @date 2019/07/11
 */
@Data
public class LeaderinformationValid implements Serializable {
    @NotEmpty(message = "姓名不能为空")
    private String name;
    @NotNull(message = "性别不能为空")
    private Byte sex;
    @NotNull(message = "出生年月不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateofbirth;
    @NotEmpty(message = "学历不能为空")
    private String education;
    @NotEmpty(message = "民族不能为空")
    private String nation;
    @NotEmpty(message = "政治面貌不能为空")
    private String politics_status;
    @NotEmpty(message = "专业/工种不能为空")
    private String major;
    @NotEmpty(message = "技术职称不能为空")
    private String technical_titles;
    @NotEmpty(message = "何年获何种省部级（含）以上劳模荣誉称号*不能为空")
    private String honorary_title;
    @NotEmpty(message = "联系电话不能为空")
    private String phone;
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotEmpty(message = "推荐单位不能为空")
    private String unit;
    @NotEmpty(message = "业绩简介不能为空")
    private String performance;
}