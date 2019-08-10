package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.StudioImg;
import com.pldk.modules.modelstudio.repository.StudioImgRepository;
import com.pldk.modules.modelstudio.service.StudioImgService;
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
public class StudioImgServiceImpl implements StudioImgService {

    @Autowired
    private StudioImgRepository studioImgRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public StudioImg getById(Long id) {
        return studioImgRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<StudioImg> getPageList(Example<StudioImg> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return studioImgRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param studioImg 实体对象
     */
    @Override
    public StudioImg save(StudioImg studioImg) {
        return studioImgRepository.save(studioImg);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return studioImgRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List<StudioImg> findAllByStudioId(Long studioId) {
        return studioImgRepository.findAllByStudioId(studioId);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return studioImgRepository.deleteAllById(id);
    }
}