package com.pldk.admin.modelstudio.controller;

import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.modelstudio.service.PlanAccessoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Controller
@RequestMapping("/modelstudio/planAccessory")
public class PlanAccessoryController {

    @Autowired
    private PlanAccessoryService planAccessoryService;

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("modelstudio:planAccessory:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (planAccessoryService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}