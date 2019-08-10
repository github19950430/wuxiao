package com.pldk.admin.modelstudio.controller;

import com.pldk.admin.modelstudio.validator. InnovationBaseValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.modelstudio.domain. InnovationBase;
import com.pldk.modules.modelstudio.service. InnovationBaseService;
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.Upload;
import com.pldk.modules.system.service.DeptService;
import com.pldk.modules.system.service.UploadService;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2019/07/15
 */
@Controller
@RequestMapping("/modelstudio/innovationBase")
public class InnovationBaseController {

    @Autowired
    private InnovationBaseService innovationBaseService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UploadService uploadService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("modelstudio:innovationBase:index")
    public String index(Model model,  InnovationBase  innovationBase) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("detptId", match -> match.contains());

        // 获取数据列表
        Example<InnovationBase> example = Example.of( innovationBase, matcher);
        Page<InnovationBase> list = innovationBaseService.getPageList(example);
        list.getContent().forEach(inno->{
            if(StringUtils.isNotEmpty(inno.getDeptId())){
                Dept dept =deptService.getById(Long.parseLong(inno.getDeptId()));
                inno.setDeptName(dept.getTitle());
            }
        });

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/modelstudio/innovationBase/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("modelstudio:innovationBase:add")
    public String toAdd() {
        return "/modelstudio/innovationBase/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("modelstudio:innovationBase:edit")
    public String toEdit(@PathVariable("id")  InnovationBase  innoviationBase, Model model) {
        if(StringUtils.isNotEmpty(innoviationBase.getDeptId())){
            Dept dept =deptService.getById(Long.parseLong(innoviationBase.getDeptId()));
            innoviationBase.setDeptName(dept.getTitle());
        }
        List<Upload> resultImg=uploadService.getListUpload(innoviationBase.getPicPath());
        List<Upload> resultFile=uploadService.getListUpload(innoviationBase.getAnnexPath());
        List<Upload> resultVideo=uploadService.getListUpload(innoviationBase.getVideoPath());
        // 封装数据
        model.addAttribute("resultImg", resultImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
        model.addAttribute("innovationBase",  innoviationBase);
        return "/modelstudio/innovationBase/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"modelstudio:innovationBase:add","modelstudio:innovationBase:edit"})
    @ResponseBody
    public ResultVo save(@Validated  InnovationBaseValid valid,  InnovationBase  innovationBase) {
        // 复制保留无需修改的数据
        if ( innovationBase.getId() != null) {
            InnovationBase beInnovationBase = innovationBaseService.getById( innovationBase.getId());
            EntityBeanUtil.copyProperties(beInnovationBase,  innovationBase);
        }

        // 保存数据
        innovationBaseService.save(innovationBase);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("modelstudio:innovationBase:detail")
    public String toDetail(@PathVariable("id") InnovationBase  innovationBase, Model model) {
        model.addAttribute("innovationBase",innovationBase );
        List<Upload> resultImg=uploadService.getListUpload(innovationBase.getPicPath());
        List<Upload> resultFile=uploadService.getListUpload(innovationBase.getAnnexPath());
        List<Upload> resultVideo=uploadService.getListUpload(innovationBase.getVideoPath());
        model.addAttribute("resultImg", resultImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
        return "/modelstudio/innovationBase/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("modelstudio:innovationBase:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if ( innovationBaseService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}