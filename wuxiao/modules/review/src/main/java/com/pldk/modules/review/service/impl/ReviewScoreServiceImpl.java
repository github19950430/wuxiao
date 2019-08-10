package com.pldk.modules.review.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.review.domain.ReviewScore;
import com.pldk.modules.review.domain.ReviewScoreDetail;
import com.pldk.modules.review.mapper.ReviewSetMapper;
import com.pldk.modules.review.repository.ReviewScoreDetailRepository;
import com.pldk.modules.review.repository.ReviewScoreRepository;
import com.pldk.modules.review.service.ReviewScoreService;
import com.pldk.modules.review.vo.ReviewScoreDetailVo;
import com.pldk.modules.review.vo.ReviewScoreVo;
import com.pldk.modules.review.vo.ReviewSetVo;

/**
 * @author renxuqiang
 * @date 2019/07/18
 */
@Service
public class ReviewScoreServiceImpl implements ReviewScoreService {

    @Autowired
    private ReviewScoreRepository reviewScoreRepository;
    @Autowired
    private ReviewScoreDetailRepository reviewScoreDetailRepository;
    @Autowired
    private ReviewSetMapper reviewSetMapper;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public ReviewScore getById(Long id) {
        return reviewScoreRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<ReviewScore> getPageList(Example<ReviewScore> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return reviewScoreRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param reviewScore 实体对象
     */
    @Override
    @Transactional
    public ReviewScore save(ReviewScore reviewScore,List<ReviewScoreDetail> detailList) {
    	ReviewScore score=reviewScoreRepository.save(reviewScore);
    	if(score.getId()!=null) {
    		for(ReviewScoreDetail detail:detailList) {
    			detail.setScoreId(score.getId());
    			reviewScoreDetailRepository.save(detail);
    		}
    	}
        return score;
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return reviewScoreRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

	@Override
	public ReviewSetVo getReviewSetById(Long id) {
		return reviewSetMapper.getReviewSetById(id);
	}
	@Override
	public ReviewSetVo getReviewSetTypeById(Long id) {
		return reviewSetMapper.getReviewSetTypeById(id);
	}

	@Override
	public ReviewScoreVo getReviewScoreDetail(ReviewScore reviewScore) {
		return reviewSetMapper.getReviewScoreDetail(reviewScore);
	}

	@Override
	public ReviewScoreDetailVo getDetailById(ReviewScoreDetail reviewScoreDetail) {
		return reviewSetMapper.getDetailById(reviewScoreDetail);
	}

}