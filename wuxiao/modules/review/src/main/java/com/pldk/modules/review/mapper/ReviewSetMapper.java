package com.pldk.modules.review.mapper;
import com.pldk.modules.review.domain.ReviewScore;
import com.pldk.modules.review.domain.ReviewScoreDetail;
import com.pldk.modules.review.vo.ReviewScoreDetailVo;
import com.pldk.modules.review.vo.ReviewScoreVo;
import com.pldk.modules.review.vo.ReviewSetVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewSetMapper {
    
	ReviewSetVo getReviewSetTypeById(Long id);
	
	ReviewSetVo getReviewSetById(Long id);
	
	ReviewScoreVo getReviewScoreDetail(ReviewScore reviewScore);
	
	ReviewScoreDetailVo  getDetailById(ReviewScoreDetail reviewScoreDetail);
}