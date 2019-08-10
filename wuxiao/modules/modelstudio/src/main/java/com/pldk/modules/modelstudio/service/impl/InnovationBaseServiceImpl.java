package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain. InnovationBase;
import com.pldk.modules.modelstudio.repository. InnovationBaseRepository;
import com.pldk.modules.modelstudio.service. InnovationBaseService;
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
public class InnovationBaseServiceImpl implements InnovationBaseService {

    @Autowired
    private InnovationBaseRepository InnovationBaseRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public InnovationBase getById(Long id) {
        return  InnovationBaseRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page< InnovationBase> getPageList(Example< InnovationBase> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return  InnovationBaseRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param  InnovationBase 实体对象
     */
    @Override
    public InnovationBase save( InnovationBase  InnovationBase) {
        return  InnovationBaseRepository.save( InnovationBase);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return  InnovationBaseRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}