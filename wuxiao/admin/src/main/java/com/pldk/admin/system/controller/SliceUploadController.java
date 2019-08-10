package com.pldk.admin.system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.pldk.common.utils.ResultVoUtil;
import com.pldk.component.sliceUpload.UploadExecute;
import com.pldk.component.sliceUpload.entity.FileInfo;
import com.pldk.component.sliceUpload.entity.UploadExtraParam;
import com.pldk.component.sliceUpload.interfaces.SliceUpload;
import com.pldk.modules.system.domain.Upload;
import com.pldk.modules.system.service.UploadService;

@RestController
@RequestMapping("/sliceUpload")
public class SliceUploadController implements SliceUpload {
	
	@Autowired
	 private UploadExecute uploadExecute;
	 
	 @Autowired
	 private UploadService uploadService;

	@Override
	@GetMapping("/uploadFile")
	public Object preUpload(HttpServletRequest request, UploadExtraParam param) {
		String hash = request.getParameter("hash");
		Upload uploadMd5 = uploadService.getByMd5(hash);
		if (uploadMd5 != null) {
			return ResultVoUtil.success(uploadMd5);
		} else {
			return ResultVoUtil.error("please upload");
		}
	}

	@PostMapping("/uploadFile")
	public Object postUpload(HttpServletRequest request, UploadExtraParam param) throws IOException {
		MultiValueMap<String, MultipartFile> fileMaps = ((MultipartHttpServletRequest) request).getMultiFileMap();
		List<Object> resList = new ArrayList<>(0);
		for (List<MultipartFile> files : fileMaps.values()) {
			for (MultipartFile file : files) {
				resList.add(uploadExecute.handleUploadFile(file, param));
			}
		}
		if (resList.size() == 1) {
			return ResultVoUtil.success(resList.get(0));
		}
		return ResultVoUtil.success(resList);
	}

	@Override
	public Object afterUpload(FileInfo fileInfo) {
		Upload upload = new Upload();
		upload.setMd5(fileInfo.getHash());
		upload.setMime(fileInfo.getMime());
		upload.setName(fileInfo.getName());
		upload.setSize(fileInfo.getSize());
		upload.setPath(fileInfo.getPath());
		upload.setThumbPath(fileInfo.getThumbPath());
		uploadService.save(upload);
		fileInfo.setId(upload.getId());
		return fileInfo;
	}

}
