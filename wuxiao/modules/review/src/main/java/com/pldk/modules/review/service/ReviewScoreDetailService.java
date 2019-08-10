package com.pldk.modules.review.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.review.domain.ReviewScoreDetail;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author renxuqiang
 * @date 2019/07/18
 */
public interface ReviewScoreDetailService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<ReviewScoreDetail> getPageList(Example<ReviewScoreDetail> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    ReviewScoreDetail getById(Long id);

    /**
     * 保存数据
     * @param reviewScoreDetail 实体对象
     */
    ReviewScoreDetail save(ReviewScoreDetail reviewScoreDetail);

}