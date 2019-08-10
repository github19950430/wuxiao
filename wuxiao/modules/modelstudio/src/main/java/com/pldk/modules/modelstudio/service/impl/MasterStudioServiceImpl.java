package com.pldk.modules.modelstudio.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.MasterStudio;
import com.pldk.modules.modelstudio.repository.MasterStudioRepository;
import com.pldk.modules.modelstudio.service.MasterStudioService;

/**
 * @author suhp
 * @date 2019/07/16
 */
@Service
public class MasterStudioServiceImpl implements MasterStudioService {

    @Autowired
    private MasterStudioRepository masterStudioRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public MasterStudio getById(Long id) {
        return masterStudioRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<MasterStudio> getPageList(Example<MasterStudio> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return masterStudioRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param tsMasterStudio 实体对象
     */
    @Override
    public MasterStudio save(MasterStudio tsMasterStudio) {
        return masterStudioRepository.save(tsMasterStudio);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return masterStudioRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}