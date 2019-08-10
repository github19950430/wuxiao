package com.pldk.modules.resultManager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.enums.UnitTypeEnum;
import com.pldk.common.utils.DateUtil;
import com.pldk.common.utils.HttpServletUtil;
import com.pldk.modules.resultManager.domain.Result;
import com.pldk.modules.resultManager.repository.ResultRepository;
import com.pldk.modules.resultManager.service.ResultService;
import com.pldk.modules.system.domain.Dept;

/**
 * @author Pldk
 * @date 2019/06/29
 */
@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;
    
    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Result getById(Long id) {
        return resultRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Result> getPageList(Example<Result> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return resultRepository.findAll(example, page);
    }

    @Override
    public Page<Result> getPageList(Result result){
    	 // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return resultRepository.findAll(new Specification<Result>(){

            @Override
            public Predicate toPredicate(Root<Result> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(result.getResultName() != null){
                    preList.add(cb.equal(root.get("resultName").as(String.class), result.getResultName()));
                }
                if(result.getAuditStatusList()!=null) {
                	In<String> in = cb.in(root.get("auditStatus"));
                	String str=result.getAuditStatusList();
                	if(str.contains(",")) {
                		String[] auditStatusList=str.split(",");
                		for (String id : auditStatusList) {
                    		in.value(id);
                		}
                		preList.add(in);
                	}else {
                		in.value(str);
                		preList.add(in);
                	}
                		  
                }
                if(result.getAuditStatus()!=null) {
                	preList.add(cb.equal(root.get("auditStatus").as(String.class), result.getAuditStatus()));
                }
                if(result.getIndustry()!=null) {
                	preList.add(cb.equal(root.get("industry").as(String.class), result.getIndustry()));
                }
                if(result.getYear()!=null) {
                	preList.add(cb.greaterThanOrEqualTo(root.get("declareDate").as(Date.class), DateUtil.strToDateLong(result.getYear()+"-01-01 00:00:00")));
                    preList.add(cb.lessThanOrEqualTo(root.get("declareDate").as(Date.class),  DateUtil.strToDateLong(Integer.parseInt(result.getYear())+1+"-01-01 00:00:00")));
                }
                preList.add(cb.equal(root.get("unitType").as(Integer.class), UnitTypeEnum.LABORUN_UNIT_TYPE.getCode()));
                if(result.getDept() != null){
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
                    
                }

                Predicate[] pres = new Predicate[preList.size()];
                return query.where(preList.toArray(pres)).getRestriction();
            }

        }, page);
    }
    
    
    @Override
    public List<Result> getList(Result result){
        return resultRepository.findAll(new Specification<Result>(){

            @Override
            public Predicate toPredicate(Root<Result> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(result.getResultName() != null){
                    preList.add(cb.equal(root.get("resultName").as(String.class), result.getResultName()));
                }
                if(result.getAuditStatusList()!=null) {
                	In<String> in = cb.in(root.get("auditStatus"));
                	String str=result.getAuditStatusList();
                	if(str.contains(",")) {
                		String[] auditStatusList=str.split(",");
                		for (String id : auditStatusList) {
                    		in.value(id);
                		}
                		preList.add(in);
                	}else {
                		preList.add(cb.equal(root.get("auditStatus").as(String.class), result.getAuditStatusList()));
                	}
                		  
                }
                if(result.getAuditStatus()!=null) {
                	preList.add(cb.equal(root.get("auditStatus").as(String.class), result.getAuditStatus()));
                }
                if(result.getYear()!=null) {
                	preList.add(cb.greaterThanOrEqualTo(root.get("declareDate").as(Date.class), DateUtil.strToDateLong(result.getYear()+"-01-01 00:00:00")));
                    preList.add(cb.lessThanOrEqualTo(root.get("declareDate").as(Date.class),  DateUtil.strToDateLong(Integer.parseInt(result.getYear())+1+"-01-01 00:00:00")));
                }
                preList.add(cb.equal(root.get("unitType").as(Integer.class), UnitTypeEnum.LABORUN_UNIT_TYPE.getCode()));
                if(result.getDept() != null){
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
                    
                }
                Predicate[] pres = new Predicate[preList.size()];
                return query.where(preList.toArray(pres)).getRestriction();
            }
        });
    }
    /**
     * 保存数据
     * @param result 实体对象
     */
    @Override
    public Result save(Result result) {
        return resultRepository.save(result);
    }
    


    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return resultRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    /**
     * 审核数据
     * @param result 实例对象
     */
	@Override
	public int auditRes(Result result) {
		return resultRepository.auditRes(result.getId(),result.getAuditStatus(),result.getReason());
	}
	
	/**
	 * 上报成果
	 * @param result 实例对象
	 */
	@Override
	public int report(Result result) {
		return resultRepository.report(result.getId(),result.getAuditStatus());
	}

	/**
     * 成果审核汇总
     * @param deptName 工会名称
     * @param auditStatus 审核状态
     * @param deptId 工会Id
     * @param level 工会级别
     */
	@Override
	public Page<Map<String, Object>> getCollectList(String deptName, List<String> auditStatus,Long deptId,Integer level) {
		Integer pageIndex = HttpServletUtil.getParameterInt("page", 1);
        Integer pageSize = HttpServletUtil.getParameterInt("size", 10);
        PageRequest page=PageRequest.of(pageIndex-1, pageSize);
		if(deptName==null) {
			deptName="";
		}
		Page<Map<String, Object>> list =resultRepository.findByNameMatch(deptName, auditStatus,deptId,level,page);
        return list;
    }

	@Override
	public Page<Map<String, Object>> getCollectList(String deptName) {
		Integer pageIndex = HttpServletUtil.getParameterInt("page", 1);
        Integer pageSize = HttpServletUtil.getParameterInt("size", 10);
        PageRequest page=PageRequest.of(pageIndex-1, pageSize);
		if(deptName==null) {
			deptName="";
		}
		Page<Map<String, Object>> list =resultRepository.findByNameMatch(deptName,page);
        return list;
	}

	@Override
	public int findCurretDayResultCount() {
		return resultRepository.findCurretDayResultCount();
	}

	@Override
	public Page<Result> getReviewPageList(Result result,List<Long> resuls) {
		 // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return resultRepository.findAll(new Specification<Result>(){

            @Override
            public Predicate toPredicate(Root<Result> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(result.getResultName() != null){
                    preList.add(cb.equal(root.get("resultName").as(String.class), result.getResultName()));
                }
                In<Long> inId = cb.in(root.get("id"));
                if(resuls.size()>0) {
	        		for (Long id : resuls) {
	        			inId.value(id);
	        		}
	        		preList.add(inId);
        		}else {
        			inId.value(0L);
        			preList.add(inId);
        		}
            	In<String> in = cb.in(root.get("auditStatus"));
            	String str="7,8,9";
            	if(str.contains(",")) {
            		String[] auditStatusList=str.split(",");
            		for (String id : auditStatusList) {
                		in.value(id);
            		}
            		preList.add(in);
            	}
                // 数据状态
                if(result.getStatus() != null){
                    preList.add(cb.equal(root.get("status").as(Byte.class), result.getStatus()));
                }
                Predicate[] pres = new Predicate[preList.size()];
                Predicate list= query.where(preList.toArray(pres)).getRestriction();
                return list;
            }

        }, page);
    }
	
	
	@Override
	public Page<Result> getReviewPageList(Result result) {
		 // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return resultRepository.findAll(new Specification<Result>(){

            @Override
            public Predicate toPredicate(Root<Result> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(result.getResultName() != null){
                    preList.add(cb.equal(root.get("resultName").as(String.class), result.getResultName()));
                }
                In<Long> inId = cb.in(root.get("id"));
            	In<String> in = cb.in(root.get("auditStatus"));
            	String str="7,8,9";
            	if(str.contains(",")) {
            		String[] auditStatusList=str.split(",");
            		for (String id : auditStatusList) {
                		in.value(id);
            		}
            		preList.add(in);
            	}
                // 数据状态
                if(result.getStatus() != null){
                    preList.add(cb.equal(root.get("status").as(Byte.class), result.getStatus()));
                }
                Predicate[] pres = new Predicate[preList.size()];
                Predicate list= query.where(preList.toArray(pres)).getRestriction();
                return list;
            }

        }, page);
    }


	@Override
	public List<Long> findAllResultByUserId(Long userId) {
		List<Long> list =resultRepository.findAllResultByUserId(userId);
		return list;
	}

	/**
     * 成果上报审核记录(分页)
     * @param
     */
	@Override
	public Page<Map<String,Object>> findAllResultById(Long id) {
		PageRequest page = PageSort.pageRequest();
		return resultRepository.findAllResultById(id, page);
	}
}