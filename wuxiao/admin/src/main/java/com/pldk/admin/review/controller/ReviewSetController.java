package com.pldk.admin.review.controller;

import java.util.List;

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

import com.pldk.admin.review.validator.ReviewSetValid;
import com.pldk.common.enums.ResultEnum;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.exception.ResultException;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.review.domain.ReviewSet;
import com.pldk.modules.review.domain.ReviewType;
import com.pldk.modules.review.service.ReviewSetService;
import com.pldk.modules.review.service.ReviewTypeService;

/**
 * 评分内容管理
 * @author RXQ
 * @date 2019/07/12
 */
@Controller
@RequestMapping("/review/reviewSet")
public class ReviewSetController {

    @Autowired
    private ReviewSetService reviewSetService;
    
    @Autowired
    private ReviewTypeService reviewTypeService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("review:reviewType:index")
    public String index(Model model, ReviewSet reviewSet) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<ReviewSet> example = Example.of(reviewSet, matcher);
        Page<ReviewSet> list = reviewSetService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/review/reviewSet/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("review:reviewType:add")
    public String toAdd() {
        return "/review/reviewSet/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("review:reviewType:edit")
    public String toEdit(@PathVariable("id") ReviewSet reviewSet, Model model) {
    	ReviewType type=reviewTypeService.getById(reviewSet.getPid());
    	 model.addAttribute("reviewType", type);
        model.addAttribute("reviewSet", reviewSet);
        return "/review/reviewSet/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"review:reviewType:add","review:reviewType:edit"})
    @ResponseBody
    public ResultVo save(@Validated ReviewSetValid valid, ReviewSet reviewSet) {
    	
    	 // 判断名称是否重复
        if (reviewSetService.repeatByContent(reviewSet)) {
            throw new ResultException(ResultEnum.REVIEW_EXIST);
        }
        // 复制保留无需修改的数据
        if (reviewSet.getId() != null) {
            ReviewSet beReviewSet = reviewSetService.getById(reviewSet.getId());
            EntityBeanUtil.copyProperties(beReviewSet, reviewSet);
        }
        if(reviewSet.getPid()!=null&&!"".equals(reviewSet.getPid()+"")) {
    		ReviewType type=reviewTypeService.getById(reviewSet.getPid());
    		if(type!=null) {
    			reviewSet.setType(type);
    		}
    	}
        // 保存数据
        ReviewSet set= reviewSetService.save(reviewSet);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("review:reviewType:detail")
    public String toDetail(@PathVariable("id") ReviewSet reviewSet, Model model) {
    	ReviewType type=reviewTypeService.getById(reviewSet.getPid());
   	 	model.addAttribute("reviewType", type);
        model.addAttribute("reviewSet",reviewSet);
        return "/review/reviewSet/detail";
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
        if (reviewSetService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}