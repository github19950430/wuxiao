package com.pldk.admin.system.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/system/groupUser")
public class GroupUserController {

	private static final Logger logger = LoggerFactory.getLogger(GroupUserController.class);
    @Autowired
    private UserService userService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:groupUser:index")
    public String index(Model model, User groupUser) {

    	groupUser.setUnitType(UnitTypeEnum.GROUP_UNIT_TYPE.getCode());
        // 获取数据列表	
        Page<User> list = userService.getPageList(groupUser);
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/groupUser/index";
    }	
 
    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:groupUser:detail")
    public String toDetail(@PathVariable("id") User user, Model model) {
        model.addAttribute("user",user);
        return "/system/groupUser/detail";
    }

}