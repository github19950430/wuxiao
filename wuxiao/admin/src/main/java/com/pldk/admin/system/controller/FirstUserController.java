package com.pldk.admin.system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pldk.admin.system.validator.UserValid;
import com.pldk.common.constant.AdminConst;
import com.pldk.common.enums.ResultEnum;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.enums.UnitTypeEnum;
import com.pldk.common.exception.ResultException;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.SpringContextUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.component.actionLog.action.UserAction;
import com.pldk.component.actionLog.annotation.ActionLog;
import com.pldk.component.excel.ExcelUtil;
import com.pldk.component.shiro.ShiroUtil;
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.Role;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.repository.FirstUserRepository;
import com.pldk.modules.system.service.DeptService;
import com.pldk.modules.system.service.FirstUserService;
import com.pldk.modules.system.service.RoleService;

/**
 * @author Pldk
 * @date 2019/07/01
 */
@Controller
@RequestMapping("/system/firstUser")
public class FirstUserController {

	private static final Logger logger = LoggerFactory.getLogger(FirstUserController.class);
    @Autowired
    private FirstUserService firstUserService;

    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;
    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:firstUser:index")
    public String index(Model model, User fristUser) {
    	User user=ShiroUtil.getSubject();
    	if(fristUser.getDept()==null) {
    		fristUser.setDept(user.getDept());
    	}else {
    		if(fristUser.getDept().getLevel()!=user.getDept().getLevel()+1) {
    			fristUser.setDept(user.getDept());
    		}
    	}
    	
        // 获取数据列表	
        Page<User> list = firstUserService.getPageList(fristUser);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        model.addAttribute("dept",fristUser.getDept());
        return "/system/firstUser/index";
    }	

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("system:firstUser:add")
    public String toAdd() {
        return "/system/firstUser/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:firstUser:edit")
    public String toEdit(@PathVariable("id") User user, Model model) {
        model.addAttribute("user", user);
        return "/system/firstUser/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/save","/edit"})
    @RequiresPermissions({"system:firstUser:add","system:firstUser:edit"})
    @ResponseBody
    public ResultVo save(@Validated UserValid valid, User firstUser) {
        // 复制保留无需修改的数据
        if (firstUser.getId() != null) {
            User beUser = firstUserService.getById(firstUser.getId());
            EntityBeanUtil.copyProperties(beUser, firstUser);
        }
        if(firstUser.getDept().getLevel()!=2) {
        	return ResultVoUtil.error("一级用户只能添加二级用户,请核实后再试");
        }
        
     // 验证数据是否合格
        if (firstUser.getId() == null) {

            // 判断密码是否为空
            if (firstUser.getPassword().isEmpty() || "".equals(firstUser.getPassword().trim())) {
                throw new ResultException(ResultEnum.USER_PWD_NULL);
            }

            // 判断两次密码是否一致
            if (!firstUser.getPassword().equals(valid.getConfirm())) {
                throw new ResultException(ResultEnum.USER_INEQUALITY);
            }

            // 对密码进行加密
            String salt = ShiroUtil.getRandomSalt();
            String encrypt = ShiroUtil.encrypt(firstUser.getPassword(), salt);
            firstUser.setPassword(encrypt);
            firstUser.setSalt(salt);
        }
     // 判断用户名是否重复
        if (firstUserService.repeatByUsername(firstUser)) {
            throw new ResultException(ResultEnum.USER_EXIST);
        }

        // 复制保留无需修改的数据
        if (firstUser.getId() != null) {
            // 不允许操作超级管理员数据
            if (firstUser.getId().equals(AdminConst.ADMIN_ID) &&
                    !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
            }

            User beUser = firstUserService.getById(firstUser.getId());
            String[] fields = {"password", "salt", "picture", "roles"};
            EntityBeanUtil.copyProperties(beUser, firstUser, fields);
        }
        firstUser.setUnitType(UnitTypeEnum.LABORUN_UNIT_TYPE.getCode());
        // 保存数据
        User userInfo=firstUserService.save(firstUser);
        int size=userInfo.getRoles().size();
        if(userInfo.getId()!=null&&size==0) {
     	   Role role=roleService.getById((long)3);
     	   HashSet<Role> roles=new HashSet<>();
     	   roles.add(role);
     	// 更新用户角色
     	   userInfo.setRoles(roles);

            // 保存数据
     	  firstUserService.save(userInfo);
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到角色分配页面
     */
    @GetMapping("/role")
    @RequiresPermissions("system:firstUser:role")
    public String toRole(@RequestParam(value = "ids") User user, Model model) {
        // 获取指定用户角色列表
        Set<Role> authRoles = user.getRoles();
        // 获取全部角色列表
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<Role> list = roleService.getListBySortOk(sort);

        model.addAttribute("id", user.getId());
        model.addAttribute("list", list);
        model.addAttribute("authRoles", authRoles);
        return "/system/user/role";
    }
    /**
     * 保存角色分配信息
     */
    @PostMapping("/role")
    @RequiresPermissions("system:firstUser:role")
    @ResponseBody
    @ActionLog(key = UserAction.EDIT_ROLE, action = UserAction.class)
    public ResultVo auth(
            @RequestParam(value = "id", required = true) User user,
            @RequestParam(value = "roleId", required = false) HashSet<Role> roles) {

        // 不允许操作超级管理员数据
        if (user.getId().equals(AdminConst.ADMIN_ID) &&
                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }

        // 更新用户角色
        user.setRoles(roles);

        // 保存数据
        firstUserService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:firstUser:detail")
    public String toDetail(@PathVariable("id") User user, Model model) {
        model.addAttribute("user",user);
        return "/system/firstUser/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("system:firstUser:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (firstUserService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
    
    /**
     * 导出二级用户数据
     */
    @GetMapping("/export")
    @ResponseBody
    public void exportExcel() {
    	FirstUserRepository userRepository = SpringContextUtil.getBean(FirstUserRepository.class);
        ExcelUtil.exportExcel(User.class, userRepository.findAll());
    }
    
    /**
     * 导入二级用户数据
     */
    @PostMapping("/importFirstUser")
    @ResponseBody
    public ResultVo importtExcel(@RequestParam("file") MultipartFile file) {
    	List<User> list=null;
        try {
			list=ExcelUtil.importExcel(User.class, file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("导入excel数据总条数:"+list.size());
        List<User> userlist=new ArrayList<User>();
        
        for(User user:list) {
        	Dept dept=deptService.getByTitle(user.getDeptStr());
        	user.setDept(dept);
        }
        
        for(User user:list) {
        	if(user.getDept().getLevel()==2) {
        		 // 对密码进行加密
                String salt = ShiroUtil.getRandomSalt();
                String encrypt = ShiroUtil.encrypt(user.getPhone(), salt);
                user.setPassword(encrypt);
                boolean flag=firstUserService.repeatByUsername(user);
                if(!flag) {
                	userlist.add(user);
                }
        	}
        }
        logger.info("符合导入二级用户数据总条数:"+userlist.size());
        if(userlist.size()>0) {
           list=firstUserService.save(userlist);
        }
        return ResultVoUtil.success("批量导入二级用户数据成功,成功数据条数："+userlist.size());
    }
}