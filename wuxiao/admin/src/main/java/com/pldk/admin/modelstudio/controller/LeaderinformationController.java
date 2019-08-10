package com.pldk.admin.modelstudio.controller;

import com.alibaba.fastjson.JSON;
import com.pldk.admin.config.UploadUrl;
import com.pldk.admin.config.Util;
import com.pldk.admin.modelstudio.validator.LeaderinformationValid;
import com.pldk.admin.upload.FileController;
import com.pldk.admin.upload.ResultInfo;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.modules.modelstudio.domain.Keymember;
import com.pldk.modules.modelstudio.domain.LeaderAccessory;
import com.pldk.modules.modelstudio.domain.Leaderinformation;
import com.pldk.modules.modelstudio.service.KeymemberService;
import com.pldk.modules.modelstudio.service.LeaderAccessoryService;
import com.pldk.modules.modelstudio.service.LeaderinformationService;
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
 * @author Pldk   带头人
 * @date 2019/07/11
 */
@Controller
@RequestMapping("/modelstudio/leaderinformation")
public class LeaderinformationController {

    @Autowired
    private LeaderinformationService leaderinformationService;
    @Autowired
    private KeymemberService keymemberService;  //骨干成员
    @Autowired
    private LeaderAccessoryService leaderAccessoryService;
    @Autowired
    private FileController fileController;
    @Autowired
    private UploadUrl uploadUrl;
    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("modelstudio:leaderinformation:add")
    public String toAdd() {
        return "/modelstudio/leaderinformation/leaderin_add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("modelstudio:leaderinformation:edit")
    public String toEdit(@PathVariable("id") Leaderinformation leaderinformation, Model model) {
        model.addAttribute("leaderinformation", leaderinformation);
        return "/modelstudio/leaderinformation/leaderin_add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"modelstudio:leaderinformation:add","modelstudio:leaderinformation:edit"})
    @ResponseBody
    public ResultVo save(@Validated LeaderinformationValid valid, Leaderinformation leaderinformation,
                         @RequestParam(value = "head",required = false) MultipartFile head,
                         HttpServletRequest request,
                         @RequestParam(value = "keymemberId",required = false) Long[] keymemberId,
                         @RequestParam(value = "Keymember",required = false) String keymember,//骨干成员字符串数组
                         @RequestParam(value = "files",required = false) MultipartFile[] files,
                         @RequestParam(value = "fileid",required = false) Long[] fileid) {
        // 复制保留无需修改的数据
        if (leaderinformation.getId() != null) {
            Leaderinformation beLeaderinformation = leaderinformationService.getById(leaderinformation.getId());
            EntityBeanUtil.copyProperties(beLeaderinformation, leaderinformation);
            if (head != null){
                ResultInfo resultInfo = fileController.uploadImages(head, request,uploadUrl.getLeaderUrlLinux());
                String data = (String)resultInfo.getData();
                leaderinformation.setHead_portrait(uploadUrl.getLeaderUrlMysql() + data);
                //删除原来的头像图片
                Util.Url(beLeaderinformation.getHead_portrait(),uploadUrl.getLeaderUrlMysql(),4);
            }else{
                leaderinformation.setHead_portrait(beLeaderinformation.getHead_portrait());
            }
            //修改 删除骨干成员
            if (keymemberId !=null){
                for (Long x:keymemberId) {
                    keymemberService.deleteById(x);
                }
            }
            //新增骨干成员
            if (keymember != null){
                List<Keymember> keymembers = JSON.parseArray(keymember, Keymember.class);
                for (Keymember x:keymembers) {
                    if (x.getKeyid()!=null){
                        x.setId(x.getKeyid());
                        Keymember keymem = keymemberService.getById(x.getId());
                        EntityBeanUtil.copyProperties(keymem, x);
                    }
                    x.setForegoerid(leaderinformation.getId());
                    // 保存数据
                    keymemberService.save(x);
                }
            }
            //删除数据库附件和服务器附件
            if (fileid != null){
                for (Long x:fileid) {
                    LeaderAccessory byId = leaderAccessoryService.getById(x);
                    leaderAccessoryService.deleteById(x);
                    Util.Url(byId.getAccessory_url(),uploadUrl.getLeaderAccessoryLinux(),4);
                }
            }
            //保存附件
            if (files != null){
                for (MultipartFile x:files) {
                    LeaderAccessory leaderAccessory = new LeaderAccessory();
                    ResultInfo resultInfo = fileController.uploadImages(x, request,uploadUrl.getLeaderAccessoryLinux());
                    String data = (String)resultInfo.getData();
                    leaderAccessory.setLeaderId(leaderinformation.getId());
                    leaderAccessory.setAccessory_name(x.getOriginalFilename());
                    leaderAccessory.setAccessory_url(uploadUrl.getLeaderAccessoryMysql()+data);
                    leaderAccessoryService.save(leaderAccessory);
                }
            }
            // 保存数据
            leaderinformationService.save(leaderinformation);
        }else {
            //保存头像
            if (head != null){
                ResultInfo resultInfo = fileController.uploadImages(head, request,uploadUrl.getLeaderUrlLinux());
                String data = (String)resultInfo.getData();
                leaderinformation.setHead_portrait(uploadUrl.getLeaderUrlMysql() + data);//leaderUrl + data   数据库存图片访问地址
            }
            // 保存数据
            leaderinformationService.save(leaderinformation);
            Long id = leaderinformation.getId();
            //添加骨干成员
            if (keymember != null){
                List<Keymember> keymembers = JSON.parseArray(keymember, Keymember.class);
                for (Keymember x:keymembers) {
                    x.setForegoerid(id);
                    keymemberService.save(x);
                }
            }
            //保存附件
            if (files != null){
                for (MultipartFile x:files) {
                    LeaderAccessory leaderAccessory = new LeaderAccessory();
                    ResultInfo resultInfo = fileController.uploadImages(x, request,uploadUrl.getLeaderAccessoryLinux());
                    String data = (String)resultInfo.getData();
                    leaderAccessory.setLeaderId(id);
                    leaderAccessory.setAccessory_name(x.getOriginalFilename());
                    leaderAccessory.setAccessory_url(uploadUrl.getLeaderAccessoryMysql()+data);
                    leaderAccessoryService.save(leaderAccessory);
                }
            }
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("modelstudio:leaderinformation:detail")
    public String toDetail(@PathVariable("id") Long id, Model model) {
        Leaderinformation leaderinformation = leaderinformationService.getById(id);
        //List<Keymember> all = keymemberService.findAll();
        model.addAttribute("leaderinformation",leaderinformation);
        //model.addAttribute("KeymemberList",all);
        return "/modelstudio/leaderinformation/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("modelstudio:leaderinformation:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (leaderinformationService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}