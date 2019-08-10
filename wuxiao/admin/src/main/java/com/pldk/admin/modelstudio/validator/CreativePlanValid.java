package com.pldk.admin.modelstudio.validator;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Data
public class CreativePlanValid implements Serializable {
    @NotNull(message = "申报日期不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateofdeclaration;
    @NotEmpty(message = "所属行业不能为空")
    private String industryInvolved;
    @NotEmpty(message = "项目类型不能为空")
    private String projectType;
    @NotEmpty(message = "五小类型不能为空")
    private String fiveSmallType;
    @NotEmpty(message = "项目名称不能为空")
    private String projectName;
    @NotEmpty(message = "完成单位不能为空")
    private String completeUnit;
    @NotEmpty(message = "项目带头人不能为空")
    private String projectLeader;
    @NotNull(message = "项目实施时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date projectTime;
    @NotEmpty(message = "是否获得专利不能为空")
    private String isPatent;
    @NotEmpty(message = "是否所属工作室不能为空")
    private String isStudio;
    @NotEmpty(message = "工作室ID不能为空")
    private String studioId;
    @NotEmpty(message = "专利号不能为空")
    private String patentNumber;
    @NotNull(message = "专利授权公告日不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateofPatent;
    @NotEmpty(message = "项目成果简介不能为空")
    private String achievementBrief;
    @NotEmpty(message = "应用前景不能为空")
    private String applicationProspect;
    @NotNull(message = "预计创造价值不能为空")
    private Integer creationOfValue;
}