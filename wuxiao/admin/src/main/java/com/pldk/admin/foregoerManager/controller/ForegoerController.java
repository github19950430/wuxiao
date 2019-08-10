package com.pldk.admin.foregoerManager.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pldk.admin.foregoerManager.validator.ForegoerValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.component.shiro.ShiroUtil;
import com.pldk.modules.foregoerManager.domain.Foregoer;
import com.pldk.modules.foregoerManager.service.ForegoerService;
import com.pldk.modules.system.domain.User;

/**
 * @author Pldk
 * @date 2019/07/01
 */
@Controller
@RequestMapping("/foregoerManager/foregoer")
public class ForegoerController {

    @Autowired
    private ForegoerService foregoerService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("foregoerManager:foregoer:index")
    public String index(Model model, Foregoer foregoer) {
    	User user=ShiroUtil.getSubject();
    	if(foregoer.getDept()==null) {
    		//默认单位类型：工会
    	    foregoer.setDept(user.getDept());
    	}/*else {
    		if(user.getDept().getLevel()==2) {
    		   if(user.getDept().getId()!=foregoer.getDept().getId()) {
    			   foregoer.setDept(user.getDept());
    		   }
    		}
    	}*/
    	if(user.getUnitType()==2||user.getUnitType()==3) {
    		//团省委，省科协
    		foregoer.setDept(null);
    		foregoer.setUnitType(user.getUnitType());
    	}
    	if("admin".equals(user.getUsername())||"shengzong".equals(user.getUsername())) {
    		foregoer.setDept(null);
    	}
    	foregoer.setUnitType(user.getUnitType());
        // 创建匹配器，进行动态查询匹配
        /*ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains())
                .withMatcher("identityNum", match -> match.contains());

        // 获取数据列表
        Example<Foregoer> example = Example.of(foregoer, matcher);
        Page<Foregoer> list = foregoerService.getPageList(example);*/
    	Page<Foregoer> list = foregoerService.getPageList(foregoer);
        // 封装数据
    	model.addAttribute("user", user);
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/foregoerManager/foregoer/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("foregoerManager:foregoer:add")
    public String toAdd(Model model) {
    	User user=ShiroUtil.getSubject();
    	model.addAttribute("user", user);
        return "/foregoerManager/foregoer/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("foregoerManager:foregoer:edit")
    public String toEdit(@PathVariable("id") Foregoer foregoer, Model model) {
        model.addAttribute("foregoer", foregoer);
        return "/foregoerManager/foregoer/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"foregoerManager:foregoer:add","foregoerManager:foregoer:edit"})
    @ResponseBody
    public ResultVo save(@Validated ForegoerValid valid, Foregoer foregoer) {
    	User user = ShiroUtil.getSubject();
        // 复制保留无需修改的数据
        if (foregoer.getId() != null) {
            Foregoer beForegoer = foregoerService.getById(foregoer.getId());
            EntityBeanUtil.copyProperties(beForegoer, foregoer);
        }
        
        if(user.getDept().getLevel()!=2) {
           foregoer.setDept(user.getDept());//登陆用户所属单位id
        }
        foregoer.setUnitType(user.getUnitType()); //登录用户单位类型
        
        // 保存数据
        foregoerService.save(foregoer);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("foregoerManager:foregoer:detail")
    public String toDetail(@PathVariable("id") Foregoer foregoer, Model model) {
        model.addAttribute("foregoer",foregoer);
        return "/foregoerManager/foregoer/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("foregoerManager:foregoer:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (foregoerService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}