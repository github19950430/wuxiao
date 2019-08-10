package com.pldk.modules.modelstudio.repository;

import com.pldk.modules.modelstudio.domain.StudioHonor;
import com.pldk.modules.system.repository.BaseRepository;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/22
 */
public interface StudioHonorRepository extends BaseRepository<StudioHonor, Long> {

    int deleteAllById(Long id);

    List<StudioHonor> findAllByStudioId(Long studioId);
}