package com.pldk.admin.modelstudio.validator;

import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author suhp
 * @date 2019/07/16
 */
@Data
public class MasterStudioValid implements Serializable {
    @NotEmpty(message = "工作室名称不能为空")
    private String title;
    @NotEmpty(message = "姓名不能为空")
    private String name;
    @NotEmpty(message = "联系电话不能为空")
    private String phone;
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotEmpty(message = "学历不能为空")
    private String education;
    @NotEmpty(message = "民族不能为空")
    private String nation;
}