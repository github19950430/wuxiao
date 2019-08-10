package com.pldk.admin.modelstudio.validator;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotEmpty;

/**
 * @author Pldk
 * @date 2019/07/18
 */
@Data
public class KeymemberValid implements Serializable {
    @NotEmpty(message = "姓名不能为空")
    private String keyname;
    @NotEmpty(message = "性别不能为空")
    private String keysex;
    @NotEmpty(message = "出生年份不能为空")
    private Date keyyear;
    @NotEmpty(message = "学历不能为空")
    private String keyeducation;
    @NotEmpty(message = "技术职称不能为空")
    private String keytechnical;
    @NotEmpty(message = "所在部门不能为空")
    private String keydepartment;
    @NotEmpty(message = "主要分工不能为空")
    private String keydivision;
}