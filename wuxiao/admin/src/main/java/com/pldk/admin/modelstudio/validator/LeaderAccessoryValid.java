package com.pldk.admin.modelstudio.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author Pldk
 * @date 2019/07/17
 */
@Data
public class LeaderAccessoryValid implements Serializable {
    @NotEmpty(message = "附件地址不能为空")
    private String accessory_url;
    @NotEmpty(message = "附件原名不能为空")
    private String accessory_name;
   /* @NotEmpty(message = "带头人ID不能为空")
    private Long leader_id;*/
}