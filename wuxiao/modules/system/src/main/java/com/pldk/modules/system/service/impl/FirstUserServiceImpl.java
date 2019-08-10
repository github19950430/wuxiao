package com.pldk.modules.system.service.impl;

import java.util.ArrayList;
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
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.repository.FirstUserRepository;
import com.pldk.modules.system.service.DeptService;
import com.pldk.modules.system.service.FirstUserService;

/**
 * @author Pldk
 * @date 2019/07/01
 */
@Service
public class FirstUserServiceImpl implements FirstUserService {

    @Autowired
    private FirstUserRepository firstUserRepository;

    @Autowired
    private DeptService deptService;
    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public User getById(Long id) {
        return firstUserRepository.findById(id).orElse(null);
    }
    
    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    @Override
    public User getByName(String username) {
        return firstUserRepository.findByUsername(username);
    }

    /**
     * 用户名是否存在
     * @param user 用户对象
     * @return 用户数据
     */
    @Override
    public Boolean repeatByUsername(User user) {
        Long id = user.getId() != null ? user.getId() : Long.MIN_VALUE;
        return firstUserRepository.findByUsernameAndIdNot(user.getUsername(), id) != null;
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
        return firstUserRepository.findAll(new Specification<User>(){

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
                if(user.getDept() != null){
                    // 联级查询组织
                	// 联级查询组织
                    Dept dept = user.getDept();
                    List<Long> deptIn = new ArrayList<>();
                    deptIn.add(dept.getId());
                    List<Dept> deptList = deptService.getListByPidLikeOk(dept.getId());
                    deptList.forEach(item -> deptIn.add(item.getId()));

                    Join<User, Dept> join = root.join("dept", JoinType.INNER);
                    CriteriaBuilder.In<Long> in = cb.in(join.get("id").as(Long.class));
                    deptIn.forEach(in::value);
                    preList.add(in);
                    
                    preList.add(cb.equal(join.get("level").as(Integer.class), 2));
                    
                }
                

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
        return firstUserRepository.save(User);
    }

    /**
     * 保存用户列表
     * @param userList 用户实体类
     */
    @Override
    @Transactional
    public List<User> save(List<User> userList){
        return firstUserRepository.saveAll(userList);
    }
    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return firstUserRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}