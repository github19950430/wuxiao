package com.pldk.modules.modelstudio.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.StudioAccessory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public interface StudioAccessoryService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<StudioAccessory> getPageList(Example<StudioAccessory> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    StudioAccessory getById(Long id);

    /**
     * 保存数据
     * @param studioAccessory 实体对象
     */
    StudioAccessory save(StudioAccessory studioAccessory);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    /**
     * 根据工作室ID查询所属工作室附件
     * @param studioId
     * @return
     */
    List<StudioAccessory> findAllByStudioId(Long studioId);

    /**
     * 根据主键ID 删除附件表
     * @param id
     * @return
     */
    @Transactional
    int deleteById(Long id);
}