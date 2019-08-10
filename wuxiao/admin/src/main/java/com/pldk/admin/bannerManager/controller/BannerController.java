package com.pldk.admin.bannerManager.controller;

import com.pldk.admin.bannerManager.validator.BannerValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.bannerManager.domain.Banner;
import com.pldk.modules.bannerManager.service.BannerService;
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
 * @date 2019/06/28
 */
@Controller
@RequestMapping("/bannerManager/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("bannerManager:banner:index")
    public String index(Model model, Banner banner) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains());

        // 获取数据列表
        Example<Banner> example = Example.of(banner, matcher);
        Page<Banner> list = bannerService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/bannerManager/banner/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("bannerManager:banner:add")
    public String toAdd() {
        return "/bannerManager/banner/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("bannerManager:banner:edit")
    public String toEdit(@PathVariable("id") Banner banner, Model model) {
        model.addAttribute("banner", banner);
        return "/bannerManager/banner/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"bannerManager:banner:add","bannerManager:banner:edit"})
    @ResponseBody
    public ResultVo save(@Validated BannerValid valid, Banner banner) {
        // 复制保留无需修改的数据
        if (banner.getId() != null) {
            Banner beBanner = bannerService.getById(banner.getId());
            EntityBeanUtil.copyProperties(beBanner, banner);
        }

        // 保存数据
        bannerService.save(banner);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("bannerManager:banner:detail")
    public String toDetail(@PathVariable("id") Banner banner, Model model) {
        model.addAttribute("banner",banner);
        return "/bannerManager/banner/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("bannerManager:banner:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (bannerService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}