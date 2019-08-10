package com.pldk.modules.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pldk.modules.system.domain.Upload;

/**
 * @author Pldk
 * @date 2018/11/02
 */
public interface UploadRepository extends JpaRepository<Upload, Long> {

    /**
     * 查找指定文件sha1记录
     * @param sha1 文件sha1值
     */
    public Upload findBySha1(String sha1);
    
    /**
     * 获取文件得md5值
     * @param md5
     * @return
     */
    public Upload findByMd5(String md5);
    
    /**
     * 获取文件得id值
     * @param id
     * @return Upload
     */
    public Upload findById(long id);
}

