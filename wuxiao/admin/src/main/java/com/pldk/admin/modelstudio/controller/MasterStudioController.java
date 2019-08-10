package com.pldk.admin.modelstudio.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

import com.alibaba.fastjson.JSON;
import com.pldk.admin.modelstudio.validator.MasterStudioValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.modelstudio.domain.MasterMember;
import com.pldk.modules.modelstudio.domain.MasterStudio;
import com.pldk.modules.modelstudio.service.MasterMemberService;
import com.pldk.modules.modelstudio.service.MasterStudioService;
import com.pldk.modules.system.domain.Upload;
import com.pldk.modules.system.service.UploadService;

/**
 * @author suhp
 * @date 2019/07/16
 */
@Controller
@RequestMapping("/modelstudio/masterStudio")
public class MasterStudioController {

    @Autowired
    private MasterStudioService masterStudioService;
    
    @Autowired
   	private UploadService uploadService;
    
    @Autowired
    private MasterMemberService masterMemberService;
    
    
    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("modelstudio:masterStudio:index")
    public String index(Model model, MasterStudio masterStudio) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<MasterStudio> example = Example.of(masterStudio, matcher);
        Page<MasterStudio> list = masterStudioService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/modelstudio/masterStudio/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("modelstudio:masterStudio:add")
    public String toAdd(Long id, Model model) {
    	MasterStudio ms=new MasterStudio();
    	if(null!=id) {
   		 	ms =masterStudioService.getById(id);
   	    }
    	List<Upload> img=uploadService.getListUpload(ms.getUploadImg());
    	List<Upload> file=uploadService.getListUpload(ms.getUploadFile());
    	List<Upload> video=uploadService.getListUpload(ms.getUploadVideo());
    	// 封装数据
        model.addAttribute("ms", ms);
        model.addAttribute("resultImg",img);
        model.addAttribute("resultFile",file);
        model.addAttribute("resultVideo", video);
        return "/modelstudio/masterStudio/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("modelstudio:masterStudio:edit")
    public String toEdit(@PathVariable("id") MasterStudio ms, Model model) {
    	List<MasterMember> mm=masterMemberService.getMasterMemberByStudio(ms.getId());
    	List<Upload> img=uploadService.getListUpload(ms.getUploadImg());
    	List<Upload> file=uploadService.getListUpload(ms.getUploadFile());
    	List<Upload> video=uploadService.getListUpload(ms.getUploadVideo());
    	// 封装数据
        model.addAttribute("ms", ms);
        model.addAttribute("mm", mm);
        model.addAttribute("resultImg",img);
        model.addAttribute("resultFile",file);
        model.addAttribute("resultVideo", video);
        return "/modelstudio/masterStudio/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @SuppressWarnings({"rawtypes" })
	@PostMapping({"/add","/edit"})
    @RequiresPermissions({"modelstudio:masterStudio:add","modelstudio:masterStudio:edit"})
    @ResponseBody
    public ResultVo save(@Validated MasterStudioValid valid, MasterStudio masterStudio) {
        // 复制保留无需修改的数据
        if (masterStudio.getId() != null) {
            MasterStudio bemasterStudio = masterStudioService.getById(masterStudio.getId());
            EntityBeanUtil.copyProperties(bemasterStudio, masterStudio);
        }
        toMasterSave(masterStudio);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("modelstudio:masterStudio:detail")
    public String toDetail(@PathVariable("id") MasterStudio ms, Model model) {
    	List<MasterMember> mm=masterMemberService.getMasterMemberByStudio(ms.getId());
    	List<Upload> img=uploadService.getListUpload(ms.getUploadImg());
    	List<Upload> file=uploadService.getListUpload(ms.getUploadFile());
    	List<Upload> video=uploadService.getListUpload(ms.getUploadVideo());
        model.addAttribute("ms",ms);
        model.addAttribute("mm", mm);
        model.addAttribute("resultImg",img);
        model.addAttribute("resultFile",file);
        model.addAttribute("resultVideo", video);
        return "/modelstudio/masterStudio/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("modelstudio:masterStudio:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (masterStudioService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
    
    /**
         * 添加
     */
    @SuppressWarnings("unchecked")
	public void toMasterSave(MasterStudio masterStudio) {
    	MasterStudio newMe=masterStudioService.save(masterStudio);
		List<Map<String,Object>> listMap=(List<Map<String, Object>>) JSON.parse(masterStudio.getKeyMember());
	    for(Map<String,Object> m:listMap) {
	    	MasterMember mm=new MasterMember();
	    	String id=m.get("id")==null?"":m.get("id").toString();
	    	mm.setMasterMemberId(newMe.getId());
	    	mm.setName(m.get("name")==null?"":m.get("name").toString());
	    	mm.setSex(m.get("sex")==null?"":m.get("sex").toString());
	    	mm.setEducation(m.get("education")==null?"":m.get("education").toString());
	    	mm.setTechnicalTitles(m.get("technicalTitles")==null?"":m.get("technicalTitles").toString());
	    	mm.setDepartment(m.get("deptName")==null?"":m.get("deptName").toString());
	    	mm.setBirthday(m.get("birthday")==null?"":m.get("birthday").toString());
	    	mm.setDivisionOfLabor(m.get("work")==null?"":m.get("work").toString());
	    	if(StringUtils.isNotBlank(id)) {
	    		mm.setId(Long.valueOf(id));
	    		MasterMember newMm=masterMemberService.getById(Long.valueOf(id));
	    		EntityBeanUtil.copyProperties(newMm,mm);
	    	}
	        masterMemberService.save(mm);
	    }
    }
    
    
    @SuppressWarnings("rawtypes")
	@PostMapping("/delMember")
    @ResponseBody
    public ResultVo delMember(Long id) {
    	try {
    		masterMemberService.delMember(id);
    		return ResultVoUtil.success();
    	}catch(Exception e) {
    		return ResultVoUtil.error( "失败，请重新操作");
    	}
    }
    
}