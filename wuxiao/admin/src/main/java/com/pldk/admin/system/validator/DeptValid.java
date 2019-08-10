package com.pldk.admin.system.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Pldk
 * @date 2018/12/02
 */
@Data
public class DeptValid implements Serializable {
	@NotEmpty(message = "组织名称不能为空")
	private String title;
    @NotNull(message = "父级组织不能为空")
    private Long pid;
}
