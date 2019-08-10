package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.StudioVideo;
import com.pldk.modules.modelstudio.repository.StudioVideoRepository;
import com.pldk.modules.modelstudio.service.StudioVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
@Service
public class StudioVideoServiceImpl implements StudioVideoService {

    @Autowired
    private StudioVideoRepository studioVideoRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public StudioVideo getById(Long id) {
        return studioVideoRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<StudioVideo> getPageList(Example<StudioVideo> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return studioVideoRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param studioVideo 实体对象
     */
    @Override
    public StudioVideo save(StudioVideo studioVideo) {
        return studioVideoRepository.save(studioVideo);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return studioVideoRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List<StudioVideo> getAllByStudioId(Long studioId) {
        return studioVideoRepository.findAllByStudioId(studioId);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return studioVideoRepository.deleteAllById(id);
    }

}