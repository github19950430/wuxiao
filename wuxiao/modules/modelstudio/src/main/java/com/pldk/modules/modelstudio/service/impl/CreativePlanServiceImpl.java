package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.CreativePlan;
import com.pldk.modules.modelstudio.repository.CreativePlanRepository;
import com.pldk.modules.modelstudio.service.CreativePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Service
public class CreativePlanServiceImpl implements CreativePlanService {

    @Autowired
    private CreativePlanRepository creativePlanRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public CreativePlan getById(Long id) {
        return creativePlanRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<CreativePlan> getPageList(Example<CreativePlan> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return creativePlanRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param creativePlan 实体对象
     */
    @Override
    public CreativePlan save(CreativePlan creativePlan) {
        return creativePlanRepository.save(creativePlan);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return creativePlanRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}