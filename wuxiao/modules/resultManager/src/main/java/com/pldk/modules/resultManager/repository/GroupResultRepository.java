package com.pldk.modules.resultManager.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.constant.StatusConst;
import com.pldk.modules.resultManager.domain.Result;
import com.pldk.modules.system.repository.BaseRepository;

/**
 * @author Pldk
 * @date 2019/06/29
 */
public interface GroupResultRepository extends BaseRepository<Result, Long> {
	
	  /**
     * 审核数据
     * @param id 主键ID
     * @param auditStatus 审核状态
     * @param reason 拒绝原因
     */
	@Modifying
	@Transactional
    @Query(value="update ts_result set audit_status=?2,reason=?3 where id=?1" ,nativeQuery = true)
    public int auditRes(Long id,String auditStatus,String reason);
	
}