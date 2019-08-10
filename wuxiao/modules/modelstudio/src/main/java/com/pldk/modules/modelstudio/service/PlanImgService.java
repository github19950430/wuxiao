package com.pldk.modules.modelstudio.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.PlanImg;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/12
 */
public interface PlanImgService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<PlanImg> getPageList(Example<PlanImg> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    PlanImg getById(Long id);

    /**
     * 保存数据
     * @param planImg 实体对象
     */
    PlanImg save(PlanImg planImg);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}