package com.pldk.modules.review.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.review.domain.ReviewScore;
import com.pldk.modules.review.domain.ReviewScoreDetail;
import com.pldk.modules.review.vo.ReviewScoreDetailVo;
import com.pldk.modules.review.vo.ReviewScoreVo;
import com.pldk.modules.review.vo.ReviewSetVo;

/**
 * @author renxuqiang
 * @date 2019/07/18
 */
public interface ReviewScoreService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<ReviewScore> getPageList(Example<ReviewScore> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    ReviewScore getById(Long id);

    /**
     * 保存数据
     * @param reviewScore 实体对象
     */
    ReviewScore save(ReviewScore reviewScore,List<ReviewScoreDetail> detailList);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
    
    
    ReviewSetVo  getReviewSetById(Long id);
    
    ReviewSetVo  getReviewSetTypeById(Long id);
    
    ReviewScoreVo getReviewScoreDetail(ReviewScore reviewScore);
    
    ReviewScoreDetailVo  getDetailById(ReviewScoreDetail reviewScoreDetail);
    
}