package com.pldk.modules.review.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.review.domain.ReviewSet;
import com.pldk.modules.review.repository.ReviewSetRepository;
import com.pldk.modules.review.service.ReviewSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Service
public class ReviewSetServiceImpl implements ReviewSetService {

    @Autowired
    private ReviewSetRepository reviewSetRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public ReviewSet getById(Long id) {
        return reviewSetRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<ReviewSet> getPageList(Example<ReviewSet> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return reviewSetRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param reviewSet 实体对象
     */
    @Override
    public ReviewSet save(ReviewSet reviewSet) {
        return reviewSetRepository.save(reviewSet);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return reviewSetRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

	@Override
	public Boolean repeatByContent(ReviewSet set) {
		Long id = set.getId()!= null ? set.getId() : Long.MIN_VALUE;
		Long typeId = set.getPid()!= null ? set.getPid() : Long.MIN_VALUE;
	    return reviewSetRepository.findByContentAndIdNot(set.getContent(),id) != null;
	}

	@Override
	public List<ReviewSet> findAllByPid(Long pid) {
		return reviewSetRepository.findAllByPid(pid);
	}
}