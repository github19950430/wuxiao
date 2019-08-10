package com.pldk.admin.system.controller;

import java.util.HashSet;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pldk.admin.system.validator.UserValid;
import com.pldk.common.constant.AdminConst;
import com.pldk.common.enums.ResultEnum;
import com.pldk.common.enums.UnitTypeEnum;
import com.pldk.common.exception.ResultException;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.component.shiro.ShiroUtil;
import com.pldk.modules.system.domain.Role;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.service.RoleService;
import com.pldk.modules.system.service.ThreeUserService;

/**
 * @author Pldk
 * @date 2019/07/01
 */
@Controller
@RequestMapping("/system/threeUser")
public class ThreeUserController {

	private static final Logger logger = LoggerFactory.getLogger(ThreeUserController.class);
    @Autowired
    private ThreeUserService threeUserService;
    @Autowired
    private RoleService roleService;
    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit")
    @RequiresPermissions("system:threeUser:edit")
    public String toEdit(Model model) {
    	User user=ShiroUtil.getSubject();
        model.addAttribute("user", user);
        return "/system/threeUser/add";
    }

  

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/save","/edit"})
    @RequiresPermissions({"system:threeUser:edit"})
    @ResponseBody
    public ResultVo save(@Validated UserValid valid, User threeUser) {
        // 复制保留无需修改的数据
        if (threeUser.getId() != null) {
            User beUser = threeUserService.getById(threeUser.getId());
            EntityBeanUtil.copyProperties(beUser, threeUser);
        }
       /* if(threeUser.getDept().getLevel()!=3) {
        	return ResultVoUtil.error("只能编辑三级用户信息,请核实后再试");
        }*/
     // 验证数据是否合格
        if (threeUser.getId() == null) {

            // 判断密码是否为空
            if (threeUser.getPassword().isEmpty() || "".equals(threeUser.getPassword().trim())) {
                throw new ResultException(ResultEnum.USER_PWD_NULL);
            }

            // 判断两次密码是否一致
            if (!threeUser.getPassword().equals(valid.getConfirm())) {
                throw new ResultException(ResultEnum.USER_INEQUALITY);
            }

            // 对密码进行加密
            String salt = ShiroUtil.getRandomSalt();
            String encrypt = ShiroUtil.encrypt(threeUser.getPassword(), salt);
            threeUser.setPassword(encrypt);
            threeUser.setSalt(salt);
        }
     // 判断用户名是否重复
        if (threeUserService.repeatByUsername(threeUser)) {
            throw new ResultException(ResultEnum.USER_EXIST);
        }

        // 复制保留无需修改的数据
        if (threeUser.getId() != null) {
            // 不允许操作超级管理员数据
            if (threeUser.getId().equals(AdminConst.ADMIN_ID) &&
                    !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
            }

            User beUser = threeUserService.getById(threeUser.getId());
            String[] fields = {"password", "salt", "picture", "roles"};
            EntityBeanUtil.copyProperties(beUser, threeUser, fields);
        }
        // 保存数据
        threeUser.setUnitType(UnitTypeEnum.LABORUN_UNIT_TYPE.getCode());
        // 保存数据
        User userInfo= threeUserService.save(threeUser);
        int size=userInfo.getRoles().size();
        if(userInfo.getId()!=null&&size==0) {
     	   Role role=roleService.getById((long)4);
     	   HashSet<Role> roles=new HashSet<>();
     	   roles.add(role);
     	// 更新用户角色
     	   userInfo.setRoles(roles);
            // 保存数据
     	  threeUserService.save(userInfo);
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

}