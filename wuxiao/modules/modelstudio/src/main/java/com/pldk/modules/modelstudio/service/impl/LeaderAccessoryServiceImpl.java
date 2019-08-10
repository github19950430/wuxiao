package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.LeaderAccessory;
import com.pldk.modules.modelstudio.repository.LeaderAccessoryRepository;
import com.pldk.modules.modelstudio.service.LeaderAccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/17
 */
@Service
public class LeaderAccessoryServiceImpl implements LeaderAccessoryService {

    @Autowired
    private LeaderAccessoryRepository leaderAccessoryRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public LeaderAccessory getById(Long id) {
        return leaderAccessoryRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<LeaderAccessory> getPageList(Example<LeaderAccessory> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return leaderAccessoryRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param leaderAccessory 实体对象
     */
    @Override
    public LeaderAccessory save(LeaderAccessory leaderAccessory) {
        return leaderAccessoryRepository.save(leaderAccessory);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return leaderAccessoryRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public int deleteById(Long id) {
        return leaderAccessoryRepository.deleteAllById(id);
    }

    @Override
    public List<LeaderAccessory> getAllByleaderId(Long id) {
        return leaderAccessoryRepository.findAllByLeaderId(id);
    }
}