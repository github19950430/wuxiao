package com.pldk.admin.review.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

import com.pldk.admin.review.validator.ReviewTypeValid;
import com.pldk.common.enums.ResultEnum;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.exception.ResultException;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.review.domain.ReviewSet;
import com.pldk.modules.review.domain.ReviewType;
import com.pldk.modules.review.service.ReviewTypeService;

/**
 * 审核项管理
 * @author RXQ
 * @date 2019/07/11
 */
@Controller
@RequestMapping("/review/reviewType")
public class ReviewTypeController {

    @Autowired
    private ReviewTypeService reviewTypeService;
    

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("review:reviewType:index")
    public String index(Model model, ReviewType reviewType) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<ReviewType> example = Example.of(reviewType, matcher);
        Page<ReviewType> list = reviewTypeService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/review/reviewType/index";
    }

    /**
     * 列表页面
     */
    @GetMapping("/reviewTypeList")
    @ResponseBody
    public ResultVo reviewTypeList(ReviewType reviewType) {
    	 // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                 withMatcher("title", match -> match.contains());

         // 获取组织列表
         Example<ReviewType> example = Example.of(reviewType, matcher);
         Sort sort = new Sort(Sort.Direction.ASC, "create_date");
        List<ReviewType> list = reviewTypeService.findAll(example,sort);
        return ResultVoUtil.success(list);
    }
    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("review:reviewType:add")
    public String toAdd(Model model) {
    	 List<ReviewType> types = reviewTypeService.getByOneTypeListByPid();
         Map<Integer, String> sortMap = new TreeMap<>();
         for (int i = 1; i <= types.size(); i++) {
             sortMap.put(i, types.get(i - 1).getName());
         }
         model.addAttribute("types", sortMap);
        return "/review/reviewType/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("review:reviewType:edit")
    public String toEdit(@PathVariable("id") ReviewType reviewType, Model model) {
    	 List<ReviewType> types = reviewTypeService.getByOneTypeListByPid();
         Map<Integer, String> sortMap = new TreeMap<>();
         for (int i = 1; i <= types.size(); i++) {
             sortMap.put(i, types.get(i - 1).getName());
         }
         model.addAttribute("types", sortMap);
        model.addAttribute("reviewType", reviewType);
        return "/review/reviewType/add";
    }
    
    /**
     * 获取一级审核项的列表
     */
    @GetMapping("/oneTypeList")
    @RequiresPermissions({"review:reviewType:add", "review:reviewType:edit"})
    @ResponseBody
    public Map<Integer, String> oneTypeList(){
        // 本级排序菜单列表
        List<ReviewType> types = reviewTypeService.getByOneTypeListByPid();
        Map<Integer, String> sortMap = new TreeMap<>();
        for (int i = 1; i <= types.size(); i++) {
            sortMap.put(i, types.get(i - 1).getName());
        }
        return sortMap;
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"review:reviewType:add","review:reviewType:edit"})
    @ResponseBody
    public ResultVo save(@Validated ReviewTypeValid valid, ReviewType reviewType) {
    	 // 判断名称是否重复
        if (reviewTypeService.repeatByName(reviewType)) {
            throw new ResultException(ResultEnum.REVIEW_EXIST);
        }

        // 复制保留无需修改的数据
        if (reviewType.getId() != null) {
            ReviewType beReviewType = reviewTypeService.getById(reviewType.getId());
            EntityBeanUtil.copyProperties(beReviewType, reviewType);
        }
        if(reviewType.getLevel()==1&&reviewType.getParentId()==null) {
        	reviewType.setParentId((long) 0);
        	reviewType.setIsChild((byte)1);
        }

        // 保存数据
        reviewTypeService.save(reviewType);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("review:reviewType:detail")
    public String toDetail(@PathVariable("id") ReviewType reviewType, Model model) {
    	 List<ReviewType> types = reviewTypeService.getByOneTypeListByPid();
         Map<Integer, String> sortMap = new TreeMap<>();
         for (int i = 1; i <= types.size(); i++) {
             sortMap.put(i, types.get(i - 1).getName());
         }
         model.addAttribute("types", sortMap);
        model.addAttribute("reviewType",reviewType);
        return "/review/reviewType/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("review:reviewType:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (reviewTypeService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
    /**
     * 跳转到评分项设置添加页面
     */
    @GetMapping("/set/add")
    @RequiresPermissions("review:reviewType:add")
    public String toSetAdd(@RequestParam(value = "ids") ReviewType type, Model model) {
        model.addAttribute("reviewType", type);
        return "/review/reviewSet/add";
    }
    /**
     * 跳转到内容设置列表页面
     */
    @GetMapping("/setList/{id}")
    @RequiresPermissions("review:reviewType:detail")
    public String toUserList(@PathVariable("id") ReviewType type, Model model){
    	Set<ReviewSet> sets=type.getSets();
        model.addAttribute("list", sets);
        return "/review/reviewType/set_list";
    }
}