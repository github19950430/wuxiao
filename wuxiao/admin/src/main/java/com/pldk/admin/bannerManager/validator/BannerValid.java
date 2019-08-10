package com.pldk.admin.bannerManager.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Pldk
 * @date 2019/06/28
 */
@Data
public class BannerValid implements Serializable {
//    @NotNull(message = "主键ID不能为空")
//    private Long id;
    @NotEmpty(message = "类型不能为空")
    private String type;
    @NotEmpty(message = "名称不能为空")
    private String name;
}