package com.pldk.modules.modelstudio.repository;

import com.pldk.modules.modelstudio.domain.StudioVideo;
import com.pldk.modules.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public interface StudioVideoRepository extends BaseRepository<StudioVideo, Long> {


    List<StudioVideo> findAllByStudioId(Long studioId);


    int deleteAllById(Long id);

}