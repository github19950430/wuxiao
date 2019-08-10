package com.pldk.modules.system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.system.domain.User;

/**
 * @author Pldk
 * @date 2019/07/01
 */
public interface FirstUserService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<User> getPageList(User user);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    User getById(Long id);

    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    User getByName(String username);

    /**
     * 用户名是否重复
     * @param user 用户对象
     * @return 用户数据
     */
    Boolean repeatByUsername(User user);
    /**
     * 保存数据
     * @param User 实体对象
     */
    User save(User User);
    /**
     * 保存多条数据
     * @param User 实体对象
     */
    public List<User> save(List<User> userList);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}