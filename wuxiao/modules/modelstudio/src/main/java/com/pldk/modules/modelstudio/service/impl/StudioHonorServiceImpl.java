package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.StudioHonor;
import com.pldk.modules.modelstudio.repository.StudioHonorRepository;
import com.pldk.modules.modelstudio.service.StudioHonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/22
 */
@Service
public class StudioHonorServiceImpl implements StudioHonorService {

    @Autowired
    private StudioHonorRepository studioHonorRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public StudioHonor getById(Long id) {
        return studioHonorRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<StudioHonor> getPageList(Example<StudioHonor> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return studioHonorRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param studioHonor 实体对象
     */
    @Override
    public StudioHonor save(StudioHonor studioHonor) {
        return studioHonorRepository.save(studioHonor);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return studioHonorRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List<StudioHonor> findAllByStudioId(Long id) {
        return studioHonorRepository.findAllByStudioId(id);
    }

    @Override
    public int deleteById(Long id) {
        return studioHonorRepository.deleteAllById(id);
    }
}