package com.pldk.admin.system.controller;

import com.pldk.admin.system.validator.DictValid;
import com.pldk.common.enums.ResultEnum;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.exception.ResultException;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.component.actionLog.action.SaveAction;
import com.pldk.component.actionLog.action.StatusAction;
import com.pldk.component.actionLog.annotation.ActionLog;
import com.pldk.component.actionLog.annotation.EntityParam;
import com.pldk.component.thymeleaf.utility.DictUtil;
import com.pldk.modules.system.domain.Dict;
import com.pldk.modules.system.service.DictService;

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
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:dict:index")
    public String index(Model model, Dict dict){

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("title", match -> match.contains());

        // 获取字典列表
        Example<Dict> example = Example.of(dict, matcher);
        Page<Dict> list = dictService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/dict/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("system:dict:add")
    public String toAdd(){
        return "/system/dict/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:dict:edit")
    public String toEdit(@PathVariable("id") Dict dict, Model model){
        model.addAttribute("dict", dict);
        return "/system/dict/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"system:dict:add","system:dict:edit"})
    @ResponseBody
    @ActionLog(name = "字典管理", message = "字典：${title}", action = SaveAction.class)
    public ResultVo save(@Validated DictValid valid, @EntityParam Dict dict){
        // 清除字典值两边空格
        dict.setValue(dict.getValue().trim());

        // 判断角色标识是否重复
        if (dictService.repeatByName(dict)) {
            throw new ResultException(ResultEnum.DICT_EXIST);
        }

        // 复制保留无需修改的数据
        if(dict.getId() != null){
            Dict beDict = dictService.getById(dict.getId());
            EntityBeanUtil.copyProperties(beDict, dict);
        }

        // 保存数据
        dictService.save(dict);
        if(dict.getId() != null){
            DictUtil.clearCache(dict.getName());
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:dict:detail")
    public String toDetail(@PathVariable("id") Dict dict, Model model){
        model.addAttribute("dict",dict);
        return "/system/dict/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("system:dict:status")
    @ResponseBody
    @ActionLog(name = "字典状态", action = StatusAction.class)
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids){
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (dictService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
    
    /**
     * 根据字典键与字典code查询字典值
     */
    @RequestMapping("/getDictValue")
    @ResponseBody
    public ResultVo getDictValue(String id, String name){
    	Dict dict=dictService.getByNameOk(name);
    	String value=getStringToMap(dict.getValue(),id);
        return ResultVoUtil.success("成功",value);
    }
    
    private static String getStringToMap(final String value,final String id) {
    	String[] mapArray = value.split(",");
    	String join=null;
    	for(int j=0 ;j<mapArray.length ;j++) {
    		String str = mapArray[j].replaceAll("\"", "");
    		String[] keyValue = str.split(":");
    		if(keyValue.length==2&&keyValue[0].equals(id)) {
    			join= keyValue[1];
    		}
    	}
    	return join;
    }
}
