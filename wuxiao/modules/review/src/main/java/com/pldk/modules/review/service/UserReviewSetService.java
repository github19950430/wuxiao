package com.pldk.modules.review.service;

import java.util.List;
import java.util.Map;

import com.pldk.modules.review.domain.UserReviewSet;

/**
 * @author Pldk
 * @date 2019/07/12
 */
public interface UserReviewSetService {


    /**
     * 保存数据
     * @param reviewSet 实体对象
     */
    UserReviewSet save(UserReviewSet reviewSet);

    
    List<Map<String,Object>> getList(Long userId,Long resultId);
    
    List<Map<String,Object>> getList(Long resultId);
    
}