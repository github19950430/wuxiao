package com.pldk.modules.auditRecord.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.auditRecord.domain.AuditRecord;
import com.pldk.modules.auditRecord.repository.AuditRecordRepository;
import com.pldk.modules.auditRecord.service.AuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/15
 */
@Service
public class AuditRecordServiceImpl implements AuditRecordService {

    @Autowired
    private AuditRecordRepository auditRecordRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public AuditRecord getById(Long id) {
        return auditRecordRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<AuditRecord> getPageList(Example<AuditRecord> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return auditRecordRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param auditRecord 实体对象
     */
    @Override
    public AuditRecord save(AuditRecord auditRecord) {
        return auditRecordRepository.save(auditRecord);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return auditRecordRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}