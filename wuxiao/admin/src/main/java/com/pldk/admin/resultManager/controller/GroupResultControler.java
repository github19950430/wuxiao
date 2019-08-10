package com.pldk.admin.resultManager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.pldk.admin.resultManager.validator.ResultValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.enums.UnitTypeEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.ToolUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.component.shiro.ShiroUtil;
import com.pldk.modules.foregoerManager.domain.Foregoer;
import com.pldk.modules.foregoerManager.service.ForegoerService;
import com.pldk.modules.resultManager.domain.Result;
import com.pldk.modules.resultManager.service.GroupResultService;
import com.pldk.modules.resultManager.service.ResultService;
import com.pldk.modules.system.domain.Upload;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.service.UploadService;

/**
 * 
 * 类描述：团省委成果管理
 * @author wcw
 * @addtime 2019年7月10日
 */
@Controller
@RequestMapping("/groupResultManager/result")
public class GroupResultControler {

	@Autowired
    private GroupResultService groupResultService;
    
    @Autowired
    private ForegoerService foregoerService;
    
    @Autowired
    private ResultService resultService;
    
    @Autowired
   	private UploadService uploadService;
    
    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("groupResultManager:result:index")
    public String index(Model model, Result result) {
    	result.setUnitType(UnitTypeEnum.GROUP_UNIT_TYPE.getCode()); //单位类型： 团省委
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("resultName", match -> match.contains());

        // 获取数据列表
        Example<Result> example = Example.of(result, matcher);
        Page<Result> list = groupResultService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/resultManager/groupResult/index";
    }

    /**
     * 跳转到添加/编辑页面
     */
    @GetMapping("/add")
    @RequiresPermissions("groupResultManager:result:add")
    public String toEdit(Long id, Model model) {
    	User user=ShiroUtil.getSubject();
    	Result result = new Result();
    	if(null!=id) {
    		 result =groupResultService.getById(id);
    	}
    	Foregoer foregoer=new Foregoer();
    	if(user.getUnitType()==2||user.getUnitType()==3) {
    		//团省委，省科协
    		foregoer.setDept(null);
    		foregoer.setUnitType(user.getUnitType());
    	}
    	List<Foregoer> list = foregoerService.getList(foregoer);
    	Map<Long, Object> map = new HashMap<Long, Object>();
    	list.forEach(forg->{
    		map.put(forg.getId(), forg.getName());
    	});
    	List<Upload> resultImg=uploadService.getListUpload(result.getUploadResultImg());
    	List<Upload> patentImg=uploadService.getListUpload(result.getUploadPatentImg());
    	List<Upload> honorImg=uploadService.getListUpload(result.getUploadHonorImg());
    	List<Upload> resultFile=uploadService.getListUpload(result.getUploadFile());
    	List<Upload> resultVideo=uploadService.getListUpload(result.getUploadVideo());
    	// 封装数据
        model.addAttribute("map", map);
        model.addAttribute("result", result);
        model.addAttribute("resultImg", resultImg);
        model.addAttribute("patentImg", patentImg);
        model.addAttribute("honorImg", honorImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
        return "/resultManager/groupResult/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"groupResultManager:result:add"/*,"groupResultManager:result:edit"*/})
    @ResponseBody
    public ResultVo save(@Validated ResultValid valid, Result result) {
    	
        // 复制保留无需修改的数据
        if (result.getId() != null) {
            Result beResult = groupResultService.getById(result.getId());
            EntityBeanUtil.copyProperties(beResult, result);
        }
        result.setAuditStatus(StatusEnum.AUDIT_REPORT.getValue());
        result.setUnitType(UnitTypeEnum.GROUP_UNIT_TYPE.getCode()); //单位类型： 团省委
        result.setArchivesNo(ToolUtil.no(resultService.findCurretDayResultCount()+1));//档案编号
        // 保存数据
        groupResultService.save(result);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("groupResultManager:result:detail")
    public String toDetail(@PathVariable("id") Result result, Model model) {
    	List<Upload> resultImg=uploadService.getListUpload(result.getUploadResultImg());
    	List<Upload> patentImg=uploadService.getListUpload(result.getUploadPatentImg());
    	List<Upload> honorImg=uploadService.getListUpload(result.getUploadHonorImg());
    	List<Upload> resultFile=uploadService.getListUpload(result.getUploadFile());
    	List<Upload> resultVideo=uploadService.getListUpload(result.getUploadVideo());
    	model.addAttribute("resultImg", resultImg);
        model.addAttribute("patentImg", patentImg);
        model.addAttribute("honorImg", honorImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
    	model.addAttribute("result",result);
        return "/resultManager/groupResult/detail";
    }
}
