package com.pldk.modules.review.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.pldk.modules.review.domain.UserReviewSet;
import com.pldk.modules.review.repository.UserReviewSetRepository;
import com.pldk.modules.review.service.UserReviewSetService;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Service
public class UserReviewSetServiceImpl implements UserReviewSetService {

    @Autowired
    private UserReviewSetRepository userReviewSetRepository;



    /**
     * 保存数据
     * @param reviewSet 实体对象
     */
    @Override
    public UserReviewSet save(UserReviewSet reviewSet) {
        return userReviewSetRepository.save(reviewSet);
    }



	@Override
	public List<Map<String,Object>> getList(Long userId, Long resultId) {
		return userReviewSetRepository.getList(userId, resultId);
	}



	@Override
	public List<Map<String,Object>> getList(Long resultId) {
		return  userReviewSetRepository.getList(resultId);
	}
}