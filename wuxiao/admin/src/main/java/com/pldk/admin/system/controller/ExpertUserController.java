package com.pldk.admin.system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import com.pldk.component.actionLog.annotation.EntityParam;
import com.pldk.component.excel.ExcelUtil;
import com.pldk.component.shiro.ShiroUtil;
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.Dict;
import com.pldk.modules.system.domain.Role;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.repository.ExpertUserRepository;
import com.pldk.modules.system.service.DictService;
import com.pldk.modules.system.service.ExpertUserService;
import com.pldk.modules.system.service.RoleService;
import com.pldk.modules.system.service.UserService;

/**
 * @author Pldk
 * @date 2019/07/01
 */
@Controller
@RequestMapping("/system/expertUser")
public class ExpertUserController {

	private static final Logger logger = LoggerFactory.getLogger(ExpertUserController.class);
    @Autowired
    private ExpertUserService expertUserService;

    @Autowired
    private DictService dictService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:expertUser:index")
    public String index(Model model, User expertUser) {

    	User user=ShiroUtil.getSubject();
    	if(expertUser.getDept()==null) {
    		//fristUser=ShiroUtil.getSubject();
    		expertUser.setDept(user.getDept());
    	}else {
    		if(expertUser.getDept().getId()!=user.getDept().getId()) {
    			expertUser.setDept(user.getDept());
    		}
    	}
        // 获取数据列表	
        Page<User> list = expertUserService.getPageList(expertUser);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/expertUser/index";
    }	

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("system:expertUser:add")
    public String toAdd(Model model) {
    	User user=ShiroUtil.getSubject();
    	model.addAttribute("dept", user.getDept());
        return "/system/expertUser/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:expertUser:edit")
    public String toEdit(@PathVariable("id") User user, Model model) {
    	User userSession=ShiroUtil.getSubject();
    	model.addAttribute("dept", userSession.getDept());
        model.addAttribute("user", user);
        return "/system/expertUser/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/save","/edit"})
    @RequiresPermissions({"system:expertUser:add","system:expertUser:edit"})
    @ResponseBody
    @ActionLog(key = UserAction.EXPERT_SAVE, action = UserAction.class)
    public ResultVo save(@Validated UserValid valid, @EntityParam User expertUser) {
        // 复制保留无需修改的数据
    	User userSession=ShiroUtil.getSubject();
        if (expertUser.getId() != null) {
            User beUser = expertUserService.getById(expertUser.getId());
            EntityBeanUtil.copyProperties(beUser, expertUser);
        }
        if(expertUser.getDept()==userSession.getDept()) {
        	return ResultVoUtil.error("只能添加本工会的专家用户,请核实后再试");
        }
        // 判断用户名是否重复
        if (userService.repeatByUsername(expertUser)) {
            throw new ResultException(ResultEnum.USER_EXIST);
        }
        // 验证数据是否合格
        if (expertUser.getId() == null) {

            // 判断密码是否为空
            if (expertUser.getPassword().isEmpty() || "".equals(expertUser.getPassword().trim())) {
                throw new ResultException(ResultEnum.USER_PWD_NULL);
            }

            // 判断两次密码是否一致
            if (!expertUser.getPassword().equals(valid.getConfirm())) {
                throw new ResultException(ResultEnum.USER_INEQUALITY);
            }

            // 对密码进行加密
            String salt = ShiroUtil.getRandomSalt();
            String encrypt = ShiroUtil.encrypt(expertUser.getPassword(), salt);
            expertUser.setPassword(encrypt);
            expertUser.setSalt(salt);
        }
      
      
        // 复制保留无需修改的数据
        if (expertUser.getId() != null) {
            // 不允许操作超级管理员数据
            if (expertUser.getId().equals(AdminConst.ADMIN_ID) &&
                    !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
            }

            User beUser = userService.getById(expertUser.getId());
            String[] fields = {"password", "salt", "picture", "roles"};
            EntityBeanUtil.copyProperties(beUser, expertUser, fields);
        }
        // 保存数据
        expertUser.setIsExpert(1); //是否专家
        expertUser.setUnitType(UnitTypeEnum.LABORUN_UNIT_TYPE.getCode());
       User userInfo= expertUserService.save(expertUser);
       int size=userInfo.getRoles().size();
       if(userInfo.getId()!=null&&size==0) {
    	   Role role=roleService.getById((long)6);
    	   HashSet<Role> roles=new HashSet<>();
    	   roles.add(role);
    	// 更新用户角色
    	   userInfo.setRoles(roles);

           // 保存数据
           userService.save(userInfo);
       }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到角色分配页面
     */
    @GetMapping("/role")
    @RequiresPermissions("system:expertUser:role")
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
    @RequiresPermissions("system:expertUser:role")
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
        expertUserService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:expertUser:detail")
    public String toDetail(@PathVariable("id") User user, Model model) {
        model.addAttribute("user",user);
        return "/system/expertUser/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("system:expertUser:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (expertUserService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
    
    /**
     * 导出专家用户数据
     */
    @GetMapping("/export")
    @ResponseBody
    public void exportExcel() {
    	ExpertUserRepository expertUserRepository = SpringContextUtil.getBean(ExpertUserRepository.class);
    	  // 创建匹配器，进行动态查询匹配
    	User userSession=ShiroUtil.getSubject();
      
    	User user=new User();
    	user.setIsExpert(1);
    	user.setStatus((byte)1);
    	user.setDept(userSession.getDept());
    	List<User> list=expertUserRepository.findAll(new Specification<User>(){

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                preList.add(cb.equal(root.get("isExpert").as(Integer.class), 1));
                if(user.getDept() != null){
                    Dept dept = user.getDept();
                    List<Long> deptIn = new ArrayList<>();
                    deptIn.add(dept.getId());
                    Join<User, Dept> join = root.join("dept", JoinType.INNER);
                    CriteriaBuilder.In<Long> in = cb.in(join.get("id").as(Long.class));
                    deptIn.forEach(in::value);
                    preList.add(in);
                }
                

                // 数据状态
                if(user.getStatus() != null){
                    preList.add(cb.equal(root.get("status").as(Byte.class), user.getStatus()));
                }

                Predicate[] pres = new Predicate[preList.size()];
                return query.where(preList.toArray(pres)).getRestriction();
            }

        });
        ExcelUtil.exportExcel(User.class, list);
    }
    
    /**
     * 导入专家用户数据
     *
     */
    @PostMapping("/importExpertUser")
    @ResponseBody
    public ResultVo importExcel(@RequestParam("file") MultipartFile file) {
    	User currentuser=ShiroUtil.getSubject();
    	List<User> list=null;
        try {
			list=ExcelUtil.importExcel(User.class, file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("导入excel数据总条数:"+list.size());
        List<User> userlist=new ArrayList<User>();
    	User userSession=ShiroUtil.getSubject();
    	int errorNum=0;
    	String join="";
    	boolean flag=true;
        for(User user:list) {
        	user.setDept(userSession.getDept());
        	if("男".equals(user.getSex())) {
        		user.setSex("1");
        	}else {
        		user.setSex("2");
        	}
        	Dict dict=dictService.getByNameOk("RESULT_TYPE");
        	String type=getStringToMap(dict.getValue(),user.getReviewType());
        	if(type!=null) {
        		user.setReviewType(type);
        		String salt = ShiroUtil.getRandomSalt();
				String encrypt = ShiroUtil.encrypt(user.getPassword(), salt);
				user.setPassword(encrypt);
				user.setSalt(salt);
				user.setIsExpert(1);
				boolean flags=expertUserService.repeatByUsername(user);
				if(!flags) {
					 Role role=roleService.getById((long)6);
			    	   HashSet<Role> roles=new HashSet<>();
			    	   roles.add(role);
			    	// 更新用户角色
			    	   user.setRoles(roles);
					userlist.add(user);
				}else {
					flag=false;
	        		errorNum=errorNum+1;
	        		join=join+"\r\n"+"用户名:"+user.getUsername()+"已添加";
				}
        	}else {
        		flag=false;
        		errorNum=errorNum+1;
        		join=join+"\r\n"+"用户名:"+user.getUsername()+"专家评审类型填写有误";
        	}
   
        	
        }
        
        logger.info("符合导入专家用户数据总条数:"+userlist.size());
        if(userlist.size()>0) {
        	try {
        		list=expertUserService.save(userlist);
			} catch (Exception e) {
				return ResultVoUtil.error(400, "导入失败");
			}
        }
        if(flag) {
        	return ResultVoUtil.success("批量导入专家用户数据成功,成功数据条数："+userlist.size());
        }else {
        	return ResultVoUtil.error("批量导入专家用户数据成功,成功数据条数："+userlist.size()+"\r\n"+join);
        }
    }

    public String toActive(){

        return null;
    }
    /**
     * 跳转到激活页面
     * */
    @GetMapping("/toActive")
    @RequiresPermissions("system:user:active")
    public String toActive(Model model, @RequestParam(value = "ids", required = false) List<Long> ids) {
        model.addAttribute("idList", ids);
        return "/system/expertUser/active";
    }
     /**
     * 修改密码
     */
    @PostMapping("/saveActive")
    @RequiresPermissions("system:user:active")
    @ResponseBody
    @ActionLog(key = UserAction.EDIT_PWD, action = UserAction.class)
    public ResultVo editPassword(String expBeginDate, String expEndDate,
                                 @RequestParam(value = "ids", required = false) List<Long> ids,
                                 @RequestParam(value = "ids", required = false) List<User> users) {
        users.forEach(user->{
            user.setExpBeginDate(expBeginDate);
            user.setExpEndDate(expEndDate);
        });
        // 保存数据
        userService.save(users);
        return ResultVoUtil.success("激活成功");
    }
    
	private static String getStringToMap(final String value,final String type) {
    	String[] mapArray = value.split(",");
    	String join=null;
    	for(int j=0 ;j<mapArray.length ;j++) {
    		String str = mapArray[j].replaceAll("\"", "");
    		String[] keyValue = str.split(":");
    		if(keyValue.length==2&&keyValue[1].equals(type)) {
    			join= keyValue[0];
    		}
    	}
    	return join;
    }
}