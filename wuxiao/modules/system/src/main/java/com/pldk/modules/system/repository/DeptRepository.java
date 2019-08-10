package com.pldk.modules.system.repository;

import com.pldk.common.constant.StatusConst;
import com.pldk.modules.system.domain.Dept;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Pldk
 * @date 2018/12/02
 */
public interface DeptRepository extends BaseRepository<Dept, Long> {

    /**
     * 查找多个组织
     * @param ids id列表
     */
    public List<Dept> findByIdIn(List<Long> ids);

    /**
     * 获取排序最大值
     * @param pid 父组织ID
     */
    @Query("select max(sort) from Menu m where m.pid = ?1 and m.status <> " + StatusConst.DELETE)
    public Integer findSortMax(long pid);

    /**
     * 根据父ID查找子孙组织
     * @param pids pid列表
     */
    public List<Dept> findByPidsLikeAndStatus(String pids, Byte status);

    /**
     * 根据父级组织ID获取本级全部组织
     * @param sort 排序对象
     * @param pid 父组织ID
     * @param notId 需要排除的组织ID
     */
    public List<Dept> findByPidAndIdNot(Sort sort, long pid, long notId);
    /**
     * 
     * 方法描述：根据组织名称查询记录
     * @author  wangchangwei
     * @addtime 2019年7月6日
     * @param  
     * @param name
     * @return
     */
    public List<Dept> findByTitle(String title);
}

