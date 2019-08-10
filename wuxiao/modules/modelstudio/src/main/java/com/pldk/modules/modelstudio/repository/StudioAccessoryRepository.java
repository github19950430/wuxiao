package com.pldk.modules.modelstudio.repository;

import com.pldk.modules.modelstudio.domain.StudioAccessory;
import com.pldk.modules.system.repository.BaseRepository;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public interface StudioAccessoryRepository extends BaseRepository<StudioAccessory, Long> {

    List<StudioAccessory> findAllByStudioId(Long studioId);

    int deleteAllById(Long id);
}