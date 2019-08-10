package com.pldk.admin.modelstudio.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Pldk
 * @date 2019/07/11
 */
@Data
public class StudioImgValid implements Serializable {
    @NotEmpty(message = "图片地址不能为空")
    private String img_url;
    @NotNull(message = "工作室ID不能为空")
    private Integer studio_id;
}