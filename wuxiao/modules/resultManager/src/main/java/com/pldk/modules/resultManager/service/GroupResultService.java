package com.pldk.modules.resultManager.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.resultManager.domain.Result;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/06/29
 */
public interface GroupResultService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Result> getPageList(Example<Result> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Result getById(Long id);

    /**
     * 保存数据
     * @param result 实体对象
     */
    Result save(Result result);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
    
    /**
     * 审核数据
     * @param result 实例对象
     */
    @Transactional
    int auditRes(Result result);
}