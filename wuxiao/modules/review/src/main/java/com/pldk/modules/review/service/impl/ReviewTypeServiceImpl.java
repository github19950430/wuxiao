package com.pldk.modules.review.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.review.domain.ReviewType;
import com.pldk.modules.review.repository.ReviewTypeRepository;
import com.pldk.modules.review.service.ReviewTypeService;

/**
 * @author Pldk
 * @date 2019/07/11
 */
@Service
public class ReviewTypeServiceImpl implements ReviewTypeService {

    @Autowired
    private ReviewTypeRepository reviewTypeRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public ReviewType getById(Long id) {
        return reviewTypeRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<ReviewType> getPageList(Example<ReviewType> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);
        return reviewTypeRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param reviewType 实体对象
     */
    @Override
    @Transactional
    public ReviewType save(ReviewType reviewType) {
    	ReviewType set=reviewTypeRepository.save(reviewType);
    	if(set!=null&&reviewType.getLevel()==2&&reviewType.getParentId()!=0) {
    		ReviewType type=new ReviewType();
    		type.setId(set.getParentId());
    		type.setIsChild((byte)2);
    		reviewTypeRepository.save(type);
    	}
        return set;
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return reviewTypeRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

	@Override
	public List<ReviewType> getByOneTypeListByPid() {
		 Sort sort = new Sort(Sort.Direction.ASC,"createBy");
		return reviewTypeRepository.findAllByParentId(sort, 0);
	}

	@Override
	public List<ReviewType> findAll(Example<ReviewType> example, Sort sort) {
		return reviewTypeRepository.findAll(example,sort);
	}

	@Override
	public Boolean repeatByName(ReviewType type) {
		  Long id = type.getId() != null ? type.getId() : Long.MIN_VALUE;
	        return reviewTypeRepository.findByNameAndIdNot(type.getName(), id) != null;
	}
}