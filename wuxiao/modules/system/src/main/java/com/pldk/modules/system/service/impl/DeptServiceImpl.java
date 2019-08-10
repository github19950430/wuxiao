package com.pldk.modules.system.service.impl;

import com.pldk.common.enums.ResultEnum;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.exception.ResultException;
import com.pldk.modules.system.domain.Dept;
import com.pldk.modules.system.domain.User;
import com.pldk.modules.system.repository.DeptRepository;
import com.pldk.modules.system.repository.UserRepository;
import com.pldk.modules.system.service.DeptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Pldk
 * @date 2018/12/02
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据组织管理ID查询组织管理数据
     * @param id 组织管理ID
     */
    @Override
    @Transactional
    public Dept getById(Long id) {
        return deptRepository.findById(id).orElse(null);
    }

    /**
     * 获取组织列表数据
     * @param example 查询实例
     * @param sort 排序对象
     */
    @Override
    public List<Dept> getListByExample(Example<Dept> example, Sort sort) {
        return deptRepository.findAll(example, sort);
    }

    /**
     * 获取排序最大值
     * @param pid 父菜单ID
     */
    @Override
    public Integer getSortMax(Long pid){
        return deptRepository.findSortMax(pid);
    }

    /**
     * 根据父级组织ID获取本级全部组织
     * @param pid 父组织ID
     * @param notId 需要排除的组织ID
     */
    @Override
    public List<Dept> getListByPid(Long pid, Long notId){
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        return deptRepository.findByPidAndIdNot(sort, pid, notId);
    }

    /**
     * 根据ID查找子孙组织
     * @param id [id]形式
     */
    @Override
    public List<Dept> getListByPidLikeOk(Long id){
        return deptRepository.findByPidsLikeAndStatus("%["+id+"]%", StatusEnum.OK.getCode());
    }

    /**
     * 保存组织管理
     * @param dept 组织管理实体类
     */
    @Override
    public Dept save(Dept dept){
        Dept pdept=deptRepository.findById(dept.getPid()).orElse(null);
        dept.setLevel(pdept.getLevel()+1);
        return deptRepository.save(dept);
    }

    /**
     * 保存多个组织
     * @param deptList 组织实体类列表
     */
    @Override
    public List<Dept> save(List<Dept> deptList){
        return deptRepository.saveAll(deptList);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> ids){
        // 获取与之关联的所有组织
        Set<Dept> treeDepts = new HashSet<>();
        List<Dept> depts = deptRepository.findByIdIn(ids);
        depts.forEach(dept -> {
            treeDepts.add(dept);
            treeDepts.addAll(deptRepository.findByPidsLikeAndStatus("%[" + dept.getId() + "]%", dept.getStatus()));
        });

        treeDepts.forEach(dept -> {
            if(statusEnum == StatusEnum.DELETE){
                List<User> Depts = userRepository.findByDept(dept);
                if(Depts.size() > 0){
                    throw new ResultException(ResultEnum.DEPT_EXIST_USER);
                }
            }
            // 更新关联的所有组织状态
            dept.setStatus(statusEnum.getCode());
        });

        return treeDepts.size() > 0;
    }

	@Override
	public Dept getByTitle(String title) {
		List<Dept> list=deptRepository.findByTitle(title);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}
}

