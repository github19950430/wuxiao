package com.pldk.modules.system.repository;

import com.pldk.modules.system.domain.Dict;

/**
 * @author Pldk
 * @date 2018/8/14
 */
public interface DictRepository extends BaseRepository<Dict, Long> {

    /**
     * 根据字典标识查询
     * @param name 字典标识
     * @param status 状态
     */
    public Dict findByNameAndStatus(String name, Byte status);

    /**
     * 根据标识查询字典数据,且排查指定ID的字典
     * @param name 字典标识
     * @param id 字典ID
     */
    public Dict findByNameAndIdNot(String name, Long id);
}
