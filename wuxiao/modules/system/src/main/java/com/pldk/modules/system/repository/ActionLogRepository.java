package com.pldk.modules.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pldk.modules.system.domain.ActionLog;

import java.util.List;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {

    /**
     * 根据模型和数据ID查询日志列表
     * @param model 模型（表名）
     * @param recordId 数据ID
     */
    public List<ActionLog> findByModelAndRecordId(String model, Long recordId);
}
