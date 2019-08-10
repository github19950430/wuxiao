package com.pldk.modules.modelstudio.repository;

import com.pldk.modules.modelstudio.domain.Leaderinformation;
import com.pldk.modules.system.repository.BaseRepository;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public interface LeaderinformationRepository extends BaseRepository<Leaderinformation, Long> {


    List<Leaderinformation> findAll();
}