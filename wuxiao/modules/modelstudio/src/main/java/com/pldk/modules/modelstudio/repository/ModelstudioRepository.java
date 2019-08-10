package com.pldk.modules.modelstudio.repository;

import com.pldk.modules.modelstudio.domain.Modelstudio;
import com.pldk.modules.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Map;

/**
 * @author Pldk
 * @date 2019/07/10
 */
public interface ModelstudioRepository extends BaseRepository<Modelstudio, Long>, JpaSpecificationExecutor<Modelstudio> {

    Modelstudio findAllByStudioName(String studioName);

    List<Modelstudio> findAll();

    @Modifying
    @Transactional
    @Query(value = "update ts_modelstudio set audit_status = 1 where id = ?1",nativeQuery = true)
    int update(Long id);

    @QueryHints(value = { @QueryHint(name = "HINT_COMMENT", value = "a query for pageable")})
    @Query(value = "SELECT d.id id,d.title title,u.username username,u.phone phone,(SELECT count(*) FROM ts_modelstudio WHERE audit_status = 1 AND state = :state AND flat_type = d.id) nnum,(SELECT count(*) FROM ts_modelstudio WHERE audit_status = 2 AND state = :state AND flat_type = d.id) num,(SELECT count(*) FROM ts_modelstudio WHERE audit_status = 4 AND state = :state AND flat_type = d.id) number,(SELECT count(*) FROM ts_modelstudio WHERE audit_status = 3 AND state = :state AND flat_type = d.id) numm FROM sys_dept d LEFT JOIN sys_user u ON d.id = u.dept_id WHERE d.level <= 2 GROUP BY d.id",
            countQuery = "SELECT count(*) FROM sys_dept where level <= 2",nativeQuery = true)
    Page<Map<String,Object>> findByStateMatch(@Param("state") Byte state, Pageable pageable);
}