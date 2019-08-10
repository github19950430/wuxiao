package com.pldk.modules.system.service;

import java.util.List;

import com.pldk.modules.system.domain.Upload;

/**
 * @author Pldk
 * @date 2018/11/02
 */
public interface UploadService {

    /**
     * 获取文件sha1值的记录
     */
    Upload getBySha1(String sha1);

    /**
     * 保存文件上传
     * @param upload 文件上传实体类
     */
    Upload save(Upload upload);
    
    /**
     * 获取文件得md5值
     * @param md5
     * @return
     */
    Upload getByMd5(String md5);
    
    /**
     * 获取文件得id值
     * @param id
     * @return
     */
    Upload getById(long id);
    
    /**
	   *  查询全部
	 * @param ids
	 * @return
	 */
	List<Upload> getListUpload(String ids);
    
}

