package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.Keymember;
import com.pldk.modules.modelstudio.repository.KeymemberRepository;
import com.pldk.modules.modelstudio.service.KeymemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/18
 */
@Service
public class KeymemberServiceImpl implements KeymemberService {

    @Autowired
    private KeymemberRepository keymemberRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Keymember getById(Long id) {
        return keymemberRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Keymember> getPageList(Example<Keymember> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return keymemberRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param keymember 实体对象
     */
    @Override
    public Keymember save(Keymember keymember) {
        return keymemberRepository.save(keymember);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return keymemberRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List<Keymember> findAllByforegoerId(Long foregoerId) {
        return keymemberRepository.findAllByForegoerid(foregoerId);
    }

    @Override
    public int deleteById(Long id) {
        return keymemberRepository.deleteAllById(id);
    }
}