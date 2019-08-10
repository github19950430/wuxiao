package com.pldk.admin.modelstudio.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Data
public class PlanImgValid implements Serializable {
    @NotEmpty(message = "图片地址不能为空")
    private String imgUrl;
    @NotEmpty(message = "创新计划ID不能为空")
    private String creativePlanId;
}