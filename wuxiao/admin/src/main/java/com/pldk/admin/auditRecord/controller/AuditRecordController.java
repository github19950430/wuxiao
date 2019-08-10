package com.pldk.admin.auditRecord.controller;

import com.pldk.admin.auditRecord.validator.AuditRecordValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.auditRecord.domain.AuditRecord;
import com.pldk.modules.auditRecord.service.AuditRecordService;
import com.pldk.modules.resultManager.domain.Result;

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
@RequestMapping("/auditRecord/auditRecord")
public class AuditRecordController {

    @Autowired
    private AuditRecordService auditRecordService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("auditRecord:auditRecord:index")
    public String index(Model model, AuditRecord auditRecord) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<AuditRecord> example = Example.of(auditRecord, matcher);
        Page<AuditRecord> list = auditRecordService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/auditRecord/auditRecord/index";
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("auditRecord:auditRecord:detail")
    public String toDetail(@PathVariable("id") AuditRecord auditRecord, Model model) {
        model.addAttribute("auditRecord",auditRecord);
        return "/auditRecord/auditRecord/detail";
    }
    
    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("auditRecord:auditRecord:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (auditRecordService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}