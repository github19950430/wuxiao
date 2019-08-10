package com.pldk.modules.modelstudio.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.Leaderinformation;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public interface LeaderinformationService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Leaderinformation> getPageList(Example<Leaderinformation> example);

    /**
     * 根据ID查询数据  根据带头人ID查询 该工作室带头人信息
     * @param id 主键ID
     */
    Leaderinformation getById(Long id);

    /**
     * 保存数据
     * @param leaderinformation 实体对象
     */
    Leaderinformation save(Leaderinformation leaderinformation);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);


    /**
     * 查询所有带头人
     * @return
     */
    List<Leaderinformation> findAll();
}