package com.pldk.modules.modelstudio.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.LeaderAccessory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/17
 */
public interface LeaderAccessoryService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<LeaderAccessory> getPageList(Example<LeaderAccessory> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    LeaderAccessory getById(Long id);

    /**
     * 保存数据
     * @param leaderAccessory 实体对象
     */
    LeaderAccessory save(LeaderAccessory leaderAccessory);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    /**
     * 根据主键ID删除附件
     * @param id
     * @return
     */
    @Transactional
    int deleteById(Long id);

    /**
     * 根据带头人ID 查询带头人附件
     * @param id
     * @return
     */
    List<LeaderAccessory> getAllByleaderId(Long id);
}