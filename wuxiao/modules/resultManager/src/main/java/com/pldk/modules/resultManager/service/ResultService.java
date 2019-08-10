package com.pldk.modules.resultManager.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.resultManager.domain.Result;

/**
 * @author Pldk
 * @date 2019/06/29
 */
public interface ResultService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Result> getPageList(Example<Result> example);
    
    /**
     * 工会成果汇总列表
     * @param auditStatus 审核状态
     * @param deptName 公会名称
     */
    Page<Map<String, Object>> getCollectList(String deptName);
    
    /**
     * 工会成果汇总列表
     * @param auditStatus 审核状态
     * @param deptName 公会名称
     */
    Page<Map<String,Object>> getCollectList(String deptName, List<String> auditStatus,Long deptId,Integer level);
    /**
     * 
     * 方法描述：查询列表
     * @author  wangchangwei
     * @addtime 2019年7月13日
     * @param  
     * @param result
     * @return
     */
    public List<Result> getList(Result result);
    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Result> getPageList(Result result);
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
    
    /**
     * 上报成果
     * @param result 实例对象
     */
    @Transactional
    int report(Result result);
    
    /**
     * 
     * 方法描述：当天成果数量
     * @author  wangchangwei
     * @addtime 2019年7月15日
     * @param  
     * @return
     */
    int findCurretDayResultCount();
    /**
     * 专家查看列表页面
     * @param example
     * @return
     */
    public Page<Result> getReviewPageList(Result result);
    /**
     * 专家查看列表页面
     * @param example
     * @return
     */
    Page<Result> getReviewPageList(Result result,List<Long> resuls);
    
    public List<Long> findAllResultByUserId(Long userId);
    
    /**
     * 成果上报审核记录列表页面
     * @param id
     * @return
     */
    public Page<Map<String,Object>> findAllResultById(Long id);
}