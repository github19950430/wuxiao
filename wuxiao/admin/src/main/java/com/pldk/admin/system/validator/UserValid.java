package com.pldk.admin.system.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pldk.modules.system.domain.Dept;

import java.io.Serializable;

/**
 * @author Pldk
 * @date 2018/8/14
 */
@Data
public class UserValid implements Serializable {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "真实姓名不能为空")
    @Size(min = 2, message = "真实姓名：请输入至少2个字符")
    private String realname;
    private String confirm;
    @NotNull(message = "所在组织不能为空")
    private Dept dept;
}
