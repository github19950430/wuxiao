package com.pldk.modules.modelstudio.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.Modelstudio;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Pldk
 * @date 2019/07/10
 */
public interface ModelstudioService {

    /**
     * 获取分页列表数据
     * @param modelstudio 查询实例
     * @return 返回分页数据
     */
    Page<Modelstudio> getPageList(Modelstudio modelstudio,Integer level);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Modelstudio getById(Long id);

    /**
     * 保存数据
     * @param modelstudio 实体对象
     */
    Modelstudio save(Modelstudio modelstudio);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    /**
     * 查询所添加的工作室是否已存在
     * @param studioName
     * @return
     */
    Modelstudio findOne(String studioName);

    /**
     * 查询创新工作室列表
     * @return
     */
    Map<String,Object> findAll();

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateAuditStatus(Long id);

    /**
     * 省总列表
     * @return
     */
    Page<Map<String, Object>> findAllSZ(Byte state);
}