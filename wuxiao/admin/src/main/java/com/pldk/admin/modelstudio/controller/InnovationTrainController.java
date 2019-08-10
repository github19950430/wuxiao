package com.pldk.admin.modelstudio.controller;

import com.pldk.admin.modelstudio.validator.InnovationTrainValid;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.modelstudio.domain.InnovationTrain;
import com.pldk.modules.modelstudio.service.InnovationTrainService;
import com.pldk.modules.system.domain.Upload;
import com.pldk.modules.system.service.UploadService;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2019/07/17
 */
@Controller
@RequestMapping("/modelstudio/innovationTrain")
public class InnovationTrainController {

    @Autowired
    private InnovationTrainService innovationTrainService;
    @Autowired
    private UploadService uploadService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("modelstudio:innovationTrain:index")
    public String index(Long baseId,Model model, InnovationTrain innovationTrain) {
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("trainName", match -> match.contains());
        innovationTrain.setBaseId(baseId);

        // 获取数据列表
        Example<InnovationTrain> example = Example.of(innovationTrain, matcher);
        List<InnovationTrain> list = innovationTrainService.getAll(example);
        list.forEach(inov->{
            if(StringUtils.isNotEmpty(inov.getTrainImg())){
                String str[] = inov.getTrainImg().split(",");
                Upload upload = uploadService.getById(Long.parseLong(str[0]));
                if(null!=upload){
                    inov.setTrainImg(upload.getPath());
                }

            }
        });
        // 封装数据
        model.addAttribute("list", list);
        model.addAttribute("baseId",baseId);
        model.addAttribute("trainName",innovationTrain.getTrainName());
        return "/modelstudio/innovationTrain/index1";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("modelstudio:innovationTrain:add")
    public String toAdd(Model model,String baseId) {
        model.addAttribute("baseId",baseId);
        return "/modelstudio/innovationTrain/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("modelstudio:innovationTrain:edit")
    public String toEdit(@PathVariable("id") InnovationTrain innovationTrain, Model model,Long baseId) {
        model.addAttribute("innovationTrain", innovationTrain);
        List<Upload> resultImg=uploadService.getListUpload(innovationTrain.getTrainImg());
        List<Upload> resultFile=uploadService.getListUpload(innovationTrain.getFileId());
        resultFile.forEach(upload -> {
            if(upload.getMime().indexOf("image")>-1){
                upload.setMime("img");
            }
        });
        model.addAttribute("baseId",baseId);
        model.addAttribute("resultImg", resultImg);
        model.addAttribute("resultFile", resultFile);
        return "/modelstudio/innovationTrain/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"modelstudio:innovationTrain:add","modelstudio:innovationTrain:edit"})
    @ResponseBody
    public ResultVo save(@Validated InnovationTrainValid valid, InnovationTrain innovationTrain) {
        // 复制保留无需修改的数据
        if (innovationTrain.getId() != null) {
            InnovationTrain beInnovationTrain = innovationTrainService.getById(innovationTrain.getId());
            EntityBeanUtil.copyProperties(beInnovationTrain, innovationTrain);
        }

        // 保存数据
        innovationTrainService.save(innovationTrain);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("modelstudio:innovationTrain:detail")
    public String toDetail(@PathVariable("id") InnovationTrain innovationTrain, Model model) {
        List<Upload> resultImg=uploadService.getListUpload(innovationTrain.getTrainImg());
        model.addAttribute("resultImg", resultImg);
        List<Upload> resultFile=uploadService.getListUpload(innovationTrain.getFileId());
        resultFile.forEach(upload -> {
            if(upload.getMime().indexOf("image")>-1){
                upload.setMime("img");
            }
        });
        innovationTrain.setTrainBdate(innovationTrain.getTrainBdate()+" - "+innovationTrain.getTrainEdate());
        model.addAttribute("resultFile", resultFile);
        model.addAttribute("innovationTrain",innovationTrain);
        return "/modelstudio/innovationTrain/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("modelstudio:innovationTrain:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (innovationTrainService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}