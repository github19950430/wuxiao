package com.pldk.modules.resultManager.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.resultManager.domain.Result;
import com.pldk.modules.resultManager.domain.ResultMember;

/**
 * @author Pldk
 * @date 2019/06/29
 */
public interface ResultMemberService {

   
    /**
     * 
     * 方法描述：查询列表
     * @author  wangchangwei
     * @addtime 2019年7月13日
     * @param  
     * @param result
     * @return
     */
    public List<ResultMember> getList(ResultMember resultMember);
    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    ResultMember getById(Long id);

    /**
     * 保存数据
     * @param result 实体对象
     */
    ResultMember save(ResultMember resultMember);
    
    /**
     * 
     * 方法描述：删除
     * @author  wangchangwei
     * @addtime 2019年8月1日
     * @param  
     * @param id
     */
    public void delMember(Long id) ;

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}