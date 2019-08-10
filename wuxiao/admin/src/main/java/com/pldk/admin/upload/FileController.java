package com.pldk.admin.upload;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 文件上传和下载,
 * 
 * @author liangcm
 *
 */
/*@Controller
@RequestMapping(value = "/file")
@CrossOrigin(value = "*", allowCredentials = "true")*/
@Component
public class FileController extends BaseFileController {
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	/**
	 * 
	 * @param
	 * @param request
	 * @param
	 * @return
	 */
	/*@RequestMapping("/upload")
	@ResponseBody*/
	public ResultInfo uploadImages(MultipartFile file, HttpServletRequest request,String url) {
		//files图片数据数组  abc业务需要传的值
		ResultInfo resultInfo = new ResultInfo(0, "success");
		//获取上传路径
		StringBuffer path = null;
		path = new StringBuffer(url);
		String folder = "JNPX";
		if (StringUtils.isNotBlank(request.getParameter("folder"))) {
			path.append(request.getParameter("folder")).append(File.separator);
			folder = request.getParameter("folder") + File.separator;
		}
		try {
			if (file !=null){
				//执行上传图片
				upload(folder, file, path.toString(), request);
			}
			resultInfo.setData(getFileName());
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
			throw new BussinessException(e.getMessage());
		} catch (IOException e) {
			logger.error("", e);
			throw new BussinessException(e.getMessage());
		} catch (Exception e) {
			logger.info("文件上传异常", e);
			throw new BussinessException(e.getMessage());
		}
		return resultInfo;
	}

	/**
	 * 编辑器图片上传实现
	 * 
	 * @param file
	 * @param CKEditorFuncNum
	 * @return
	 * @throws Exception
	 */
	/*@ResponseBody
	@RequestMapping("/ckeditorUpload")
	// 名字upload是固定的，有兴趣，可以打开浏览器查看元素验证
	public String ckeditorUpload(@RequestParam("upload") MultipartFile file, String CKEditorFuncNum,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuffer path = new StringBuffer(laborunProperties.getFileUploadPath());
		String folder = "JNPX";
		if (StringUtils.isNotBlank(request.getParameter("folder"))) {
			path.append(request.getParameter("folder")).append(File.separator);
			folder = request.getParameter("folder") + File.separator;
		}
		String name = FileUtil.subFileName(file.getOriginalFilename());
		setFileName(name);
		upload(folder, file, path.toString(), request);
		FileInfo filesRecord = new FileInfo();
		filesRecord.setId(UUIDUtil.getUid());
		filesRecord.setFileName(getFileName());
		filesRecord.setFileOriginName(file.getOriginalFilename());
		filesRecord.setValid(false);
		filesRecord.setFilePath(laborunProperties.getUserCenterUrl() + getFileName());
		filesRecord.setIsDelete(false);
		filesRecord.setFileType("images");
		filesRecord.setSize(file.getSize());
		filesRecord.setUploadTime(DateUtil.getDate(new Date()));
		filesRecordMapper.insertSelective(filesRecord);
		// 实现图片回显，基本上是固定代码，只需改路劲即可
		StringBuffer sb = new StringBuffer();
		sb.append("<script type=\"text/javascript\">");
		sb.append("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",'" + ""+ filesRecord.getFilePath()+" ','')");
		sb.append("</script>");

		return sb.toString();
	}

		@RequestMapping("/uploadImage")
	    @ResponseBody
	    public UploadImageResModel uploadImage(@RequestParam("upload") MultipartFile multipartFile) {
	        UploadImageResModel res = new UploadImageResModel();
	        res.setUploaded(0);

	        if (multipartFile == null || multipartFile.isEmpty())
	            return res;

	        //生成新的文件名及存储位置
	        String fileName = multipartFile.getOriginalFilename();
	        String newFileName = UUID.randomUUID().toString()
	                .replaceAll("-", "")
	                .concat(fileName.substring(fileName.lastIndexOf(".")));

	        String fullPath = "D:/images/".concat(newFileName);

	        try {
	            File target = new File(fullPath);
	            if (!target.getParentFile().exists()) { //判断文件父目录是否存在
	                target.getParentFile().mkdirs();
	            }

	            multipartFile.transferTo(target);

	            String imgUrl = "/upload/".concat(newFileName);

	            res.setUploaded(1);
	            res.setFileName(fileName);
	            res.setUrl(imgUrl);
	            return res;
	        } catch (IOException ex) {
	            logger.error("上传图片异常", ex);
	        }

	        return res;
	    }*/

	//删除文件

}
