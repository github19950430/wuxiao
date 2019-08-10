package com.pldk.modules.newsManager.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.newsManager.domain.News;
import com.pldk.modules.newsManager.repository.NewsRepository;
import com.pldk.modules.newsManager.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/06/27
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public News getById(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<News> getPageList(Example<News> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return newsRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param news 实体对象
     */
    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return newsRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}