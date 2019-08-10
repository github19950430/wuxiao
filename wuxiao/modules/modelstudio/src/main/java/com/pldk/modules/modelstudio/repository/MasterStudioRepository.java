package com.pldk.modules.modelstudio.repository;

import com.pldk.modules.modelstudio.domain.MasterStudio;
import com.pldk.modules.system.repository.BaseRepository;

import java.util.List;

/**
 * @author suhp
 * @date 2019/07/16
 */
public interface MasterStudioRepository extends BaseRepository<MasterStudio, Long> {

    List<MasterStudio> findAll();
}