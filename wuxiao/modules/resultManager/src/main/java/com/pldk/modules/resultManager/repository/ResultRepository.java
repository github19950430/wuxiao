package com.pldk.modules.resultManager.repository;

import java.util.List;
import java.util.Map;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.modules.resultManager.domain.Result;
import com.pldk.modules.system.repository.BaseRepository;

/**
 * @author Pldk
 * @date 2019/06/29
 */
public interface ResultRepository extends BaseRepository<Result, Long>,JpaSpecificationExecutor<Result>  {
	
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
	
	/**
	 * 上报成果
	 * @param id 主键ID
	 * @param auditStatus 审核状态
	 */
	@Modifying
	@Transactional
	@Query(value="update ts_result set audit_status=?2 where id=?1" ,nativeQuery = true)
	public int report(Long id,String auditStatus);
	
	 /**
     * 二级成果审核汇总
     * @param deptName 工会名称
     * @param auditStatus 审核状态
     * @param deptId 工会Id
     * @param level 工会级别
     */
	@QueryHints(value = { @QueryHint(name = "HINT_COMMENT", value = "a query for pageable")})
	@Query(value="select r.dept_id,e.title, e.username, e.phone, count(r.dept_id) num, r.audit_status from ts_result r \r\n" + 
			"left join (select u.*,d.title from sys_user u left JOIN sys_dept d on d.id = u.dept_id group by u.dept_id ) as e on r.dept_id=e.dept_id \r\n" + 
			"where e.title like CONCAT('%',:deptName,'%') and r.audit_status in (:auditStatus) AND r.`dept_id` IN (SELECT id FROM sys_dept WHERE pid=:deptId and level=:level)   group by r.dept_id   "  
			,countQuery = "SELECT count(*) from ts_result r \\r\\n\" + \r\n" + 
					"			\"left join (select u.*,d.title from sys_user u left JOIN sys_dept d on d.id = u.dept_id group by u.dept_id ) as e on r.dept_id=e.dept_id \\r\\n\" + \r\n" + 
					"			\"where e.title like CONCAT('%',:deptName,'%') and r.audit_status in (:auditStatus) AND r.`dept_id` IN (SELECT id FROM sys_dept WHERE pid=:deptId and level=:level)   group by r.dept_id  "
			,nativeQuery = true)
	public Page<Map<String, Object>> findByNameMatch( @Param("deptName") String deptName, @Param("auditStatus") List<String> auditStatus,@Param("deptId")Long deptId,@Param("level")Integer level,Pageable pageable);

	/**
     * 一级成果审核汇总
     * @param deptName 工会名称
     * @param auditStatus 审核状态
     * @param deptId 工会Id
     * @param level 工会级别
     */
	@QueryHints(value = { @QueryHint(name = "HINT_COMMENT", value = "a query for pageable")})
	@Query(value="SELECT u.username, u.phone,d.title,u.dept_id,0 as unVerifyCount,0 as verifyPassCount, 0 as verifyUnpassCount FROM sys_user u LEFT JOIN sys_dept d ON d.id = u.dept_id where d.level=2 and d.title  like CONCAT('%',:deptName,'%')  GROUP BY u.dept_id  ", 
			countQuery = "SELECT count(*) FROM sys_user u LEFT JOIN sys_dept d ON d.id = u.dept_id where d.level=2 and d.title  like CONCAT('%',:deptName,'%')  GROUP BY u.dept_id ",
			nativeQuery = true)
	public Page<Map<String,Object>> findByNameMatch(@Param("deptName") String deptName,Pageable pageable);
  
	@Query(value="SELECT count(1) from ts_result where DATE_FORMAT(create_date,'%y-%m-%d')=DATE_FORMAT(now(),'%y-%m-%d') ",nativeQuery = true)
	public int findCurretDayResultCount();
	
	@Query(value="select result_id from tr_result_user where user_id=?1",nativeQuery = true)
	public List<Long> findAllResultByUserId( @Param("userId") Long userId);
	
	/**
     * 成果上报审核记录(分页)
     * @param auditId 审核id
     * @param pageable 实例对象
     */
	@QueryHints(value = { @QueryHint(name = "HINT_COMMENT", value = "a query for pageable")})
	@Query(value="select id, audit_id as auditId, type, audit_status as auditStatus, name, create_date as createDate, "
			+ "dept_name as deptName, status from ts_audit_record where audit_id=?1  ", countQuery = "SELECT count(*) FROM ts_audit_record WHERE audit_id = ?1",nativeQuery = true)
	public Page<Map<String,Object>> findAllResultById( @Param("id") Long auditId, Pageable pageable);
}

