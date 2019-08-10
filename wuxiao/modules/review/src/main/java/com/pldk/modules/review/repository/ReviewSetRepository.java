package com.pldk.modules.review.repository;

import java.util.List;

import com.pldk.modules.review.domain.ReviewSet;
import com.pldk.modules.system.repository.BaseRepository;

/**
 * @author Pldk
 * @date 2019/07/12
 */
public interface ReviewSetRepository extends BaseRepository<ReviewSet, Long> {
	
	 public ReviewSet findByContentAndIdNot(String content,Long id);
	 
	 public List<ReviewSet> findAllByPid(Long pid);
}