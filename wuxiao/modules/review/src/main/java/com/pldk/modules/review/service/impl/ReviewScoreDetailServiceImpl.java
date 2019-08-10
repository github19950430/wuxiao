package com.pldk.modules.review.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.data.PageSort;
import com.pldk.modules.review.domain.ReviewScoreDetail;
import com.pldk.modules.review.repository.ReviewScoreDetailRepository;
import com.pldk.modules.review.service.ReviewScoreDetailService;

/**
 * @author renxuqiang
 * @date 2019/07/18
 */
@Service
public class ReviewScoreDetailServiceImpl implements ReviewScoreDetailService {

    @Autowired
    private ReviewScoreDetailRepository reviewScoreDetailRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public ReviewScoreDetail getById(Long id) {
        return reviewScoreDetailRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<ReviewScoreDetail> getPageList(Example<ReviewScoreDetail> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return reviewScoreDetailRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param reviewScoreDetail 实体对象
     */
    @Override
    public ReviewScoreDetail save(ReviewScoreDetail reviewScoreDetail) {
        return reviewScoreDetailRepository.save(reviewScoreDetail);
    }

}