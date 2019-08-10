package com.pldk.modules.resultManager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.resultManager.domain.Result;
import com.pldk.modules.resultManager.repository.ResultRepository;
import com.pldk.modules.resultManager.service.ScienceResultService;

/**
 * @author Pldk
 * @date 2019/06/29
 */
@Service
public class ScienceResultServiceImpl implements ScienceResultService {

    @Autowired
    private ResultRepository resultRepository;
    
    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Result getById(Long id) {
        return resultRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Result> getPageList(Example<Result> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return resultRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param result 实体对象
     */
    @Override
    public Result save(Result result) {
        return resultRepository.save(result);
    }
    


    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return resultRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    /**
     * 审核数据
     * @param result 实例对象
     */
	@Override
	public int auditRes(Result result) {
		return resultRepository.auditRes(result.getId(),result.getAuditStatus(),result.getReason());
	}
}