package com.pldk.modules.bannerManager.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.bannerManager.domain.Banner;
import com.pldk.modules.bannerManager.repository.BannerRepository;
import com.pldk.modules.bannerManager.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/06/28
 */
@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Banner getById(Long id) {
        return bannerRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Banner> getPageList(Example<Banner> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return bannerRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param banner 实体对象
     */
    @Override
    public Banner save(Banner banner) {
        return bannerRepository.save(banner);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return bannerRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}