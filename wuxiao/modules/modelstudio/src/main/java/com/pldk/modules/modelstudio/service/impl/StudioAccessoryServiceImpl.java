package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.StudioAccessory;
import com.pldk.modules.modelstudio.repository.StudioAccessoryRepository;
import com.pldk.modules.modelstudio.service.StudioAccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
@Service
public class StudioAccessoryServiceImpl implements StudioAccessoryService {

    @Autowired
    private StudioAccessoryRepository studioAccessoryRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public StudioAccessory getById(Long id) {
        return studioAccessoryRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<StudioAccessory> getPageList(Example<StudioAccessory> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return studioAccessoryRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param studioAccessory 实体对象
     */
    @Override
    public StudioAccessory save(StudioAccessory studioAccessory) {
        return studioAccessoryRepository.save(studioAccessory);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return studioAccessoryRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List<StudioAccessory> findAllByStudioId(Long studioId) {
        return studioAccessoryRepository.findAllByStudioId(studioId);
    }

    @Override
    public int deleteById(Long id) {
        return studioAccessoryRepository.deleteAllById(id);
    }
}