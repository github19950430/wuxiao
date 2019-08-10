package com.pldk.admin.resultManager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.pldk.admin.resultManager.validator.ResultValid;
import com.pldk.common.enums.AuditEnum;
import com.pldk.common.enums.LevelEnum;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.enums.UnitTypeEnum;
import com.pldk.common.utils.DateUtil;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.utils.ToolUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.component.shiro.ShiroUtil;
import com.pldk.modules.auditRecord.domain.AuditRecord;
import com.pldk.modules.auditRecord.service.AuditRecordService;
import com.pldk.modules.foregoerManager.domain.Foregoer;
import com.pldk.modules.foregoerManager.service.ForegoerService;
import com.pldk.modules.modelstudio.domain.MasterMember;
import com.pldk.modules.modelstudio.domain.Modelstudio;
import com.pldk.modules.modelstudio.service.ModelstudioService;
import com.pldk.modules.resultManager.domain.Result;
import com.pldk.modules.resultManager.domain.ResultMember;
import com.pldk.modules.resultManager.service.ResultMemberService;
import com.pldk.modules.resultManager.service.ResultService;
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.Upload;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.service.DeptService;
import com.pldk.modules.system.service.ExpertUserService;
import com.pldk.modules.system.service.UploadService;

/**
 * @author Pldk
 * @date 2019/06/29
 */
@Controller
@RequestMapping("/resultManager/result")
public class ResultController {

    @Autowired
    private ResultService resultService;
    @Autowired
    private ResultMemberService resultMemberService;
    @Autowired
    private ForegoerService foregoerService;
    @Autowired
    private  DeptService deptService;
    @Autowired
    private ExpertUserService expertUserService;
    @Autowired
    private AuditRecordService auditRecordService;
    
    @Autowired
    private ModelstudioService modelstudioService;
    @Autowired
   	private UploadService uploadService;
    
    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("resultManager:result:index")
    public String index(Model model, Result result) {
    	result.setUnitType(UnitTypeEnum.LABORUN_UNIT_TYPE.getCode());//查询单位类型为工会的数据
        User user=ShiroUtil.getSubject();
        result.setDept(user.getDept());
        // 创建匹配器，进行动态查询匹配
        /*ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("resultName", match -> match.contains());

        // 获取数据列表
        Example<Result> example = Example.of(result, matcher);
        Page<Result> list = resultService.getPageList(example);*/
    	Page<Result> list = resultService.getPageList(result);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/resultManager/result/index";
    }

    /**
     * 跳转到添加/编辑页面
     */
    @GetMapping("/add")
    @RequiresPermissions("resultManager:result:add")
    public String toEdit(Long id, Model model) {
    	Result result = new Result();
    	if(null!=id) {
    		 result =resultService.getById(id);
    	}
    	Foregoer foregoer=new Foregoer();
    	User user=ShiroUtil.getSubject();
    	foregoer.setDept(user.getDept());
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
    	
    	ResultMember resultMember=new ResultMember();
    	resultMember.setResult(result);
    	List<ResultMember> resultMemberlist=resultMemberService.getList(resultMember);
    	// 封装数据
    	model.addAttribute("resultMemberlist", resultMemberlist);
        model.addAttribute("map", map);
        model.addAttribute("result", result);
        model.addAttribute("resultImg", resultImg);
        model.addAttribute("patentImg", patentImg);
        model.addAttribute("honorImg", honorImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
        model.addAttribute("studioMap", modelstudioService.findAll());
        model.addAttribute("studioId", result.getStudioId()+","+result.getStudioType());
        return "/resultManager/result/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @SuppressWarnings("rawtypes")
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"resultManager:result:add","resultManager:result:edit"})
    @ResponseBody
    public ResultVo save(@Validated ResultValid valid, Result result) {
    	User user = ShiroUtil.getSubject();
        // 复制保留无需修改的数据 
        if (result.getId() != null) {
            Result beResult = resultService.getById(result.getId());
            EntityBeanUtil.copyProperties(beResult, result);
        }
        result.setDept(user.getDept());//登陆用户所属单位id
        result.setAuditStatus(StatusEnum.AUDIT_REPORT.getValue()); //三级待上报
        result.setUnitType(user.getUnitType()); //登录用户单位类型
        result.setArchivesNo(ToolUtil.no(resultService.findCurretDayResultCount()+1));
        result.setAddLevel(LevelEnum.THREE_LEVEL.getCode());//添加成果用户级别
        // 保存数据
        if(!StringUtils.isEmpty(result.getStudioIdAndType())&&result.getStudioIdAndType().contains(",")) {
        	String[] studioIdAndTypes=result.getStudioIdAndType().split(",");
        	result.setStudioId(Integer.parseInt(studioIdAndTypes[0]));
        	result.setStudioType(Integer.parseInt(studioIdAndTypes[1]));
        	if(result.getStudioType()==1) {
        		//查询工作室下面的带头人
        	    Modelstudio modelstudio=modelstudioService.getById(Long.valueOf(result.getStudioId()+""));
        	    result.setStudioLeader(modelstudio.getLingxianrenId());
        	}else {
        		//查询工作室下面的带头人---大师工作室就是带头人
        		result.setStudioLeader(Long.parseLong(studioIdAndTypes[0]));
        	}
        }
        result.setYear(DateUtil.getYear(result.getDeclareDate()));
        resultService.save(result);
        toResultMeberSave(result);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    
    /**
     * 添加
	 */
	@SuppressWarnings("unchecked")
	public void toResultMeberSave(Result result) {
		List<Map<String,Object>> listMap=(List<Map<String, Object>>) JSON.parse(result.getKeyMember());
	    for(Map<String,Object> m:listMap) {
	    	ResultMember mm=new ResultMember();
	    	String id=m.get("id")==null?"":m.get("id").toString();
	    	mm.setResult(result);
	    	mm.setName(m.get("name")==null?"":m.get("name").toString());
	    	mm.setSex(m.get("sex")==null?"":m.get("sex").toString());
	    	mm.setEducation(m.get("education")==null?"":m.get("education").toString());
	    	mm.setTechnicalTitles(m.get("technicalTitles")==null?"":m.get("technicalTitles").toString());
	    	mm.setDepartment(m.get("deptName")==null?"":m.get("deptName").toString());
	    	mm.setBirthday(m.get("birthday")==null?"":m.get("birthday").toString());
	    	mm.setDivisionOfLabor(m.get("work")==null?"":m.get("work").toString());
	    	if(!StringUtils.isEmpty(id)) {
	    		mm.setId(Long.valueOf(id));
	    		ResultMember newMm=resultMemberService.getById(Long.valueOf(id));
	    		EntityBeanUtil.copyProperties(newMm,mm);
	    	}
	    	resultMemberService.save(mm);
	    }
	}
	
	
	/**
     * 删除相关成员
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/delResultMember")
    @RequiresPermissions("resultManager:result:delResultMember")
    @ResponseBody
    public ResultVo delResultMember(@RequestParam(value = "id", required = false) Long id) {
    	try {
			resultMemberService.delMember(id);
			return ResultVoUtil.SAVE_SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return ResultVoUtil.error( "失败，请重新操作");
		}
    }
	

    /** 
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("resultManager:result:detail")
    public String toDetail(@PathVariable("id") Result result, Model model) {
    	ResultMember resultMember=new ResultMember();
    	resultMember.setResult(result);
    	List<ResultMember> resultMemberlist=resultMemberService.getList(resultMember);
    	List<Upload> resultImg=uploadService.getListUpload(result.getUploadResultImg());
    	List<Upload> patentImg=uploadService.getListUpload(result.getUploadPatentImg());
    	List<Upload> honorImg=uploadService.getListUpload(result.getUploadHonorImg());
    	List<Upload> resultFile=uploadService.getListUpload(result.getUploadFile());
    	List<Upload> resultVideo=uploadService.getListUpload(result.getUploadVideo());
    	Page<Map<String,Object>> recordList = resultService.findAllResultById(result.getId());
    	model.addAttribute("recordList", recordList.getContent());
    	model.addAttribute("page", recordList);
        model.addAttribute("result", result);
        model.addAttribute("resultMemberlist", resultMemberlist);
       /* if(result.getStudioType()==1) {
    		//查询工作室下面的带头人
        	if(null!=result.getStudioLeader()) {
	        	Leaderinformation leaderinformation=leaderinformationService.getById(result.getStudioLeader());
	        	model.addAttribute("leaderinformation", leaderinformation);
        	}
    	}else {
    		//查询工作室下面的带头人---大师工作室就是带头人
    		if(null!=result.getStudioLeader()) {
	    		MasterStudio masterStudio=masterStudioService.getById(result.getStudioLeader());
	    		model.addAttribute("masterStudio", masterStudio);
    		}
    	}*/
        model.addAttribute("resultImg", resultImg);
        model.addAttribute("patentImg", patentImg);
        model.addAttribute("honorImg", honorImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
        return "/resultManager/result/detail";
    }
    
    /**
     * 三级上报数据
     */
    @SuppressWarnings("rawtypes")
    @GetMapping("/report/{id}")
    @RequiresPermissions("resultManager:result:report")
    @ResponseBody
    public ResultVo toReport(@PathVariable("id") Result result) {
    	
    	if(result.getAuditStatus()==StatusEnum.FIRST_AUDIT_DEAL.getValue()) {
    		return ResultVoUtil.error("不能上报，请核实信息");
    	}
    	// 新增审核记录数据
    	User user = ShiroUtil.getSubject();
    	AuditRecord auditRecord = new AuditRecord();
    	auditRecord.setAuditId(result.getId());
    	auditRecord.setDept(user.getDept());
    	auditRecord.setDeptName(user.getDept().getTitle());
    	auditRecord.setType(AuditEnum.RESULT.getValue());
    	auditRecord.setName(user.getRealname());
    	auditRecord.setAuditStatus(StatusEnum.FIRST_AUDIT_WAIT.getValue());
    	auditRecordService.save(auditRecord);
    	// 更新数据
    	result.setAuditStatus(StatusEnum.FIRST_AUDIT_WAIT.getValue());//待初审
    	resultService.report(result);
    	return ResultVoUtil.SAVE_SUCCESS;
    }
    
  //=================================== 一级用户  =========================================================// 
    
    /**
     * 一级用户汇总列表页面
     */
    @GetMapping("/frist/collect")
    @RequiresPermissions("resultManager:result:frist:collect")
    public String fristCollect(Model model, 
    		@RequestParam(value = "deptName", required = false) String deptName,@RequestParam(value = "year", required = false)String year) {

    	List<Map<String, Object>> collectionNew=new ArrayList<Map<String,Object>>();
    	Page<Map<String, Object>> page = resultService.getCollectList(deptName);
    	List<Map<String, Object>> collectList=page.getContent();

    	for(Map<String, Object> forg:collectList) {
    		Map<String, Object> map=new HashMap<String, Object>();
    		map.put("title", forg.get("title"));
    		map.put("username", forg.get("username"));
    		map.put("phone", forg.get("phone"));
    		map.put("dept_id", forg.get("dept_id"));
    		collectionNew.add(map);
    	};
    	for(Map<String, Object> forg:collectionNew) {
    	    Result result=new Result();
    	    result.setYear(year);
    	    result.setDept(deptService.getById(Long.parseLong(forg.get("dept_id")+"")));
    	    //待审核
        	String auditStatuss= "4";
        	result.setAuditStatusList(auditStatuss);
        	forg.put("unVerifyCount",resultService.getList(result).size());
        	 
        	//已通过
        	auditStatuss= "5,7,8,9";
        	result.setAuditStatusList(auditStatuss);
        	forg.put("verifyPassCount",resultService.getList(result).size());
        	
        	//已驳回
        	auditStatuss= "6";
        	result.setAuditStatusList(auditStatuss);
        	forg.put("verifyUnpassCount",resultService.getList(result).size());
        	
        	//已退回,不能再次上报
        	auditStatuss= "11";
        	result.setAuditStatusList(auditStatuss);
        	forg.put("verifyDealCount",resultService.getList(result).size());
    	}; 
    	
        // 封装数据
        model.addAttribute("list", collectionNew);
        model.addAttribute("page", page);
        model.addAttribute("year", year);
        return "/resultManager/fristResult/collect";
    }
    
    /**
     * 一级用户列表页面
     */
    @GetMapping("/frist/index/{id}")
    @RequiresPermissions("resultManager:result:frist:index")
    public String fristIndex(@PathVariable("id") Dept dept,Model model, Result result) {
    	result.setUnitType(UnitTypeEnum.LABORUN_UNIT_TYPE.getCode());//查询单位类型为工会的数据
    	//result.setAuditStatus(StatusEnum.AUDIT_WAIT.getValue());//待审核
    	// 创建匹配器，进行动态查询匹配
    	/*ExampleMatcher matcher = ExampleMatcher.matching()
    			.withMatcher("resultName", match -> match.contains());
    	
    	// 获取数据列表
    	Example<Result> example = Example.of(result, matcher);
    	Page<Result> list = resultService.getPageList(example);*/
    	//Dept dept=deptService.getById(deptId);
    	result.setDept(dept);
    	if(StringUtils.isEmpty(result.getAuditStatusList())){
    		String auditStatusList= "4,5,6,7,8,9,11";
        	result.setAuditStatusList(auditStatusList);
    	}
    	Page<Result> list = resultService.getPageList(result);

    	// 封装数据
    	model.addAttribute("list", list.getContent());
    	model.addAttribute("page", list);
    	 model.addAttribute("year", result.getYear());
    	 model.addAttribute("result", result);
    	return "/resultManager/fristResult/index";
    }
    
    /**
     * 跳转到一级详细页面
     */
    @GetMapping("/frist/detail/{id}")
    @RequiresPermissions("resultManager:result:frist:detail")
    public String toFristDetail(@PathVariable("id") Result result, Model model) {
    	List<Upload> resultImg=uploadService.getListUpload(result.getUploadResultImg());
    	List<Upload> patentImg=uploadService.getListUpload(result.getUploadPatentImg());
    	List<Upload> honorImg=uploadService.getListUpload(result.getUploadHonorImg());
    	List<Upload> resultFile=uploadService.getListUpload(result.getUploadFile());
    	List<Upload> resultVideo=uploadService.getListUpload(result.getUploadVideo());
    	Page<Map<String,Object>> recordList = resultService.findAllResultById(result.getId());
    	model.addAttribute("recordList", recordList.getContent());
    	model.addAttribute("page", recordList);
        model.addAttribute("result", result);
        model.addAttribute("resultImg", resultImg);
        model.addAttribute("patentImg", patentImg);
        model.addAttribute("honorImg", honorImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
    	return "/resultManager/fristResult/detail";
    }

    /**
     * 跳转到一级审核页面
     */
    @GetMapping("/frist/audit/{id}")
    @RequiresPermissions("resultManager:result:frist:audit")
    public String toFristAudit(@PathVariable("id") Result result, Model model) {
    	List<Upload> resultImg=uploadService.getListUpload(result.getUploadResultImg());
    	List<Upload> patentImg=uploadService.getListUpload(result.getUploadPatentImg());
    	List<Upload> honorImg=uploadService.getListUpload(result.getUploadHonorImg());
    	List<Upload> resultFile=uploadService.getListUpload(result.getUploadFile());
    	List<Upload> resultVideo=uploadService.getListUpload(result.getUploadVideo());
    	model.addAttribute("result",result);
    	model.addAttribute("resultImg", resultImg);
        model.addAttribute("patentImg", patentImg);
        model.addAttribute("honorImg", honorImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
    	return "/resultManager/fristResult/audit";
    }
    
    /**
     * 一级审核数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/frist/audit")
    @RequiresPermissions("resultManager:result:frist:audit")
    @ResponseBody
    public ResultVo toFristAudit(Result result) {
    	User user = ShiroUtil.getSubject();
    	AuditRecord auditRecord = new AuditRecord();
    	auditRecord.setAuditId(result.getId());
    	auditRecord.setAuditStatus(result.getAuditStatus());
    	auditRecord.setDept(user.getDept());
    	auditRecord.setDeptName(user.getDept().getTitle());
    	auditRecord.setType(AuditEnum.RESULT.getValue());
    	auditRecord.setName(user.getRealname());
    	auditRecordService.save(auditRecord);
        resultService.auditRes(result);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    
    /**
     * 一级上报数据
     */
    /*@GetMapping("/frist/report/{id}")
    @RequiresPermissions("resultManager:result:frist:report")
    @ResponseBody
    public ResultVo toFristReport(@PathVariable("id") Result result) {
    	// 更新数据
    	result.setAuditStatus(StatusEnum.SCORE_WAIT.getValue());//待评分
    	System.out.println(result);
    	resultService.report(result);
    	return ResultVoUtil.SAVE_SUCCESS;
    }
    */
    
  //=================================== 二级用户  =========================================================// 
    
    /**
     * 二级用户汇总列表页面
     */
    @GetMapping("/second/collect")
    @RequiresPermissions("resultManager:result:second:collect")
    public String secondCollect(Model model, 
    		@RequestParam(value = "deptName", required = false) String deptName) {
    	List<String> auditStatusList=new ArrayList<String>();
    	auditStatusList.add("1");
    	auditStatusList.add("2");
    	auditStatusList.add("3");
    	auditStatusList.add("4");
    	auditStatusList.add("5");
    	auditStatusList.add("6");
    	auditStatusList.add("7");
    	auditStatusList.add("8");
    	auditStatusList.add("10");
    	auditStatusList.add("11");
    	User user=ShiroUtil.getSubject();
    	List<Map<String,Object>> collectionNew= new ArrayList<Map<String,Object>>();
    	Page<Map<String,Object>> page = resultService.getCollectList(deptName, auditStatusList,user.getDept().getId(),3);
    	List<Map<String,Object>>  collectList=page.getContent();
    	/*collectList.forEach(forg->{ 
    		
    	});*/
    	for(Map<String, Object> forg:collectList) {
    		Map<String, Object> map=new HashMap<String, Object>();
    		map.put("title", forg.get("title"));
    		map.put("username", forg.get("username"));
    		map.put("phone", forg.get("phone"));
    		map.put("dept_id", forg.get("dept_id"));
    		collectionNew.add(map);
    	};
    	for(Map<String, Object> forg:collectionNew) {
    	    Result result=new Result();
    	    result.setDept(deptService.getById(Long.parseLong(forg.get("dept_id")+"")));
    	    //待审核
        	String auditStatuss= "1";
        	result.setAuditStatusList(auditStatuss);
        	forg.put("unVerifyCount",resultService.getList(result).size());
        	 
        	//已审核
        	auditStatuss= "2,4,5,6,7,8,11";
        	result.setAuditStatusList(auditStatuss);
        	forg.put("verifyPassCount",resultService.getList(result).size());
        	
        	//已驳回
        	auditStatuss= "3";
        	result.setAuditStatusList(auditStatuss);
        	forg.put("verifyUnpassCount",resultService.getList(result).size());
        	//已驳回,不能再次上报
        	auditStatuss= "10";
        	result.setAuditStatusList(auditStatuss);
        	forg.put("verifyDealCount",resultService.getList(result).size());
    	}; 
    	
        // 封装数据
        model.addAttribute("list", collectionNew);
        model.addAttribute("page", page);
        return "/resultManager/secondResult/collect";
    }
    
    /**
     * 二级用户列表页面
     */
    @GetMapping("/second/index/{id}")
    @RequiresPermissions("resultManager:result:second:index")
    public String secondIndex(@PathVariable("id") Dept dept,Model model, Result result) {
    	result.setUnitType(UnitTypeEnum.LABORUN_UNIT_TYPE.getCode());//查询单位类型为工会的数据
    	//result.setAuditStatus(StatusEnum.FIRST_AUDIT_WAIT.getValue());//待初审
        // 创建匹配器，进行动态查询匹配
        /*ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("resultName", match -> match.contains());

        // 获取数据列表
        Example<Result> example = Example.of(result, matcher);
        Page<Result> list = resultService.getPageList(example);*/
    	result.setDept(dept);
    	if(StringUtils.isEmpty(result.getAuditStatusList())){
    		String auditStatusList= "1,2,3,4,5,6,7,8,10,11";
        	result.setAuditStatusList(auditStatusList);
    	}
    	Page<Result> list = resultService.getPageList(result);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/resultManager/secondResult/index";
    }
    
    /**
     * 跳转到二级详细页面
     */
    @GetMapping("/second/detail/{id}")
    @RequiresPermissions("resultManager:result:second:detail")
    public String toSecondDetail(@PathVariable("id") Result result, Model model) {
    	List<Upload> resultImg=uploadService.getListUpload(result.getUploadResultImg());
    	List<Upload> patentImg=uploadService.getListUpload(result.getUploadPatentImg());
    	List<Upload> honorImg=uploadService.getListUpload(result.getUploadHonorImg());
    	List<Upload> resultFile=uploadService.getListUpload(result.getUploadFile());
    	List<Upload> resultVideo=uploadService.getListUpload(result.getUploadVideo());
    	Page<Map<String,Object>> recordList = resultService.findAllResultById(result.getId());
    	model.addAttribute("recordList", recordList.getContent());
    	model.addAttribute("page", recordList);
        model.addAttribute("result", result);
        model.addAttribute("resultImg", resultImg);
        model.addAttribute("patentImg", patentImg);
        model.addAttribute("honorImg", honorImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
      //相关成员
        ResultMember resultMember=new ResultMember();
    	resultMember.setResult(result);
    	List<ResultMember> resultMemberlist=resultMemberService.getList(resultMember);
        model.addAttribute("resultMemberlist", resultMemberlist);
    	return "/resultManager/secondResult/detail";
    }
    
    /**
     * 跳转到二级审核页面
     */
    @GetMapping("/second/audit/{id}")
    @RequiresPermissions("resultManager:result:second:audit")
    public String toSecondAudit(@PathVariable("id") Result result, Model model) {
    	List<Upload> resultImg=uploadService.getListUpload(result.getUploadResultImg());
    	List<Upload> patentImg=uploadService.getListUpload(result.getUploadPatentImg());
    	List<Upload> honorImg=uploadService.getListUpload(result.getUploadHonorImg());
    	List<Upload> resultFile=uploadService.getListUpload(result.getUploadFile());
    	List<Upload> resultVideo=uploadService.getListUpload(result.getUploadVideo());
    	model.addAttribute("result",result);
    	model.addAttribute("resultImg", resultImg);
        model.addAttribute("patentImg", patentImg);
        model.addAttribute("honorImg", honorImg);
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("resultVideo", resultVideo);
      //相关成员
        ResultMember resultMember=new ResultMember();
    	resultMember.setResult(result);
    	List<ResultMember> resultMemberlist=resultMemberService.getList(resultMember);
        model.addAttribute("resultMemberlist", resultMemberlist);
    	return "/resultManager/secondResult/audit";
    }
    
    /**
     * 二级审核数据
     */
    @SuppressWarnings("rawtypes")
	@PostMapping("/second/audit")
    @RequiresPermissions("resultManager:result:second:audit")
    @ResponseBody
    public ResultVo toSecondAudit(Result result) {
        // 更新数据
    	User user = ShiroUtil.getSubject();
    	AuditRecord auditRecord = new AuditRecord();
    	auditRecord.setAuditId(result.getId());
    	auditRecord.setAuditStatus(result.getAuditStatus());
    	auditRecord.setDept(user.getDept());
    	auditRecord.setDeptName(user.getDept().getTitle());
    	auditRecord.setType(AuditEnum.RESULT.getValue());
    	auditRecord.setName(user.getRealname());
    	auditRecordService.save(auditRecord);
        resultService.auditRes(result);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    
    /**
     * 二级上报数据
     */
    @GetMapping("/second/report/{id}")
    @RequiresPermissions("resultManager:result:second:report")
    @ResponseBody
    public ResultVo toSecondReport(@PathVariable("id") Result result) {
    	
    	if(result.getAuditStatus()==StatusEnum.AUDIT_DEAL.getValue()) {
    		return ResultVoUtil.error("不能上报，请核实信息");
    	}
    	// 新增审核记录数据
    	User user = ShiroUtil.getSubject();
    	AuditRecord auditRecord = new AuditRecord();
    	auditRecord.setAuditId(result.getId());
    	auditRecord.setDept(user.getDept());
    	auditRecord.setDeptName(user.getDept().getTitle());
    	auditRecord.setType(AuditEnum.RESULT.getValue());
    	auditRecord.setName(user.getRealname());
    	auditRecord.setAuditStatus(StatusEnum.AUDIT_WAIT.getValue());
    	auditRecordService.save(auditRecord);
    	// 更新数据
    	result.setAuditStatus(StatusEnum.AUDIT_WAIT.getValue());//待审核
    	resultService.report(result);
    	return ResultVoUtil.SAVE_SUCCESS;
    }
    
    /**
     * 跳转到添加/编辑页面
     */
    @GetMapping("/second/add")
    @RequiresPermissions("resultManager:result:second:add")
    public String toSecondEdit(Long id, Model model) {
    	Result result = new Result();
    	if(null!=id) {
    		 result =resultService.getById(id);
    	}
    	Foregoer foregoer=new Foregoer();
    	User user=ShiroUtil.getSubject();
    	foregoer.setDept(user.getDept());
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
        model.addAttribute("studioMap", modelstudioService.findAll());
        model.addAttribute("studioId", result.getStudioId()+","+result.getStudioType());
        //相关成员
        ResultMember resultMember=new ResultMember();
    	resultMember.setResult(result);
    	List<ResultMember> resultMemberlist=resultMemberService.getList(resultMember);
        model.addAttribute("resultMemberlist", resultMemberlist);
        return "/resultManager/secondResult/add";
    }
    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/second/add","/second/edit"})
    @RequiresPermissions({"resultManager:result:second:add","resultManager:result:second:edit"})
    @ResponseBody
    public ResultVo secondSave(@Validated ResultValid valid, Result result) {
    	User user = ShiroUtil.getSubject();
        // 复制保留无需修改的数据
        if (result.getId() != null) {
            Result beResult = resultService.getById(result.getId());
            EntityBeanUtil.copyProperties(beResult, result);
        }
        if(result.getDept()!=null) {
        	if(result.getDept().getLevel()!=3) {
        		return ResultVoUtil.error("二级只能添加三级,请核实后再试");
        	}
        }
        result.setAuditStatus(StatusEnum.FIRST_AUDIT_PASS.getValue()); //二级已通过
        result.setUnitType(user.getUnitType()); //登录用户单位类型
        result.setArchivesNo(ToolUtil.no(resultService.findCurretDayResultCount()+1));
        result.setAddLevel(LevelEnum.SECOND_LEVEL.getCode());//添加成果用户级别
     // 保存数据
        if(!StringUtils.isEmpty(result.getStudioIdAndType())&&result.getStudioIdAndType().contains(",")) {
        	String[] studioIdAndTypes=result.getStudioIdAndType().split(",");
        	result.setStudioId(Integer.parseInt(studioIdAndTypes[0]));
        	result.setStudioType(Integer.parseInt(studioIdAndTypes[1]));
        	if(result.getStudioType()==1) {
        		//查询工作室下面的带头人
        	    Modelstudio modelstudio=modelstudioService.getById(Long.valueOf(result.getStudioId()+""));
        	    result.setStudioLeader(modelstudio.getLingxianrenId());
        	}else {
        		//查询工作室下面的带头人---大师工作室就是带头人
        		result.setStudioLeader(Long.parseLong(studioIdAndTypes[0]));
        	}
        }
        result.setYear(DateUtil.getYear(result.getDeclareDate()));
        // 保存数据
        resultService.save(result); 
        toResultMeberSave(result);
        return ResultVoUtil.SAVE_SUCCESS;
    }
    
    //================================ 二级用户结束  ==============================================================//
    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("resultManager:result:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (resultService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
    
    /**
     * 跳转到专家选择页面
     */
   /* @GetMapping("/review/{id}")
    @RequiresPermissions("resultManager:result:frist:report")
    public ModelAndView  toReview(@PathVariable("id") Result result) {
    	User user=ShiroUtil.getSubject();
    	List<User> list=expertUserService.findAll(user);
    	 Set<User> authUsers = result.getUsers();
    	 
    	ModelAndView mav = new ModelAndView("/resultManager/fristResult/review");
    	mav.addObject("id", result.getId());
    	mav.addObject("list", list);
    	mav.addObject("authUsers", authUsers);
        return mav;
    }*/
    
    /**
     * 保存专家选择信息
     */
    /*@PostMapping("/review")
    @RequiresPermissions("resultManager:result:frist:report")
    @ResponseBody
    public ResultVo auth(
            @RequestParam(value = "id", required = true) Result result,
            @RequestParam(value = "userId", required = false) HashSet<User> users) {

    	if("5".equals(result.getAuditStatus())) {
    		HashSet<User> userList=users;
    		result.setUsers(userList);
    		result.setAuditStatus("7");
    		resultService.save(result);
    		return ResultVoUtil.SAVE_SUCCESS;
    	}else {
    		return ResultVoUtil.error("已提交专家评分,请勿重复提交");
    	}
    }*/
    
    /**
     * 一级提交专家
     */
    @GetMapping("/first/report/{id}")
    @RequiresPermissions("resultManager:result:frist:report")
    @ResponseBody
    public ResultVo firstReport(@PathVariable("id") Result result) {
    	if("5".equals(result.getAuditStatus())) {
    		User user=new User();
    		user.setReviewType(result.getResultType());
    		List<User> ulist=expertUserService.findAll(user);
    		if(ulist.size()<=0) {
    			return ResultVoUtil.error("无符合授权条件的专家，请核实后再试");
    		}
    		Set<User> userList= new HashSet<User>();
    		for(User userNew:ulist) {
    			userList.add(userNew);
    		}
    		result.setUsers(userList);
    		
    		// 新增审核记录数据
        	User user1 = ShiroUtil.getSubject();
        	AuditRecord auditRecord = new AuditRecord();
        	auditRecord.setAuditId(result.getId());
        	auditRecord.setDept(user1.getDept());
        	auditRecord.setDeptName(user1.getDept().getTitle());
        	auditRecord.setType(AuditEnum.RESULT.getValue());
        	auditRecord.setName(user1.getRealname());
        	auditRecord.setAuditStatus("7");
        	auditRecordService.save(auditRecord);
    		
    		result.setAuditStatus("7");
    		resultService.save(result);
    		return ResultVoUtil.SAVE_SUCCESS;
    	}else {
    		return ResultVoUtil.error("已提交专家评分,请勿重复提交");
    	}
    }
    
    /**
     * 一级批量提交专家
     */
    @RequestMapping("/first/moreReport")
    @RequiresPermissions("resultManager:result:first:moreReport")
    @ResponseBody
    public ResultVo firstMoreReport(@RequestParam(value = "ids", required = false) List<Result> list) {
    	int total=0;
    	int suc=0;
    	int fail=0;
    	if(list==null) {
    		return ResultVoUtil.error("请核实操作"); 
    	}
    	for(Result result:list) {
    		total++;
	    	try {
				if("5".equals(result.getAuditStatus())) {
					User user=new User();
					user.setReviewType(result.getResultType());
					List<User> ulist=expertUserService.findAll(user);
					if(ulist.size()<=0) {
						return ResultVoUtil.error("无符合授权条件的专家，请核实后再试");
					}
					Set<User> userList= new HashSet<User>();
					for(User userNew:ulist) {
						userList.add(userNew);
					}
					result.setUsers(userList);
					
					// 新增审核记录数据
					User user1 = ShiroUtil.getSubject();
					AuditRecord auditRecord = new AuditRecord();
					auditRecord.setAuditId(result.getId());
					auditRecord.setDept(user1.getDept());
					auditRecord.setDeptName(user1.getDept().getTitle());
					auditRecord.setType(AuditEnum.RESULT.getValue());
					auditRecord.setName(user1.getRealname());
					auditRecord.setAuditStatus("7");
					auditRecordService.save(auditRecord);
					
					result.setAuditStatus("7");
					resultService.save(result);
					suc++;
					//return ResultVoUtil.SAVE_SUCCESS;
				}else {
					//return ResultVoUtil.error("已提交专家评分,请勿重复提交");
					fail++;
				}
				
			} catch (Exception e) {
				fail++;
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
    	}
    	return ResultVoUtil.success("已提交专家成果总个数："+total+",成功提交专家"+suc+"个,失败个数："+fail+"个，请核实专家是否有或者是否授权过期");
    }
}