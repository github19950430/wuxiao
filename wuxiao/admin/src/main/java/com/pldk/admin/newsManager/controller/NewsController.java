package com.pldk.admin.newsManager.controller;

import com.pldk.admin.newsManager.validator.NewsValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.newsManager.domain.News;
import com.pldk.modules.newsManager.service.NewsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/06/27
 */
@Controller
@RequestMapping("/newsManager/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("newsManager:news:index")
    public String index(Model model, News news) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<News> example = Example.of(news, matcher);
        Page<News> list = newsService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/newsManager/news/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("newsManager:news:add")
    public String toAdd() {
        return "/newsManager/news/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("newsManager:news:edit")
    public String toEdit(@PathVariable("id") News news, Model model) {
        model.addAttribute("news", news);
        return "/newsManager/news/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"newsManager:news:add","newsManager:news:edit"})
    @ResponseBody
    public ResultVo save(@Validated NewsValid valid, News news) {
        // 复制保留无需修改的数据
        if (news.getId() != null) {
            News beNews = newsService.getById(news.getId());
            EntityBeanUtil.copyProperties(beNews, news);
            news.setReleaseTime(beNews.getReleaseTime());
        }

        // 保存数据
        newsService.save(news);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("newsManager:news:detail")
    public String toDetail(@PathVariable("id") News news, Model model) {
        model.addAttribute("news",news);
        return "/newsManager/news/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("newsManager:news:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (newsService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}