package com.pldk.modules.review.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import com.pldk.modules.review.domain.UserReviewSet;
import com.pldk.modules.system.repository.BaseRepository;

/**
 * @author Pldk
 * @date 2019/07/12
 */
public interface UserReviewSetRepository extends BaseRepository<UserReviewSet, Long> {
	
	@Query(value="select id,user_id,review_set_json_str,message,result_id,score,realname,result_name from tr_user_review_set where user_id=?1 and result_id=?2",nativeQuery = true)
	public List<Map<String,Object>> getList(Long userId,Long resultId);
	
	@Query(value="select id,user_id,review_set_json_str,message,result_id,score,realname,result_name from tr_user_review_set where   result_id=?1",nativeQuery = true)
	public List<Map<String,Object>> getList(Long resultId);
}  