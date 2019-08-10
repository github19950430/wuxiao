package com.pldk.admin.newsManager.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Pldk
 * @date 2019/06/27
 */
@Data
public class NewsValid implements Serializable {
//    @NotNull(message = "主键ID不能为空")
//    private Long id;
    @NotEmpty(message = "标题不能为空")
    private String title;
}