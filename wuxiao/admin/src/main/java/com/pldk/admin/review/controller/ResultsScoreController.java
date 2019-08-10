package com.pldk.admin.review.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pldk.common.constant.StatusConst;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.enums.UnitTypeEnum;
import com.pldk.common.utils.BigDecimalUtils;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.component.shiro.ShiroUtil;
import com.pldk.modules.resultManager.domain.Result;
import com.pldk.modules.resultManager.service.ResultService;
import com.pldk.modules.review.domain.ReviewScore;
import com.pldk.modules.review.domain.ReviewScoreDetail;
import com.pldk.modules.review.domain.ReviewSet;
import com.pldk.modules.review.domain.ReviewType;
import com.pldk.modules.review.domain.UserReviewSet;
import com.pldk.modules.review.service.ReviewScoreService;
import com.pldk.modules.review.service.ReviewSetService;
import com.pldk.modules.review.service.ReviewTypeService;
import com.pldk.modules.review.service.UserReviewSetService;
import com.pldk.modules.review.vo.ReviewScoreDetailVo;
import com.pldk.modules.review.vo.ReviewScoreVo;
import com.pldk.modules.review.vo.ReviewSetVo;
import com.pldk.modules.system.domain.User;

/**
 * 专家评分管理
 * @author RXQ
 *
 */
@Controller
@RequestMapping("/review/score")
public class ResultsScoreController {

	 @Autowired
	 private ResultService resultService;
	 
	  @Autowired
	  private ReviewTypeService reviewTypeService;
	  
	  @Autowired
	  private UserReviewSetService userReviewSetService;
	  
	  @Autowired
	  private ReviewSetService reviewSetService;
	  
	  @Autowired
	  private ReviewScoreService reviewScoreService;
	 /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("review:score:index")
    public String index(Model model, Result result) {
    	result.setUnitType(UnitTypeEnum.LABORUN_UNIT_TYPE.getCode());//查询单位类型为工会的数据
    	User user=ShiroUtil.getSubject();
    	List<Long> resuls=resultService.findAllResultByUserId(user.getId());
        // 获取数据列表
    	Page<Result> list = resultService.getReviewPageList(result,resuls);
    	List<Result> resultList=list.getContent();
        for(Result resultNew:resultList) {
        	List<Map<String,Object>> userReviewSetList=userReviewSetService.getList(user.getId(), resultNew.getId());
        	if(userReviewSetList.size()>0) {
        		resultNew.setAuditStatus(StatusEnum.SCORE_OK.getValue());
        		resultNew.setScore(Double.parseDouble((String)userReviewSetList.get(0).get("score")));
        	}else {
        		resultNew.setScore(0);
        	}
        }
        List<Result> listInfo= new ArrayList<>();
        for(Result res :resultList) {
        	ReviewScore reviewScore=new ReviewScore();
        	reviewScore.setUserId(user.getId());
        	reviewScore.setResultId(res.getId());
        	ReviewScoreVo score=reviewScoreService.getReviewScoreDetail(reviewScore);
        	if(score!=null) {
        		res.setState((byte) 1);
        	}else {
        		res.setState((byte) 0);
        	}
        	listInfo.add(res);
        	
        }
        // 封装数据
        model.addAttribute("list", listInfo);
        model.addAttribute("page", list);
        return "/review/reviewScore/index";
    }
    
    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("review:score:detail")
    public String toDetail(@PathVariable("id") Result result, Model model) {
        model.addAttribute("result",result);
        return "/review/reviewScore/detail";
    }
    /**
     * 跳转到评分页面
     */
    @GetMapping("/dynamic/score/{id}")
    @RequiresPermissions("review:score:score")
    public String toDynamicScore(@PathVariable("id") Result result, Model model) {
    	User user=ShiroUtil.getSubject();
    	ReviewType reviewType=new ReviewType();
    	Example<ReviewType> example = Example.of(reviewType);
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<ReviewType> typeList = reviewTypeService.findAll(example,sort);
        model.addAttribute("user",user);
        model.addAttribute("result",result);
        model.addAttribute("typeList",typeList);
    	return "/review/reviewScore/scoreDynamic";
    }
    /**
     * 详情页面
     * @param result
     * @param model
     * @return
     */
    @GetMapping("/dynamic/scoreDetail/{id}")
    @RequiresPermissions("review:score:score")
    public String toDynamicScorescoreDetail(@PathVariable("id") Result result, Model model) {
    	User user=ShiroUtil.getSubject();
    	ReviewType reviewType=new ReviewType();
    	Example<ReviewType> example = Example.of(reviewType);
    	Sort sort = new Sort(Sort.Direction.ASC, "createDate");
    	List<ReviewType> typeList = reviewTypeService.findAll(example,sort);
    	ReviewScore reviewScore=new ReviewScore();
    	reviewScore.setUserId(user.getId());
    	reviewScore.setResultId(result.getId());
    	ReviewScoreVo score=reviewScoreService.getReviewScoreDetail(reviewScore);
    	model.addAttribute("user",user);
    	model.addAttribute("score",score);
    	model.addAttribute("result",result);
    	model.addAttribute("typeList",typeList);
    	return "/review/reviewScore/scoreDynamicDetail";
    }
    /**
     * 打分项详情
     */
    @RequestMapping("/dynamic/getDetailById")
    @RequiresPermissions("review:score:score")
    @ResponseBody
    public ResultVo getDetailById(ReviewScoreDetail detail){
    	ReviewScoreDetailVo reviewScoreDetail =reviewScoreService.getDetailById(detail);
        return ResultVoUtil.success("成功",reviewScoreDetail);
    }
    
    /**
     * 根据查询所有审核项
     */
    @RequestMapping("/dynamic/getReviewTypeList")
    @RequiresPermissions("review:score:score")
    @ResponseBody
    public ResultVo getDynamicReviewTypeList( @RequestParam(value = "id", required = false)Long id){
    	ReviewType reviewType=new ReviewType();
    	if(id!=null) {
    		reviewType.setParentId(id);
    	}else {
    		reviewType.setParentId((long)0);
    	}
    	Example<ReviewType> example = Example.of(reviewType);
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<ReviewType> typeList = reviewTypeService.findAll(example,sort);
        return ResultVoUtil.success("成功",typeList);
    }
    
    /**
     * 查询所有查询内容
     * @param id
     * @return
     */
    @RequestMapping("/dynamic/getReviewSetList")
    @RequiresPermissions("review:score:score")
    @ResponseBody
    public ResultVo getDynamicReviewSetList(Long id){
    	List<ReviewSet> setList=reviewSetService.findAllByPid(id);
        return ResultVoUtil.success("成功",setList);
    }
    
    /**
     * 保存添加
     */
    @PostMapping({"/dynamic/add"})
    @RequiresPermissions({"review:score:score"})
    @ResponseBody
    public ResultVo saveDynamic(String  jsonStr) {
    	ReviewScore score=new ReviewScore();
    	List<ReviewScoreDetail> detailList=new ArrayList<>();
    	try {
    		JSONArray jsonArray = JSONArray.parseArray(jsonStr);
    		if(jsonArray.size()>0) {
    			for(int i=0;i<jsonArray.size();i++) {
    				ReviewScoreDetail detail=new ReviewScoreDetail();
    				JSONObject json = jsonArray.getJSONObject(i);
    				String name = json.getString("name");
    				String value = json.getString("value");
    				if(value==null||"".equals(value)) {
    					return ResultVoUtil.error("评分未完成或评审意见不能为空!");
    				}else {
    					if("userId".equals(name)) {
    						score.setUserId(Long.parseLong(value));
    					}
    					else if("resultId".equals(name)) {
    						score.setResultId(Long.parseLong(value));
    					}
    					else if("summation".equals(name)) {
    						score.setSummation(Integer.parseInt(value));
    					}
    					else if("reviewComments".equals(name)) {
    						score.setReviewComments(value);
    					}else {
    						ReviewSetVo set=reviewScoreService.getReviewSetById(Long.parseLong(name));
    						if(set!=null&&set.getWiLowest()!=null) {
    							if(set.getWiLowest()<=Integer.parseInt(value)&&Integer.parseInt(value)<=set.getWlHighest()) {
    								detail.setScore(Integer.parseInt(value));
    								detail.setTypeId(Long.parseLong(name));
    								detailList.add(detail);
    							}else {
    								return ResultVoUtil.error(set.getContent()+"必须大于等于"+set.getWiLowest()+"小于等于"+set.getWlHighest());
    							}
    						}else {
    							 set=reviewScoreService.getReviewSetTypeById(Long.parseLong(name));
    							 if(set.getWiLowest()<=Integer.parseInt(value)&&Integer.parseInt(value)<=set.getWlHighest()) {
     								detail.setScore(Integer.parseInt(value));
     								detail.setTypeId(Long.parseLong(name));
     								detailList.add(detail);
     							}else {
     								return ResultVoUtil.error(set.getContent()+"必须大于等于"+set.getWiLowest()+"小于等于"+set.getWlHighest());
     							}
    						}
    					}
    				}
    			}
    		}
    		reviewScoreService.save(score,detailList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return ResultVoUtil.error("服务器有误");
		}
        return ResultVoUtil.SAVE_SUCCESS;
    }
    
    /**
     * 列表页面
     */
    @GetMapping("/score/index")
    @RequiresPermissions("review:score:index")
    public String scoreIndex(Model model, Result result) {
        // 获取数据列表
    	Page<Result> list = resultService.getReviewPageList(result);
    	
    	List<Result> resultList=list.getContent();
    	for(Result resultNew:resultList) {
    		List<Map<String,Object>> maplist=userReviewSetService.getList(resultNew.getId());
    		double totalScore=0;
    		int i=0;
    		for(Map<String,Object> map:maplist) { 
    			double score=Double.parseDouble((String)map.get("score"));
    			totalScore+=score; 
    			i++;
    		}
    		if(i>0) {
    		    resultNew.setScore(totalScore/i);
    		}else {
    			resultNew.setScore(0);
    		}
    	}
        // 封装数据
        model.addAttribute("list", resultList);
        model.addAttribute("page", list);
        return "/review/reviewScore/collect";
    }
    /**
     * 跳转到详细页面
     */
    @GetMapping("/score/scoreDetail/{id}")
    @RequiresPermissions("review:score:score:scoreDetail")
    public String toScoreDetail(@PathVariable("id") Result result,Model model) {
    	User user=ShiroUtil.getSubject();
    	ReviewType reviewType=new ReviewType();
    	Example<ReviewType> example = Example.of(reviewType);
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<ReviewType> typeList = reviewTypeService.findAll(example,sort);
        model.addAttribute("user",user);
        model.addAttribute("result",result);
        model.addAttribute("typeList",typeList);
        
        double score1=0;
        double score2=0;
        double score3=0;
        double score4=0;
        double score5=0;
        int i=0;
        if(null!=user.getIsExpert()&&1==user.getIsExpert()) {
        	List<Map<String,Object>> scoreMapList=new ArrayList<Map<String,Object>>();
	        List<Map<String,Object>> list=userReviewSetService.getList(user.getId(), result.getId());
	        if(list.size()==1){
	        	Map<String,Object> userReviewSet=list.get(0);
	        	Map<String,Object> scoreMap=(Map<String, Object>) JSON.parseObject((String)userReviewSet.get("review_set_json_str"));
	        	scoreMap.put("realname", (String)userReviewSet.get("realname"));
	        	scoreMap.put("result_name", (String)userReviewSet.get("result_name"));
	        	scoreMapList.add(scoreMap); 
	        	model.addAttribute("scoreMapList",scoreMapList);
	        	
	        	score1=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid1"))?"0":(String)scoreMap.get("nid1"));
	            score2=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid2"))?"0":(String)scoreMap.get("nid2"));
	            score3=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid3"))?"0":(String)scoreMap.get("nid3"));
	            score4=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid4"))?"0":(String)scoreMap.get("nid4"));
	            score5=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid5"))?"0":(String)scoreMap.get("nid5"));
	            i++;
	        }
        }else {
        	List<Map<String,Object>> scoreMapList=new ArrayList<Map<String,Object>>();
        	List<Map<String,Object>> list=userReviewSetService.getList(result.getId());
	        for(Map<String,Object> userReviewSetNew:list){
	        	Map<String,Object> scoreMap=(Map<String, Object>) JSON.parseObject((String)userReviewSetNew.get("review_set_json_str"));
	        	scoreMap.put("realname", (String)userReviewSetNew.get("realname"));
	        	scoreMap.put("result_name", (String)userReviewSetNew.get("result_name"));
	        	scoreMapList.add(scoreMap); 
	        	
	        	
	        	//综合得分
	        	double nid1=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid1"))?"0":(String)scoreMap.get("nid1"));
                double nid2=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid2"))?"0":(String)scoreMap.get("nid2"));
                double nid3=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid3"))?"0":(String)scoreMap.get("nid3"));
                double nid4=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid4"))?"0":(String)scoreMap.get("nid4"));
                double nid5=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid5"))?"0":(String)scoreMap.get("nid5"));
                
                score1+=nid1;
                score2+=nid2;
                score3+=nid3;
                score4+=nid4;
                score5+=nid5;
                i++;
	        }
	        model.addAttribute("scoreMapList",scoreMapList);
        }
        Map<String,Object> zhScore=new HashMap<String,Object>();
        try {
			zhScore.put("score1", BigDecimalUtils.round(score1/i, 2, BigDecimal.ROUND_DOWN));
			zhScore.put("score2", BigDecimalUtils.round(score2/i, 2, BigDecimal.ROUND_DOWN));
			zhScore.put("score3", BigDecimalUtils.round(score3/i, 2, BigDecimal.ROUND_DOWN));
			zhScore.put("score4", BigDecimalUtils.round(score4/i, 2, BigDecimal.ROUND_DOWN)); 
			zhScore.put("score5", BigDecimalUtils.round(score5/i, 2, BigDecimal.ROUND_DOWN));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			zhScore.put("score1", 0);
			zhScore.put("score2", 0);
			zhScore.put("score3", 0);
			zhScore.put("score4", 0); 
			zhScore.put("score5", 0);
		}
        model.addAttribute("zhScore",zhScore);
        return "/review/reviewScore/scoreDetail"; 
    }
    
    /**
     * 跳转到详细页面
     */
    @GetMapping("/score/scoreModifyDetail/{id}")
    @RequiresPermissions("review:score:score:scoreModifyDetail")
    public String toScoreModifyDetail(@PathVariable("id") Result result,Model model) {
    	User user=ShiroUtil.getSubject();
    	ReviewType reviewType=new ReviewType();
    	Example<ReviewType> example = Example.of(reviewType);
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<ReviewType> typeList = reviewTypeService.findAll(example,sort);
        model.addAttribute("user",user);
        model.addAttribute("result",result);
        model.addAttribute("typeList",typeList);
        
        double score1=0;
        double score2=0;
        double score3=0;
        double score4=0;
        double score5=0;
        int i=0;
        if(null!=user.getIsExpert()&&1==user.getIsExpert()) {
        	List<Map<String,Object>> scoreMapList=new ArrayList<Map<String,Object>>();
	        List<Map<String,Object>> list=userReviewSetService.getList(user.getId(), result.getId());
	        if(list.size()==1){
	        	Map<String,Object> userReviewSet=list.get(0);
	        	Map<String,Object> scoreMap=(Map<String, Object>) JSON.parseObject((String)userReviewSet.get("review_set_json_str"));
	        	scoreMap.put("realname", (String)userReviewSet.get("realname"));
	        	scoreMap.put("result_name", (String)userReviewSet.get("result_name"));
	        	scoreMapList.add(scoreMap); 
	        	model.addAttribute("scoreMapList",scoreMapList);
	        	model.addAttribute("item",scoreMapList.get(0));
	        	score1=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid1"))?"0":(String)scoreMap.get("nid1"));
	            score2=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid2"))?"0":(String)scoreMap.get("nid2"));
	            score3=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid3"))?"0":(String)scoreMap.get("nid3"));
	            score4=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid4"))?"0":(String)scoreMap.get("nid4"));
	            score5=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid5"))?"0":(String)scoreMap.get("nid5"));
	            i++;
	        }
        }else {
        	List<Map<String,Object>> scoreMapList=new ArrayList<Map<String,Object>>();
        	List<Map<String,Object>> list=userReviewSetService.getList(result.getId());
	        for(Map<String,Object> userReviewSetNew:list){
	        	Map<String,Object> scoreMap=(Map<String, Object>) JSON.parseObject((String)userReviewSetNew.get("review_set_json_str"));
	        	scoreMap.put("realname", (String)userReviewSetNew.get("realname"));
	        	scoreMap.put("result_name", (String)userReviewSetNew.get("result_name"));
	        	scoreMapList.add(scoreMap); 
	        	
	        	
	        	//综合得分
	        	double nid1=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid1"))?"0":(String)scoreMap.get("nid1"));
                double nid2=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid2"))?"0":(String)scoreMap.get("nid2"));
                double nid3=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid3"))?"0":(String)scoreMap.get("nid3"));
                double nid4=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid4"))?"0":(String)scoreMap.get("nid4"));
                double nid5=Double.parseDouble(StringUtils.isEmpty((String)scoreMap.get("nid5"))?"0":(String)scoreMap.get("nid5"));
                
                score1+=nid1;
                score2+=nid2;
                score3+=nid3;
                score4+=nid4;
                score5+=nid5;
                i++;
	        }
	        model.addAttribute("scoreMapList",scoreMapList);
        }
        Map<String,Object> zhScore=new HashMap<String,Object>();
        try {
			zhScore.put("score1", BigDecimalUtils.round(score1/i, 2, BigDecimal.ROUND_DOWN));
			zhScore.put("score2", BigDecimalUtils.round(score2/i, 2, BigDecimal.ROUND_DOWN));
			zhScore.put("score3", BigDecimalUtils.round(score3/i, 2, BigDecimal.ROUND_DOWN));
			zhScore.put("score4", BigDecimalUtils.round(score4/i, 2, BigDecimal.ROUND_DOWN)); 
			zhScore.put("score5", BigDecimalUtils.round(score5/i, 2, BigDecimal.ROUND_DOWN));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			zhScore.put("score1", 0);
			zhScore.put("score2", 0);
			zhScore.put("score3", 0);
			zhScore.put("score4", 0); 
			zhScore.put("score5", 0);
		}
        model.addAttribute("zhScore",zhScore);
        return "/review/reviewScore/scoreModify"; 
    }
    /**
     * 跳转到评分页面
     */
    @SuppressWarnings("unchecked")
	@GetMapping("/score/{id}")
    @RequiresPermissions("review:score:score")
    public String toScore(@PathVariable("id") Result result, Model model) {
    	User user=ShiroUtil.getSubject();
    	ReviewType reviewType=new ReviewType();
    	Example<ReviewType> example = Example.of(reviewType);
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<ReviewType> typeList = reviewTypeService.findAll(example,sort);
        model.addAttribute("user",user);
        model.addAttribute("result",result);
        model.addAttribute("typeList",typeList);
        
        /*if(user.getIsExpert()==1) {
	        List<Map<String,Object>> list=userReviewSetService.getList(user.getId(), result.getId());
	        if(list.size()==1){
	        	Map<String,Object> userReviewSet=list.get(0);
	        	Map<String,String> scoreMap= (Map<String,String>) JSONArray.parseArray((String)userReviewSet.get("reviewSetJsonStr"));
	        	scoreMap.put("realname", (String)userReviewSet.get("realname"));
	        	scoreMap.put("result_name", (String)userReviewSet.get("result_name"));
	        	model.addAttribute("userReviewSet",scoreMap);
	        }
        }else {
        	List<Map<String,String>> scoreMapList=new ArrayList<Map<String,String>>();
        	List<Map<String,Object>> list=userReviewSetService.getList(result.getId());
	        for(Map<String,Object> userReviewSetNew:list){
	        	Map<String,String> scoreMap= (Map<String,String>) JSONArray.parseArray((String)userReviewSetNew.get("reviewSetJsonStr"));
	        	scoreMap.put("realname", (String)userReviewSetNew.get("realname"));
	        	scoreMap.put("result_name", (String)userReviewSetNew.get("result_name"));
	        	scoreMapList.add(scoreMap);
	        }
	        model.addAttribute("scoreMapList",scoreMapList);
        }*/
    	return "/review/reviewScore/score";
    }
    
    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/score/add","/score/edit"})
    @RequiresPermissions({"review:score:score:add","review:score:score:edit"})
    @ResponseBody
    public ResultVo save(HttpServletRequest request) {
    	
    	Map<String, String> param=new HashMap<String, String>();
    	Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> para : params.entrySet()) {
        	String key=para.getKey();
        	String[] values=para.getValue();
        	String value="";
        	if(values==null) {
        		value="";
        	}
        	else if(values.length>1) {
	        	 for (int i = 0; i < values.length; i++) { //用于请求参数中有多个相同名称
	        		  value = values[i] + ",";
	        	 }
        	}else {
        		value=values[0];
        	}
        	param.put(key, value);
        }
        Long userId=Long.parseLong(param.get("userId"));
        Long resultId=Long.parseLong(param.get("resultId"));
        Result result=resultService.getById(resultId);
        User user=ShiroUtil.getSubject();
        Set<User> users=result.getUsers();
        List<Map<String,Object>> resultList=userReviewSetService.getList(resultId);
        for(User exportUser:users) {
        	if(exportUser.getId()==user.getId()) {
        		UserReviewSet userReviewSet=new UserReviewSet();
        		List<Map<String,Object>> list=userReviewSetService.getList(userId, resultId);
                if(list.size()>0){
                	//return ResultVoUtil.error("该专家已经评过分");
                	userReviewSet.setId(Long.valueOf(list.get(0).get("id")+""));
                }
                userReviewSet.setMessage(param.get("message"));
                userReviewSet.setUserId(userId);
                userReviewSet.setReviewSetJsonStr(JSONObject.toJSONString(param));
                userReviewSet.setResultId(resultId);
                double nid1=Double.parseDouble(StringUtils.isEmpty((String)param.get("nid1"))?"0":param.get("nid1"));
                double nid2=Double.parseDouble(StringUtils.isEmpty((String)param.get("nid2"))?"0":param.get("nid2"));
                double nid3=Double.parseDouble(StringUtils.isEmpty((String)param.get("nid3"))?"0":param.get("nid3"));
                double nid4=Double.parseDouble(StringUtils.isEmpty((String)param.get("nid4"))?"0":param.get("nid4"));
                double nid5=Double.parseDouble(StringUtils.isEmpty((String)param.get("nid5"))?"0":param.get("nid5"));
                if(StringUtils.isEmpty(param.get("message"))) {
                	return ResultVoUtil.error("评审内容不能为空"); 
                }
                if(nid1<5||nid1>20) {
                	return ResultVoUtil.error("创新思路的分值不在范围内"); 
                }
                if(nid2<10||nid2>30) {
                	return ResultVoUtil.error("科技含量的分值不在范围内"); 
                }
                if(nid3>0&&nid4>0) {
                	return ResultVoUtil.error("创造经济效益与创造社会效益两项只能选其一"); 
                }
                if(nid3>0&&nid4==0) {
                	if(nid3<5||nid3>25) {
                	   return ResultVoUtil.error("创造经济效益的分值不在范围内"); 
                	}
                }
            	if(nid4>0&&nid3==0) {
                	if(nid4<10||nid4>30) {
                	   return ResultVoUtil.error("创造社会效益的分值不在范围内"); 
                	}
                }	
                if(nid5<5||nid5>25) {
                	return ResultVoUtil.error("发挥作用的分值不在范围内"); 
                }
                double score=nid1+nid2+nid3+nid4+nid5;
                userReviewSet.setScore(score);
                userReviewSet.setRealname(user.getRealname());
                userReviewSet.setResultName(result.getResultName());
                
                if(StringUtils.isEmpty(userReviewSet.getId())||0==userReviewSet.getId()) {
	                if(users.size()==resultList.size()+1) {
	                	result.setAuditStatus(StatusEnum.SCORE_OK.getValue());
	                    resultService.save(result);
	                }
                }
                
                userReviewSetService.save(userReviewSet);
                return ResultVoUtil.SAVE_SUCCESS;
        	}
        }
        return ResultVoUtil.error("该专家不能对该成果进行评分"); 
        
        
    }
    
    
    /**
     * 根据字典键与字典code查询字典值
     */
    @RequestMapping("/getReviewTypeList")
    @ResponseBody
    public ResultVo getReviewTypeList(){
    	ReviewType reviewType=new ReviewType();
    	Example<ReviewType> example = Example.of(reviewType);
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<ReviewType> typeList = reviewTypeService.findAll(example,sort);
        return ResultVoUtil.success("成功",typeList);
    }
}
