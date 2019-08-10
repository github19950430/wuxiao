package com.pldk.modules.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.pldk.modules.system.domain.Upload;
import com.pldk.modules.system.repository.UploadRepository;
import com.pldk.modules.system.service.UploadService;

/**
 * @author Pldk
 * @date 2018/11/02
 */
@Service
public class UploadServiceImpl implements UploadService {

	@Autowired
	private UploadRepository uploadRepository;

	/**
	 * 获取文件sha1值的记录
	 */
	@Override
	public Upload getBySha1(String sha1) {
		return uploadRepository.findBySha1(sha1);
	}

	/**
	 * 保存文件上传
	 * 
	 * @param upload
	 *            文件上传实体类
	 */
	@Override
	public Upload save(Upload upload) {
		return uploadRepository.save(upload);
	}

	/**
	 * 获取文件md5值的记录
	 */
	@Override
	public Upload getByMd5(String md5) {
		return uploadRepository.findByMd5(md5);
	}

	/**
	 * 获取文件id值的记录
	 */
	@Override
	public Upload getById(long id) {
		return uploadRepository.findById(id);
	}
	
	@Override
	public List<Upload> getListUpload(String ids) {
		List<Long> list=new ArrayList<Long>();
		if(!StringUtils.isEmpty(ids)) {
			String[] array=ids.split(",");
			for(String s:array) {
					list.add(Long.valueOf(s));
			}
		}
		return uploadRepository.findAllById(list);
	}
}
