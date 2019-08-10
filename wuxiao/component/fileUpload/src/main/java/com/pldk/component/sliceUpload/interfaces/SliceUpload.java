package com.pldk.component.sliceUpload.interfaces;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.pldk.component.sliceUpload.entity.FileInfo;
import com.pldk.component.sliceUpload.entity.UploadExtraParam;

@Component
public interface SliceUpload {
	
	@GetMapping("/uploadFile")
	public Object preUpload(HttpServletRequest request, UploadExtraParam param);
	
	@PostMapping("/uploadFile")
	public Object postUpload(HttpServletRequest request, UploadExtraParam param) throws IOException;
	
	public Object afterUpload(FileInfo fileInfo);
}
