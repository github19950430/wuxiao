package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.StudioProfile;
import com.pldk.modules.modelstudio.repository.StudioProfileRepository;
import com.pldk.modules.modelstudio.service.StudioProfileService;
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
public class StudioProfileServiceImpl implements StudioProfileService {

    @Autowired
    private StudioProfileRepository studioProfileRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public StudioProfile getById(Long id) {
        return studioProfileRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<StudioProfile> getPageList(Example<StudioProfile> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return studioProfileRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param studioProfile 实体对象
     */
    @Override
    public StudioProfile save(StudioProfile studioProfile) {
        return studioProfileRepository.save(studioProfile);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return studioProfileRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}