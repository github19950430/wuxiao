package com.pldk.modules.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.DateUtil;
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.repository.ExpertUserRepository;
import com.pldk.modules.system.service.DeptService;
import com.pldk.modules.system.service.ExpertUserService;

/**
 * @author Pldk
 * @date 2019/07/01
 */
@Service
public class ExpertUserServiceImpl implements ExpertUserService {

    @Autowired
    private ExpertUserRepository expertUserRepository;

    @Autowired
    private DeptService deptService;
    
    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public User getById(Long id) {
        return expertUserRepository.findById(id).orElse(null);
    }
    
    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    @Override
    public User getByName(String username) {
        return expertUserRepository.findByUsername(username);
    }

    /**
     * 用户名是否存在
     * @param user 用户对象
     * @return 用户数据
     */
    @Override
    public Boolean repeatByUsername(User user) {
        Long id = user.getId() != null ? user.getId() : Long.MIN_VALUE;
        return expertUserRepository.findByUsernameAndIdNot(user.getUsername(), id) != null;
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<User> getPageList(User user) {
    	 // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);
        return expertUserRepository.findAll(new Specification<User>(){

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if(user.getId() != null){
                    preList.add(cb.equal(root.get("id").as(Long.class), user.getId()));
                }
                if(user.getUsername() != null){
                    preList.add(cb.equal(root.get("username").as(String.class), user.getUsername()));
                }
                if(user.getRealname() != null){
                    preList.add(cb.like(root.get("realname").as(String.class), "%"+ user.getRealname() + "%"));
                }
                preList.add(cb.equal(root.get("isExpert").as(Integer.class), 1));
                if(user.getDept() != null){
                    Dept dept = user.getDept();
                    List<Long> deptIn = new ArrayList<>();
                    deptIn.add(dept.getId());
//                    List<Dept> deptList = deptService.getListByPidLikeOk(dept.getId());
//                    deptList.forEach(item -> deptIn.add(item.getId()));

                    Join<User, Dept> join = root.join("dept", JoinType.INNER);
                    CriteriaBuilder.In<Long> in = cb.in(join.get("id").as(Long.class));
                    deptIn.forEach(in::value);
                    preList.add(in);
                    
//                    preList.add(cb.equal(join.get("level").as(Integer.class), dept.getLevel()+1));
                    
                }
                
//                if(user.getReviewType()!=null) {
                	//联机查询
                	/*ReviewType reviewType=user.getReviewType();
                	List<Long> reviewTypeIn = new ArrayList<>();
                	reviewTypeIn.add(reviewType.getId());
                	reviewTypeService.*/
//                }

                // 数据状态
                if(user.getStatus() != null){
                    preList.add(cb.equal(root.get("status").as(Byte.class), user.getStatus()));
                }

                Predicate[] pres = new Predicate[preList.size()];
                return query.where(preList.toArray(pres)).getRestriction();
            }

        }, page);
    }

    /**
     * 保存数据
     * @param User 实体对象
     */
    @Override
    public User save(User User) {
        return expertUserRepository.save(User);
    }

    /**
     * 保存用户列表
     * @param userList 用户实体类
     */
    @Override
    @Transactional
    public List<User> save(List<User> userList){
        return expertUserRepository.saveAll(userList);
    }
    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return expertUserRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

	@Override
	public List<User> findAll(User user) {
		return expertUserRepository.findAll(new Specification<User>(){

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                preList.add(cb.equal(root.get("isExpert").as(Integer.class), 1));
                if(user.getDept() != null){
                    Dept dept = user.getDept();
                    List<Long> deptIn = new ArrayList<>();
                    deptIn.add(dept.getId());
                    Join<User, Dept> join = root.join("dept", JoinType.INNER);
                    CriteriaBuilder.In<Long> in = cb.in(join.get("id").as(Long.class));
                    deptIn.forEach(in::value);
                    preList.add(in);
                }
                
                // 数据状态
                if(user.getStatus() != null){
                    preList.add(cb.equal(root.get("status").as(Byte.class), user.getStatus()));
                }
                if(user.getReviewType()!=null) {//专家评审类别
                	preList.add(cb.equal(root.get("reviewType").as(String.class), user.getReviewType()));
                }
                
                //专家授权开始--结束时间
                preList.add(cb.lessThanOrEqualTo(root.get("expBeginDate").as(String.class), DateUtil.getTime()));
                preList.add(cb.greaterThanOrEqualTo(root.get("expEndDate").as(String.class), DateUtil.getTime()));
               
                

                Predicate[] pres = new Predicate[preList.size()];
                return query.where(preList.toArray(pres)).getRestriction();
            }

        });
    }

}