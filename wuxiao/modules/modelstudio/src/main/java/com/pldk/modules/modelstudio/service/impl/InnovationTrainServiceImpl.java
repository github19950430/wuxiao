package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.InnovationTrain;
import com.pldk.modules.modelstudio.repository.InnovationTrainRepository;
import com.pldk.modules.modelstudio.service.InnovationTrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/17
 */
@Service
public class InnovationTrainServiceImpl implements InnovationTrainService {

    @Autowired
    private InnovationTrainRepository innovationTrainRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public InnovationTrain getById(Long id) {
        return innovationTrainRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<InnovationTrain> getPageList(Example<InnovationTrain> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return innovationTrainRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param innovationTrain 实体对象
     */
    @Override
    public InnovationTrain save(InnovationTrain innovationTrain) {
        return innovationTrainRepository.save(innovationTrain);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return innovationTrainRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List<InnovationTrain> getAll(Example<InnovationTrain> example) {
        return innovationTrainRepository.findAll(example, Sort.by("createDate"));
    }
}