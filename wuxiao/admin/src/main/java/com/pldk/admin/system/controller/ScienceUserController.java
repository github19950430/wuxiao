package com.pldk.admin.system.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pldk.common.enums.UnitTypeEnum;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.service.UserService;
/**
 * @author Pldk
 * @date 2019/07/01
 */
@Controller
@RequestMapping("/system/scienceUser")
public class ScienceUserController {

	 @Autowired
	    private UserService userService;

	    /**
	     * 列表页面
	     */
	    @GetMapping("/index")
	    @RequiresPermissions("system:scienceUser:index")
	    public String index(Model model, User scienceUser) {

	    	scienceUser.setUnitType(UnitTypeEnum.SCIENCE_UNIT_TYPE.getCode());
	        // 获取数据列表	
	        Page<User> list = userService.getPageList(scienceUser);
	        // 封装数据
	        model.addAttribute("list", list.getContent());
	        model.addAttribute("page", list);
	        return "/system/scienceUser/index";
	    }	
	 
	    /**
	     * 跳转到详细页面
	     */
	    @GetMapping("/detail/{id}")
	    @RequiresPermissions("system:scienceUser:detail")
	    public String toDetail(@PathVariable("id") User user, Model model) {
	        model.addAttribute("user",user);
	        return "/system/scienceUser/detail";
	    }
}
