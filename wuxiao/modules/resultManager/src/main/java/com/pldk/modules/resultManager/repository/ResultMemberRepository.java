package com.pldk.modules.resultManager.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pldk.modules.resultManager.domain.ResultMember;
import com.pldk.modules.system.repository.BaseRepository;

/**
 * @author Pldk
 * @date 2019/06/29
 */
public interface ResultMemberRepository extends BaseRepository<ResultMember, Long>,JpaSpecificationExecutor<ResultMember>  {
	
	  
}

