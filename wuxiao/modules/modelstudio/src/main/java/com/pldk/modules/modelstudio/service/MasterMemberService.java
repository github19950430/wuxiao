package com.pldk.modules.modelstudio.service;

import java.util.List;

import com.pldk.modules.modelstudio.domain.MasterMember;

/**
 * 
 * @author suhp
 *
 */
public interface  MasterMemberService {
	
	/**
         *   根据ID查询数据
     *  @param id 主键ID
     */
	MasterMember getById(Long id);
	
	
	 /**
          * 保存数据
     * @param masterMember 实体对象
     */
	MasterMember save(MasterMember masterMember);
	
	
	/**
	 * 查询某工作室成员
	 * @param id
	 * @return
	 */
	List<MasterMember> getMasterMemberByStudio(Long id);
	
	void delMember(Long id);
	
}
