package com.pldk.modules.modelstudio.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.StudioHonor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/22
 */
public interface StudioHonorService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<StudioHonor> getPageList(Example<StudioHonor> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    StudioHonor getById(Long id);

    /**
     * 保存数据
     * @param studioHonor 实体对象
     */
    StudioHonor save(StudioHonor studioHonor);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    /**
     * 根据工作室ID 查询所有荣誉图片
     * @param id
     * @return
     */
    List<StudioHonor> findAllByStudioId(Long id);
    /***
     * 根据主键ID 删除表数据
     * @param id
     * @return
     */
    @Transactional
    int deleteById(Long id);
}