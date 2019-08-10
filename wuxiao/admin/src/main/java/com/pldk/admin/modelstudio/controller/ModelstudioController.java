package com.pldk.admin.modelstudio.controller;

import com.pldk.admin.config.UploadUrl;
import com.pldk.admin.config.Util;
import com.pldk.admin.modelstudio.validator.ModelstudioValid;
import com.pldk.admin.upload.FileController;
import com.pldk.admin.upload.ResultInfo;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.EntityBeanUtil;
import com.pldk.common.utils.ResultVoUtil;
import com.pldk.common.utils.StatusUtil;
import com.pldk.common.vo.ResultVo;
import com.pldk.component.shiro.ShiroUtil;
import com.pldk.modules.modelstudio.domain.*;
import com.pldk.modules.modelstudio.service.*;
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.service.DeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Pldk
 * @date 2019/07/10
 */
@Controller
@RequestMapping("/modelstudio/modelstudio")
public class ModelstudioController {

    @Autowired
    private ModelstudioService modelstudioService;
    @Autowired
    private FileController fileController;
    @Autowired
    private UploadUrl uploadUrl;
    @Autowired
    private StudioVideoService studioVideoService; //视频
    @Autowired
    private StudioImgService studioImgService; //图片
    @Autowired
    private StudioAccessoryService studioAccessoryService; //附件
    @Autowired
    private StudioHonorService studioHonorService; //荣誉图片
    @Autowired
    private LeaderinformationService leaderinformationService;
    @Autowired
    private LeaderAccessoryService leaderAccessoryServicel;
    @Autowired
    private KeymemberService keymemberService;
    @Autowired
    private DeptService deptService;//所属工会组织
    /**
     * 列表页面   0 劳模
     */
    @GetMapping("/index")
    @RequiresPermissions("modelstudio:modelstudio:index")
    public String index(Model model, Modelstudio modelstudio) {
        Byte state = 0;
        modelstudio.setState(state);

        User subject = ShiroUtil.getSubject();
        Dept dept = subject.getDept();
        Integer level = dept.getLevel();//level 1 是一级 2 是二级 3 是三级
        Long id = dept.getId();//太原市总工会ID


        if (level == 1 && modelstudio.getFlat_type() == null){//省总列表首页
            Page<Map<String, Object>> list = modelstudioService.findAllSZ(state);
            model.addAttribute("list",list.getContent());
            model.addAttribute("page", list);
            model.addAttribute("index",state);
            return "/modelstudio/modelstudio/shenzong_index";
        }

        if (level != 1){//市总 及 各地市 县
            modelstudio.setFlat_type(id.toString());
        }
        // 创建匹配器，进行动态查询匹配   audit_status
        //ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        //Example<Modelstudio> example = Example.of(modelstudio, matcher);
        Page<Modelstudio> list = modelstudioService.getPageList(modelstudio,level);

        List<Modelstudio> content = list.getContent();
        content.forEach(x->{
            Dept byId = deptService.getById(Long.parseLong(x.getFlat_type()));
            x.setFlat_name(byId.getTitle());
        });

        // 封装数据
        model.addAttribute("list", content);
        model.addAttribute("page", list);
        //劳模 职工 依据
        model.addAttribute("index",state);
        model.addAttribute("level",level);
        model.addAttribute("type", modelstudio.getFlat_type());
        return "/modelstudio/modelstudio/studio_index";
    }

    /**
     * 列表页面   1 职工
     */
    @GetMapping("/indexzg")
    @RequiresPermissions("modelstudio:modelstudio:indexzg")
    public String indexzg(Model model, Modelstudio modelstudio) {
        Byte state = 1;
        modelstudio.setState(state);

        User subject = ShiroUtil.getSubject();
        Dept dept = subject.getDept();
        Integer level = dept.getLevel();//level 1 是一级 2 是二级 3 是三级
        Long id = dept.getId();//太原市总工会ID


        if (level == 1 && modelstudio.getFlat_type() == null){//省总列表首页
            Page<Map<String, Object>> list = modelstudioService.findAllSZ(state);
            model.addAttribute("list",list.getContent());
            model.addAttribute("page", list);
            model.addAttribute("index",state);
            return "/modelstudio/modelstudio/shenzong_index";
        }

        if (level != 1){//市总 及 各地市 县
            modelstudio.setFlat_type(id.toString());
        }

        // 创建匹配器，进行动态查询匹配
        //ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        //Example<Modelstudio> example = Example.of(modelstudio, matcher);
        Page<Modelstudio> list = modelstudioService.getPageList(modelstudio,level);

        List<Modelstudio> content = list.getContent();
        content.forEach(x->{
            Dept byId = deptService.getById(Long.parseLong(x.getFlat_type()));
            x.setFlat_name(byId.getTitle());
        });
        // 封装数据
        model.addAttribute("list", content);
        model.addAttribute("page", list);
        //劳模 职工 依据
        model.addAttribute("index",state);
        model.addAttribute("level",level);
        model.addAttribute("type", modelstudio.getFlat_type());
        return "/modelstudio/modelstudio/studio_index";
    }

    /**
     * 跳转到添加页面 id  0 劳模添加 1 职工添加
     */
    @GetMapping("/add/{index}")
    @RequiresPermissions("modelstudio:modelstudio:add")
    public String toAdd(@PathVariable("index")Byte index,Model model) {
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        model.addAttribute("one",year-1 + "年：");
        model.addAttribute("two",year-2 + "年：");
        model.addAttribute("three",year-3 + "年：");
        //查询带头人
        List<Leaderinformation> all = leaderinformationService.findAll();

        Map<String,Object> map = new HashMap<>();

        for (Leaderinformation x:all) {
            map.put(x.getId() + "",x.getName()+"-"+ x.getPhone());
        }

        User subject = ShiroUtil.getSubject();
        Dept dept = subject.getDept();
        Integer level = dept.getLevel();//level 1 是一级 2 是二级 3 是三级
        String title = dept.getTitle();//太原市总工会

        model.addAttribute("level",level);
        model.addAttribute("title",title);
        model.addAttribute("leaderlist",map);
        model.addAttribute("index",index);
        return "/modelstudio/modelstudio/studio_add";
    }

    /**
     * 跳转到编辑页面Tab选项卡 index 0 劳模编辑 1 职工编辑
     */
    @GetMapping("/editTab/{id}/{index}")
    @RequiresPermissions("modelstudio:modelstudio:edit")
    public String toEditTab(@PathVariable("id") Modelstudio modelstudio,@PathVariable("index")Byte index, Model model) {
        //工作室详情
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        model.addAttribute("one",year-1 + "年：");
        model.addAttribute("two",year-2 + "年：");
        model.addAttribute("three",year-3 + "年：");
        model.addAttribute("modelstudio", modelstudio);
        List<StudioVideo> allByVideo = studioVideoService.getAllByStudioId(modelstudio.getId());//视频
        List<StudioImg> allByImg = studioImgService.findAllByStudioId(modelstudio.getId());//图片
        List<StudioAccessory> allByAccessory = studioAccessoryService.findAllByStudioId(modelstudio.getId());//附件
        List<StudioHonor> allByhonor = studioHonorService.findAllByStudioId(modelstudio.getId());//荣誉图片
        model.addAttribute("allByVideo",allByVideo);
        model.addAttribute("allByImg",allByImg);
        model.addAttribute("allByAccessory",allByAccessory);
        model.addAttribute("allByhonor",allByhonor);
        //查询带头人
        List<Leaderinformation> all = leaderinformationService.findAll();
        Map<String,Object> map = new HashMap<>();
        for (Leaderinformation x:all) {
            map.put(x.getId() + "",x.getName()+"-"+ x.getPhone());
        }
        model.addAttribute("leaderlist",map);

        //带头人详情
        Leaderinformation leaderinformation = leaderinformationService.getById(modelstudio.getLingxianrenId());
        //查询带头人附件
        List<LeaderAccessory> allByleaderId = leaderAccessoryServicel.getAllByleaderId(modelstudio.getLingxianrenId());
        //获得骨干成员列表
        List<Keymember> allByforegoerId = keymemberService.findAllByforegoerId(modelstudio.getLingxianrenId());

        User subject = ShiroUtil.getSubject();
        Dept dept = subject.getDept();
        Integer level = dept.getLevel();//level 1 是一级 2 是二级 3 是三级
        Dept byId = deptService.getById(Long.parseLong(modelstudio.getFlat_type()));

        model.addAttribute("leaderinformation",leaderinformation);
        model.addAttribute("allByforegoerIdList",allByforegoerId);
        model.addAttribute("allByleaderIdList",allByleaderId);
        model.addAttribute("index",index);
        model.addAttribute("level",level);
        model.addAttribute("title",byId.getTitle());
        return "/modelstudio/modelstudio/studio_editTab";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"modelstudio:modelstudio:add","modelstudio:modelstudio:edit"})
    @ResponseBody
    public ResultVo save(@Validated ModelstudioValid valid, Modelstudio modelstudio,
                         HttpServletRequest request,
                         @RequestParam(value = "gh",required = false) MultipartFile gh,
                         @RequestParam(value = "sz",required = false) MultipartFile sz,
                         @RequestParam(value = "SZZ",required = false) MultipartFile SZZ,

                         @RequestParam(value = "videoid",required = false) Long[] videoid,
                         @RequestParam(value = "video",required = false) MultipartFile[] video,
                         @RequestParam(value = "imgsid",required = false) Long[] imgsid,
                         @RequestParam(value = "imgs",required = false) MultipartFile[] imgs,
                         @RequestParam(value = "filesid",required = false) Long[] filesid,
                         @RequestParam(value = "files",required = false) MultipartFile[] files,
                         @RequestParam(value = "honorsid",required = false) Long[] honorsid,
                         @RequestParam(value = "honors",required = false) MultipartFile[] honors) {

        User subject = ShiroUtil.getSubject();
        Dept dept = subject.getDept();

        // 复制保留无需修改的数据
        if (modelstudio.getId() != null) {
            //修改保存
            Modelstudio beModelstudio = modelstudioService.getById(modelstudio.getId());
            EntityBeanUtil.copyProperties(beModelstudio, modelstudio);
            if (gh == null){
                modelstudio.setUnit_opinion(beModelstudio.getUnit_opinion());
            }else {
                //删除图片
                Util.Url(beModelstudio.getUnit_opinion(),uploadUrl.getLaborUnionLinux(),4);
                ResultInfo resultgh = fileController.uploadImages(gh, request, uploadUrl.getLaborUnionLinux());//上传单位工会意见图片
                String datagh = (String)resultgh.getData();
                modelstudio.setUnit_opinion(uploadUrl.getLaborUnionMysql() + datagh);
            }
            if (sz == null){
                modelstudio.setAlways_opinions(beModelstudio.getAlways_opinions());
            }else {
                //删除图片
                Util.Url(beModelstudio.getAlways_opinions(),uploadUrl.getLaborUnionLinux(),4);
                ResultInfo resultsz = fileController.uploadImages(sz, request, uploadUrl.getLaborUnionLinux());//上传市总工会意见图片
                String datasz = (String)resultsz.getData();
                modelstudio.setAlways_opinions(uploadUrl.getLaborUnionMysql() + datasz);
            }

            if (SZZ == null){
                modelstudio.setGeneral_opinion(beModelstudio.getGeneral_opinion());
            }else {
                //删除图片
                //Util.Url(beModelstudio.getGeneral_opinion(),uploadUrl.getLaborUnionLinux(),4);
                ResultInfo resultSZ = fileController.uploadImages(SZZ, request, uploadUrl.getLaborUnionLinux());//上传省总工会意见图片
                String dataSZ = (String)resultSZ.getData();
                modelstudio.setGeneral_opinion(uploadUrl.getLaborUnionMysql() + dataSZ);
            }

            //编辑时 删除视频
            if (videoid != null){
                for (Long x:videoid) {
                    StudioVideo byId = studioVideoService.getById(x);
                    //删除数据库
                    studioVideoService.deleteById(x);
                    //删除文件夹文件
                    Util.Url(byId.getVideo_url(),uploadUrl.getStudioVideoLinux(),4);
                }
            }
            //编辑时 删除 图片
            if (imgsid != null){
                for (Long x:imgsid) {
                    StudioImg byId = studioImgService.getById(x);
                    studioImgService.deleteById(x);
                    Util.Url(byId.getImg_url(),uploadUrl.getStudioImgLinux(),4);
                }
            }
            //编辑时 删除附件
            if (filesid != null){
                for (Long x:filesid) {
                    StudioAccessory byId = studioAccessoryService.getById(x);
                    studioAccessoryService.deleteById(x);
                    Util.Url(byId.getAccessory_url(),uploadUrl.getStudioFileLinux(),4);
                }
            }

            //编辑是删除 荣誉图片
            if (honorsid != null){
                for (Long x:honorsid){
                    StudioHonor byId = studioHonorService.getById(x);
                    studioHonorService.deleteById(x);
                    Util.Url(byId.getImg_url(),uploadUrl.getStudioImgLinux(),4);
                }
            }
            //修改视频
            if (video != null){
                for (MultipartFile x:video) {
                    StudioVideo studioVideo = new StudioVideo();
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getStudioVideoLinux());//上传视频
                    String data = (String)resultInfo.getData();//视频名字
                    studioVideo.setName(x.getOriginalFilename());
                    studioVideo.setStudioId(modelstudio.getId());
                    studioVideo.setVideo_url(uploadUrl.getStudioVideoMysql() + data);
                    studioVideoService.save(studioVideo);
                }
            }
            //修改图片
            if (imgs != null){
                for (MultipartFile x:imgs) {
                    StudioImg studioImg = new StudioImg();
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getStudioImgLinux());//上传图片
                    String data = (String)resultInfo.getData();//图片名字
                    studioImg.setStudioId(modelstudio.getId());
                    studioImg.setImg_url(uploadUrl.getStudioImgMysql() + data);
                    studioImgService.save(studioImg);
                }
            }
            //修改附件
            if (files != null){
                for (MultipartFile x:files) {
                    StudioAccessory studioAccessory = new StudioAccessory();
                    String name = x.getOriginalFilename();
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getStudioFileLinux());//上传附件
                    String data = (String)resultInfo.getData();//附件名字
                    studioAccessory.setStudioId(modelstudio.getId());
                    studioAccessory.setAccessory_name(name);
                    studioAccessory.setAccessory_url(uploadUrl.getStudioFileMysql() + data);
                    studioAccessoryService.save(studioAccessory);
                }
            }
            if (honors != null){
                for (MultipartFile x:honors){
                    StudioHonor studioHonor = new StudioHonor();
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getStudioImgLinux());//上传图片
                    String data = (String)resultInfo.getData();//荣誉图片名字
                    studioHonor.setStudioId(modelstudio.getId());
                    studioHonor.setImg_url(uploadUrl.getStudioImgMysql() + data);
                    studioHonorService.save(studioHonor);
                }
            }
            // 保存数据
            modelstudioService.save(modelstudio);
        }else {
            Modelstudio one = modelstudioService.findOne(modelstudio.getStudioName());
            if (one != null)
                return ResultVoUtil.SAVE_ERROR;
            //工会单位意见图片
            if (gh != null){
                ResultInfo resultgh = fileController.uploadImages(gh, request, uploadUrl.getLaborUnionLinux());//上传单位工会意见图片
                String datagh = (String)resultgh.getData();
                modelstudio.setUnit_opinion(uploadUrl.getLaborUnionMysql() + datagh);
            }
            //市总意见图片
            if (sz != null){
                ResultInfo resultsz = fileController.uploadImages(sz, request, uploadUrl.getLaborUnionLinux());//上传市总工会意见图片
                String datasz = (String)resultsz.getData();
                modelstudio.setAlways_opinions(uploadUrl.getLaborUnionMysql() + datasz);
            }
            //省总意见图片
            if (SZZ != null){
                ResultInfo resultSZ = fileController.uploadImages(SZZ, request, uploadUrl.getLaborUnionLinux());//上传省总工会意见图片
                String dataSZ = (String)resultSZ.getData();
                modelstudio.setGeneral_opinion(uploadUrl.getLaborUnionMysql() + dataSZ);
            }
            modelstudio.setFlat_type(dept.getId().toString());//所属工会-总工会ID
            Byte status = null;
            if(dept.getLevel() == 1){//省总添加
                status = 2;
            }else{
                status = 0;
            }
            modelstudio.setAudit_status(status);//
            // 保存数据
            modelstudioService.save(modelstudio);
            Long id = modelstudio.getId();

            if (video != null){
                for (MultipartFile x:video) {
                    StudioVideo studioVideo = new StudioVideo();
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getStudioVideoLinux());//上传视频
                    String data = (String)resultInfo.getData();//视频名字
                    studioVideo.setName(x.getOriginalFilename());
                    studioVideo.setStudioId(id);
                    studioVideo.setVideo_url(uploadUrl.getStudioVideoMysql() + data);
                    studioVideoService.save(studioVideo);
                }
            }
            if (imgs != null){
                for (MultipartFile x:imgs) {
                    StudioImg studioImg = new StudioImg();
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getStudioImgLinux());//上传图片
                    String data = (String)resultInfo.getData();//图片名字
                    studioImg.setStudioId(id);
                    studioImg.setImg_url(uploadUrl.getStudioImgMysql() + data);
                    studioImgService.save(studioImg);
                }
            }
            if (files != null){
                for (MultipartFile x:files) {
                    StudioAccessory studioAccessory = new StudioAccessory();
                    String name = x.getOriginalFilename();
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getStudioFileLinux());//上传附件
                    String data = (String)resultInfo.getData();//附件名字
                    studioAccessory.setStudioId(id);
                    studioAccessory.setAccessory_name(name);
                    studioAccessory.setAccessory_url(uploadUrl.getStudioFileMysql() + data);
                    studioAccessoryService.save(studioAccessory);
                }
            }
            if (honors != null){
                for (MultipartFile x:honors){
                    StudioHonor studioHonor = new StudioHonor();
                    ResultInfo resultInfo = fileController.uploadImages(x, request, uploadUrl.getStudioImgLinux());//上传图片
                    String data = (String)resultInfo.getData();//荣誉图片名字
                    studioHonor.setStudioId(id);
                    studioHonor.setImg_url(uploadUrl.getStudioImgMysql() + data);
                    studioHonorService.save(studioHonor);
                }
            }
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}/{index}")
    @RequiresPermissions("modelstudio:modelstudio:detail")
    public String toDetail(@PathVariable("id") Modelstudio modelstudio,@PathVariable("index") Byte index, Model model) {
        User subject = ShiroUtil.getSubject();
        Dept dept = subject.getDept();
        Integer level = dept.getLevel();//level 1 是一级 2 是二级 3 是三级
        String title = dept.getTitle();//太原市总工会
        Long id = dept.getId();//太原市总工会ID

        //工作室详情
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        model.addAttribute("one",year-1 + "年:");
        model.addAttribute("two",year-2 + "年:");
        model.addAttribute("three",year-3 + "年:");
        model.addAttribute("modelstudio", modelstudio);
        List<StudioVideo> allByVideo = studioVideoService.getAllByStudioId(modelstudio.getId());//视频
        List<StudioImg> allByImg = studioImgService.findAllByStudioId(modelstudio.getId());//图片
        List<StudioAccessory> allByAccessory = studioAccessoryService.findAllByStudioId(modelstudio.getId());//附件
        List<StudioHonor> allByhonor = studioHonorService.findAllByStudioId(modelstudio.getId());//荣誉图片
        model.addAttribute("allByVideo",allByVideo);
        model.addAttribute("allByImg",allByImg);
        model.addAttribute("allByAccessory",allByAccessory);
        model.addAttribute("allByhonor",allByhonor);
        //带头人详情
        Leaderinformation leaderinformation = leaderinformationService.getById(modelstudio.getLingxianrenId());
        //获得骨干成员列表
        List<Keymember> allByforegoerId = keymemberService.findAllByforegoerId(modelstudio.getLingxianrenId());
        //查询带头人附件
        List<LeaderAccessory> allByleaderId = leaderAccessoryServicel.getAllByleaderId(modelstudio.getLingxianrenId());

        model.addAttribute("leaderinformation",leaderinformation);
        model.addAttribute("allByforegoerIdList",allByforegoerId);
        model.addAttribute("allByleaderIdList",allByleaderId);
        model.addAttribute("index",index);
        //工作室简介详情   先不做

        //创新计划详情  先不做
        return "/modelstudio/modelstudio/studio_detailTab";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("modelstudio:modelstudio:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (modelstudioService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/update")
    @RequiresPermissions("modelstudio:modelstudio:status")
    @ResponseBody
    public ResultVo update(@RequestParam(value = "ids", required = false) Long ids){
        // 更新状态
        if (modelstudioService.updateAuditStatus(ids)) {
            return ResultVoUtil.success("上报成功");
        } else {
            return ResultVoUtil.error("上报失败，请重新操作");
        }
    }
}