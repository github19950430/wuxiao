package com.pldk.modules.modelstudio.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pldk.modules.modelstudio.domain.MasterMember;
import com.pldk.modules.modelstudio.repository.MasterMemberRepository;
import com.pldk.modules.modelstudio.service.MasterMemberService;

/**
 * @author suhp
 * @date 2019/07/16
 */
@Service
public class MasterMemberServiceImpl implements MasterMemberService{
	
	@Autowired
	private MasterMemberRepository masterMemberRepository;
	
	
	@Override
	public MasterMember getById(Long id) {
		return masterMemberRepository.findById(id).orElse(null);
	}

	@Override
	public MasterMember save(MasterMember masterMember) {
		return masterMemberRepository.save(masterMember);
	}

	@Override
	public List<MasterMember> getMasterMemberByStudio(Long id) {
		return masterMemberRepository.findAllByMasterMemberId(id);
	}
	
	@Override
	public void delMember(Long id) {
		masterMemberRepository.deleteById(id);
	}
	
}
