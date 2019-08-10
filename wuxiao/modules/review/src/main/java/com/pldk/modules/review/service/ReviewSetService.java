package com.pldk.modules.review.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.review.domain.ReviewSet;

/**
 * @author Pldk
 * @date 2019/07/12
 */
public interface ReviewSetService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<ReviewSet> getPageList(Example<ReviewSet> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    ReviewSet getById(Long id);

    /**
     * 保存数据
     * @param reviewSet 实体对象
     */
    ReviewSet save(ReviewSet reviewSet);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
    
    /**
     * 名称是否重复
     * @param type 审核内容对象
     * @return 审核内容数据
     */
    Boolean repeatByContent(ReviewSet set);
    
    List<ReviewSet> findAllByPid(Long pid);
}