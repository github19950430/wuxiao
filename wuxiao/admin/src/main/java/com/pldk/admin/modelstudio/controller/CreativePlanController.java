package com.pldk.admin.modelstudio.controller;

import com.pldk.admin.config.UploadUrl;
import com.pldk.admin.modelstudio.validator.CreativePlanValid;
import com.pldk.admin.upload.FileController;
import com.pldk.admin.upload.ResultInfo;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.modelstudio.domain.CreativePlan;
import com.pldk.modules.modelstudio.domain.PlanAccessory;
import com.pldk.modules.modelstudio.domain.PlanImg;
import com.pldk.modules.modelstudio.domain.PlanVideo;
import com.pldk.modules.modelstudio.service.CreativePlanService;
import com.pldk.modules.modelstudio.service.PlanVideoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Controller
@RequestMapping("/modelstudio/creativePlan")
public class CreativePlanController {

    @Autowired
    private CreativePlanService creativePlanService;
    @Autowired
    private FileController fileController;
    @Autowired
    private UploadUrl uploadUrl;
    @Autowired
    private PlanVideoService planVideoService;
    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("modelstudio:creativePlan:add")
    public String toAdd() {
        return "/modelstudio/creativePlan/plan_add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("modelstudio:creativePlan:edit")
    public String toEdit(@PathVariable("id") CreativePlan creativePlan, Model model) {
        model.addAttribute("creativePlan", creativePlan);
        return "/modelstudio/creativePlan/plan_add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"modelstudio:creativePlan:add","modelstudio:creativePlan:edit"})
    @ResponseBody
    public ResultVo save(@Validated CreativePlanValid valid, CreativePlan creativePlan,
                         HttpServletRequest request,
                         @RequestParam(value = "videoid",required = false) Integer[] videoid,
                         @RequestParam(value = "video",required = false) MultipartFile[] video,
                         @RequestParam(value = "imgsid",required = false) Integer[] imgsid,
                         @RequestParam(value = "imgs",required = false)MultipartFile[] imgs,
                         @RequestParam(value = "filesid",required = false) Integer[] filesid,
                         @RequestParam(value = "files",required = false)MultipartFile[] files) {
        // 复制保留无需修改的数据
        if (creativePlan.getId() != null) {
            CreativePlan beCreativePlan = creativePlanService.getById(creativePlan.getId());
            EntityBeanUtil.copyProperties(beCreativePlan, creativePlan);


            // 保存数据
            creativePlanService.save(creativePlan);
        }else {
            // 保存数据
            creativePlanService.save(creativePlan);
            Long id = creativePlan.getId();

            if (video != null){
                PlanVideo planVideo = new PlanVideo();
                for (MultipartFile x:video) {
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getCreativePlanVideoLinux());
                    String data = (String)resultInfo.getData();
                    planVideo.setCreativePlanId(id);
                    planVideo.setVideoUrl(uploadUrl.getCreativePlanVideoMysql() + data);
                    planVideoService.save(planVideo);
                }
            }
            if (imgs != null){
                PlanImg planImg = new PlanImg();
                for (MultipartFile x:imgs) {
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getCreativePlanImgsLinux());
                    String data = (String)resultInfo.getData();
                    planImg.setCreativePlanId(id);
                    planImg.setImgUrl(uploadUrl.getCreativePlanImgsMysql() + data);
                }
            }
            if (files != null){
                PlanAccessory planAccessory = new PlanAccessory();
                for (MultipartFile x:files) {
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getCreativePlanFilesLinux());
                    String data = (String)resultInfo.getData();
                    String name = x.getOriginalFilename();
                    planAccessory.setAccessory_name(name);
                    planAccessory.setCreativePlanId(id);
                    planAccessory.setFileUrl(uploadUrl.getCreativePlanFilesMysql() + data);
                }
            }
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("modelstudio:creativePlan:detail")
    public String toDetail(@PathVariable("id") CreativePlan creativePlan, Model model) {
        model.addAttribute("creativePlan",creativePlan);
        return "/modelstudio/creativePlan/plan_detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("modelstudio:creativePlan:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (creativePlanService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}