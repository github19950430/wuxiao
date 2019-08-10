package com.pldk.admin.modelstudio.controller;

import com.pldk.admin.modelstudio.validator.StudioImgValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.modelstudio.domain.StudioImg;
import com.pldk.modules.modelstudio.service.StudioImgService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
@Controller
@RequestMapping("/modelstudio/studioImg")
public class StudioImgController {

    @Autowired
    private StudioImgService studioImgService;

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("modelstudio:studioImg:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (studioImgService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}