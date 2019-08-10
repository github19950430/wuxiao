package com.pldk.modules.foregoerManager.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pldk.modules.foregoerManager.domain.Foregoer;
import com.pldk.modules.system.repository.BaseRepository;

/**
 * @author Pldk
 * @date 2019/07/01
 */
public interface ForegoerRepository extends BaseRepository<Foregoer, Long>,JpaSpecificationExecutor<Foregoer>  {
	
}