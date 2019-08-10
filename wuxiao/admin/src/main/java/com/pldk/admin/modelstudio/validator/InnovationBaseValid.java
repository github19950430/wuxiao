package com.pldk.admin.modelstudio.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author Pldk
 * @date 2019/07/15
 */
@Data
public class  InnovationBaseValid implements Serializable {
    @NotEmpty(message = "公会ID不能为空")
    private String deptId;
    @NotEmpty(message = "所在单位不能为空")
    private String company;
    @NotEmpty(message = "基地名称不能为空")
    private String baseName;
    @NotEmpty(message = "基地创建时间不能为空")
    private String baseDate;
    @NotEmpty(message = "基础简介不能为空")
    private String baseDesc;
    @NotEmpty(message = "联系人不能为空")
    private String baseContcat;
    @NotEmpty(message = "联系电话不能为空")
    private String basePhone;
}