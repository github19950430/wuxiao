package com.pldk.modules.resultManager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.common.enums.UnitTypeEnum;
import com.pldk.common.utils.DateUtil;
import com.pldk.modules.resultManager.domain.Result;
import com.pldk.modules.resultManager.domain.ResultMember;
import com.pldk.modules.resultManager.repository.ResultMemberRepository;
import com.pldk.modules.resultManager.service.ResultMemberService;
import com.pldk.modules.system.domain.Dept;

/**
 * @author Pldk
 * @date 2019/06/29
 */
@Service
public class ResultMemberServiceImpl implements ResultMemberService {

    @Autowired
    private ResultMemberRepository resultMemberRepository;

	@Override
	public List<ResultMember> getList(ResultMember resultMember) {
		return resultMemberRepository.findAll(new Specification<ResultMember>(){

            @Override
            public Predicate toPredicate(Root<ResultMember> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(resultMember.getResult() != null){
                	Result result=resultMember.getResult();
                    Join<ResultMember, Result> join = root.join("result", JoinType.INNER);
                    preList.add(cb.equal(join.get("id").as(Integer.class), result.getId()));
                }
                /*if(result.getDept() != null){
                	// 联级查询组织
                    Dept dept = result.getDept();
                    List<Long> deptIn = new ArrayList<>();
                    deptIn.add(dept.getId());

                    Join<Result, Dept> join = root.join("dept", JoinType.INNER);
                    
                    if(dept.getLevel()==2) {
                    	preList.add(cb.equal(join.get("pid").as(Integer.class), dept.getId()));
                    	preList.add(cb.equal(join.get("level").as(Integer.class), 3));
                    }else {
                       preList.add(cb.equal(join.get("level").as(Integer.class), dept.getLevel()));
                       CriteriaBuilder.In<Long> in = cb.in(join.get("id").as(Long.class));
                       deptIn.forEach(in::value);
                       preList.add(in);
                    }
                    
                }*/
                Predicate[] pres = new Predicate[preList.size()];
                return query.where(preList.toArray(pres)).getRestriction();
            }
        });
	}

	@Override
	public ResultMember getById(Long id) {
		return resultMemberRepository.findById(id).orElse(null);
	}

	@Override
	public ResultMember save(ResultMember resultMember) {
		return resultMemberRepository.save(resultMember);
	}

	@Override
	public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
		return resultMemberRepository.updateStatus(statusEnum.getCode(), idList) > 0;
	}

	@Override
	public void delMember(Long id) {
		resultMemberRepository.deleteById(id);
	}
    
  
}