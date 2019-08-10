package com.pldk.modules.modelstudio.repository;

import com.pldk.modules.modelstudio.domain.LeaderAccessory;
import com.pldk.modules.system.repository.BaseRepository;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/17
 */
public interface LeaderAccessoryRepository extends BaseRepository<LeaderAccessory, Long> {

    int deleteAllById(Long id);

    List<LeaderAccessory> findAllByLeaderId(Long leaderId);

}