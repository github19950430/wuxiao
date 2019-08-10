package com.pldk.admin.modelstudio.controller;

import com.pldk.admin.modelstudio.validator.StudioProfileValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.modelstudio.domain.StudioProfile;
import com.pldk.modules.modelstudio.service.StudioProfileService;
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
 * @date 2019/07/12
 */
@Controller
@RequestMapping("/modelstudio/studioProfile")
public class StudioProfileController {

    @Autowired
    private StudioProfileService studioProfileService;

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("modelstudio:studioProfile:add")
    public String toAdd() {
        return "/modelstudio/studioProfile/Profile_add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("modelstudio:studioProfile:edit")
    public String toEdit(@PathVariable("id") StudioProfile studioProfile, Model model) {
        model.addAttribute("studioProfile", studioProfile);
        return "/modelstudio/studioProfile/Profile_add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"modelstudio:studioProfile:add","modelstudio:studioProfile:edit"})
    @ResponseBody
    public ResultVo save(@Validated StudioProfileValid valid, StudioProfile studioProfile) {
        // 复制保留无需修改的数据
        if (studioProfile.getId() != null) {
            StudioProfile beStudioProfile = studioProfileService.getById(studioProfile.getId());
            EntityBeanUtil.copyProperties(beStudioProfile, studioProfile);
        }

        // 保存数据
        studioProfileService.save(studioProfile);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("modelstudio:studioProfile:detail")
    public String toDetail(@PathVariable("id") StudioProfile studioProfile, Model model) {
        model.addAttribute("studioProfile",studioProfile);
        return "/modelstudio/studioProfile/Profile_detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("modelstudio:studioProfile:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (studioProfileService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}