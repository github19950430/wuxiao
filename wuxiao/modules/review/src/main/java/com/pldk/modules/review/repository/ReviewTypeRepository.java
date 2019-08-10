package com.pldk.modules.review.repository;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.pldk.modules.review.domain.ReviewType;
import com.pldk.modules.system.repository.BaseRepository;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public interface ReviewTypeRepository extends BaseRepository<ReviewType, Long> {
	
	/**
     * 根据父级菜单ID获取 所有一级审核项类型
     * @param sort 排序对象
     * @param pid 父菜单ID
     */
    public List<ReviewType> findAllByParentId(Sort sort,long pid);
    
    public ReviewType findByNameAndIdNot(String name, Long id);
}