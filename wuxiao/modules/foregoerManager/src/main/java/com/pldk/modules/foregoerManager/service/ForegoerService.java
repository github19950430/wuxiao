package com.pldk.modules.foregoerManager.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.foregoerManager.domain.Foregoer;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/01
 */
public interface ForegoerService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Foregoer> getPageList(Example<Foregoer> example);
    
    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Foregoer> getPageList(Foregoer foregoer);
    
    /**
     * 获取列表数据
     * @param example 查询实例
     * @return 返回列表数据
     */
    List<Foregoer> getList(Foregoer foregoer);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Foregoer getById(Long id);

    /**
     * 保存数据
     * @param foregoer 实体对象
     */
    Foregoer save(Foregoer foregoer);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}