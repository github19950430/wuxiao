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
public class StudioProfileValid implements Serializable {
    @NotNull(message = "工作室ID不能为空")
    private Integer studioId;
    @NotEmpty(message = "工作室简介不能为空")
    private String studioProfile;
    @NotEmpty(message = "带头人简介不能为空")
    private String IntroductionProfile;
    @NotEmpty(message = "团队简介不能为空")
    private String teamProfile;
    @NotEmpty(message = "创新成果描述不能为空")
    private String innovativeDescription;
    @NotEmpty(message = "经济效益描述不能为空")
    private String economicDescription;
    @NotEmpty(message = "创新计划描述不能为空")
    private String creativeplanDescription;
}