package com.pldk.admin.modelstudio.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Data
public class PlanAccessoryValid implements Serializable {
    @NotEmpty(message = "附件地址不能为空")
    private String fileUrl;
    @NotNull(message = "创新计划ID不能为空")
    private Integer creativePlanId;
}