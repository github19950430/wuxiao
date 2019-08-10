package com.pldk.modules.modelstudio.repository;

import com.pldk.modules.modelstudio.domain.StudioImg;
import com.pldk.modules.system.repository.BaseRepository;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public interface StudioImgRepository extends BaseRepository<StudioImg, Long> {

    List<StudioImg> findAllByStudioId(Long studioId);

    int deleteAllById(Long id);
}