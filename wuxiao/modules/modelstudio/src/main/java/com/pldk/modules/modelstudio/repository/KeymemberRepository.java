package com.pldk.modules.modelstudio.repository;

import com.pldk.modules.modelstudio.domain.Keymember;
import com.pldk.modules.system.repository.BaseRepository;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/18
 */
public interface KeymemberRepository extends BaseRepository<Keymember, Long> {

    List<Keymember> findAllByForegoerid(Long foregoerid);

    int deleteAllById(Long id);
}