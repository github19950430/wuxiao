package com.pldk.modules.foregoerManager.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.foregoerManager.domain.Foregoer;
import com.pldk.modules.foregoerManager.repository.ForegoerRepository;
import com.pldk.modules.foregoerManager.service.ForegoerService;
import com.pldk.modules.system.domain.Dept;

/**
 * @author Pldk
 * @date 2019/07/01
 */
@Service
public class ForegoerServiceImpl implements ForegoerService {

    @Autowired
    private ForegoerRepository foregoerRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Foregoer getById(Long id) {
        return foregoerRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Foregoer> getPageList(Example<Foregoer> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return foregoerRepository.findAll(example, page);
    }
    
    /**
     * 获取列表数据
     * @param example 查询实例
     * @return 返回列表数据
     */
    @Override
    public List<Foregoer> getList(Foregoer foregoer) {
    	return foregoerRepository.findAll(new Specification<Foregoer>(){

            @Override
            public Predicate toPredicate(Root<Foregoer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(foregoer.getName() != null){
                    preList.add(cb.equal(root.get("name").as(String.class), foregoer.getName()));
                }
                if(foregoer.getIdentityNum()!=null) {
                	preList.add(cb.equal(root.get("identityNum").as(String.class), foregoer.getIdentityNum()));
                }
                if(foregoer.getUnitType()!=0) {
                	preList.add(cb.equal(root.get("unitType").as(Integer.class), foregoer.getUnitType()));
                }
                if(foregoer.getDept() != null){
                	// 联级查询组织
                    Dept dept = foregoer.getDept();
                    List<Long> deptIn = new ArrayList<>();
                    deptIn.add(dept.getId());

                    Join<Foregoer, Dept> join = root.join("dept", JoinType.INNER);
                    if(dept.getLevel()==2) {
                    	preList.add(cb.equal(join.get("pid").as(Integer.class), dept.getId()));
                    	preList.add(cb.equal(join.get("level").as(Integer.class), 3));
                    }else {
                       preList.add(cb.equal(join.get("level").as(Integer.class), dept.getLevel()));
                       CriteriaBuilder.In<Long> in = cb.in(join.get("id").as(Long.class));
                       deptIn.forEach(in::value);
                       preList.add(in);
                    }
                }

                Predicate[] pres = new Predicate[preList.size()];
                return query.where(preList.toArray(pres)).getRestriction();
            }

        });
    }

    /**
     * 保存数据
     * @param foregoer 实体对象
     */
    @Override
    public Foregoer save(Foregoer foregoer) {
        return foregoerRepository.save(foregoer);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return foregoerRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

	@Override
	public Page<Foregoer> getPageList(Foregoer foregoer) {
		 // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return foregoerRepository.findAll(new Specification<Foregoer>(){

            @Override
            public Predicate toPredicate(Root<Foregoer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(foregoer.getName() != null){
                    preList.add(cb.equal(root.get("name").as(String.class), foregoer.getName()));
                }
                if(foregoer.getIdentityNum()!=null) {
                	preList.add(cb.equal(root.get("identityNum").as(String.class), foregoer.getIdentityNum()));
                }
                if(foregoer.getUnitType()!=0) {
                	preList.add(cb.equal(root.get("unitType").as(Integer.class), foregoer.getUnitType()));
                }
                if(foregoer.getDept() != null){
                	// 联级查询组织
                    Dept dept = foregoer.getDept();
                    List<Long> deptIn = new ArrayList<>();
                    deptIn.add(dept.getId());

                    Join<Foregoer, Dept> join = root.join("dept", JoinType.INNER);
                    if(dept.getLevel()==2) {
                    	preList.add(cb.equal(join.get("pid").as(Integer.class), dept.getId()));
                    	preList.add(cb.equal(join.get("level").as(Integer.class), 3));
                    }else {
                       preList.add(cb.equal(join.get("level").as(Integer.class), dept.getLevel()));
                       CriteriaBuilder.In<Long> in = cb.in(join.get("id").as(Long.class));
                       deptIn.forEach(in::value);
                       preList.add(in);
                    }
                }

                Predicate[] pres = new Predicate[preList.size()];
                return query.where(preList.toArray(pres)).getRestriction();
            }

        }, page);
	}
}