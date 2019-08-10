package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.PlanVideo;
import com.pldk.modules.modelstudio.repository.PlanVideoRepository;
import com.pldk.modules.modelstudio.service.PlanVideoService;
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
public class PlanVideoServiceImpl implements PlanVideoService {

    @Autowired
    private PlanVideoRepository planVideoRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public PlanVideo getById(Long id) {
        return planVideoRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<PlanVideo> getPageList(Example<PlanVideo> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return planVideoRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param planVideo 实体对象
     */
    @Override
    public PlanVideo save(PlanVideo planVideo) {
        return planVideoRepository.save(planVideo);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return planVideoRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}