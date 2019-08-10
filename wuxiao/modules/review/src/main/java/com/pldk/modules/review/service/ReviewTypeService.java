package com.pldk.modules.review.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.review.domain.ReviewType;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public interface ReviewTypeService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<ReviewType> getPageList(Example<ReviewType> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    ReviewType getById(Long id);

    /**
     * 保存数据
     * @param reviewType 实体对象
     */
    ReviewType save(ReviewType reviewType);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
    

    /**
     * 名称是否重复
     * @param type 审核项对象
     * @return 审核项数据
     */
    Boolean repeatByName(ReviewType type);
    /**
     * 获取一级审核项的列表
     * @return
     */
    List<ReviewType> getByOneTypeListByPid();
    
    List<ReviewType> findAll(Example<ReviewType> example,Sort sort);
}