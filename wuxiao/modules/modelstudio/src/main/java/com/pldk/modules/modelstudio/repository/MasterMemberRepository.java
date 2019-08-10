package com.pldk.modules.modelstudio.repository;

import java.util.List;

import com.pldk.modules.modelstudio.domain.MasterMember;
import com.pldk.modules.system.repository.BaseRepository;

/**
 * 
 * @author suhp
 *
 */
public interface MasterMemberRepository extends BaseRepository<MasterMember, Long>{
	
	 List<MasterMember> findAllByMasterMemberId(Long id);
	
}
