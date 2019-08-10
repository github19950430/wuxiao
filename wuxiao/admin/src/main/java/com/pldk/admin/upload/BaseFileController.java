package com.pldk.admin.upload;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


public class BaseFileController {
	
	private static final String DOCUMENT_TYPE = "docx,dotx,doc,dot,txt,cvs,Word,Excel,PDF,TXT,CVS,rar,zrp,MP4,mp4,move,RMVB,AVI,flv,3gp";
	private static final String EXCEL_TYPE = "xls,xlsx";
	private static final String IMAGE_TYPE = "jpg,png,gif,jpeg";
	
	
	private String allowSuffix = "jpg,png,gif,jpeg";// 允许文件格式
	private long allowSize = 50L;// 允许文件大小
	private String fileName;

	public String getAllowSuffix() {
		return allowSuffix;
	}

	public void setAllowSuffix(String allowSuffix) {
		this.allowSuffix = allowSuffix;
	}

	public long getAllowSize() {
		return allowSize * 1024 * 1024;
	}

	public void setAllowSize(long allowSize) {
		this.allowSize = allowSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 
	 * @return
	 */
	private String getFileNameNew() {
		return UUIDUtil.getUUIDByLength(17) ;
	}

	/**
	 * 
	 * @param file
	 * @param destDir
	 * @param request
	 * @throws Exception
	 */
	public void upload(String folder, MultipartFile file, String destDir, HttpServletRequest request) throws Exception {
		try {
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			if(DOCUMENT_TYPE.contains(suffix)){
				setAllowSuffix(DOCUMENT_TYPE);
			}else if(EXCEL_TYPE.contains(suffix)){
				setAllowSuffix(EXCEL_TYPE);
			}else if(IMAGE_TYPE.contains(suffix)){
				setAllowSuffix(IMAGE_TYPE);
			}
			int length = getAllowSuffix().indexOf(suffix);
			if (length == -1) {
				throw new Exception("请上传允许格式的文件");
			}
			if (file.getSize() > getAllowSize()) {
				throw new Exception("您上传的文件大小已经超出范围");
			}

			File destFile = new File(destDir);
			if (!destFile.exists()) {
				destFile.mkdirs();
			}
			String fileNameNew = folder+getFileNameNew() + "." + suffix;
			File f = new File(destFile.getAbsoluteFile() + "/" + fileNameNew);
			file.transferTo(f);
			f.createNewFile();
			fileName = fileNameNew;
		} catch (Exception e) {
			throw e;
		}
	}
}