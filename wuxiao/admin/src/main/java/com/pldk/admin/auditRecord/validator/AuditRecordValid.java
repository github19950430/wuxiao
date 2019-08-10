package com.pldk.admin.auditRecord.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author Pldk
 * @date 2019/07/15
 */
@Data
public class AuditRecordValid implements Serializable {
    @NotEmpty(message = "审核类型不能为空")
    private String type;
    @NotEmpty(message = "审核状态不能为空")
    private String auditStatus;
}