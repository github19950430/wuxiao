package com.pldk.modules.modelstudio.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.Keymember;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/18
 */
public interface KeymemberService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Keymember> getPageList(Example<Keymember> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Keymember getById(Long id);

    /**
     * 保存数据
     * @param keymember 实体对象
     */
    Keymember save(Keymember keymember);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    /**
     * 根据带头人id 查询所有骨干成员
     * @param foregoerId
     * @return
     */
    List<Keymember> findAllByforegoerId(Long foregoerId);

    /**
     * 根据主键ID删除骨干成员
     * @param id
     * @return
     */
    @Transactional
    int deleteById(Long id);
}