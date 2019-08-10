package com.pldk.modules.system.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.system.domain.Dept;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2018/12/02
 */
public interface DeptService {

    /**
     * 获取组织列表数据
     * @param example 查询实例
     * @param sort 排序对象
     */
    List<Dept> getListByExample(Example<Dept> example, Sort sort);

    /**
     * 获取排序最大值
     * @param pid 父菜单ID
     */
    Integer getSortMax(Long pid);

    /**
     * 根据父级组织ID获取本级全部组织
     * @param pid 父组织ID
     * @param notId 需要排除的组织ID
     */
    List<Dept> getListByPid(Long pid, Long notId);

    /**
     * 保存多个组织
     * @param deptList 组织实体类列表
     */
    List<Dept> save(List<Dept> deptList);

    /**
     * 根据组织管理ID查询组织管理数据
     * @param id 组织管理ID
     */
    Dept getById(Long id);
    
    
    /**
     * 
     * 方法描述：根据工会名称查询记录
     * @author  wangchangwei
     * @addtime 2019年7月6日
     * @param  
     * @param title
     * @return
     */
    Dept getByTitle(String title);

    /**
     * 根据ID查找子孙组织
     * @param id [id]形式
     */
    List<Dept> getListByPidLikeOk(Long id);

    /**
     * 保存组织管理
     * @param dept 组织管理实体类
     */
    Dept save(Dept dept);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}

