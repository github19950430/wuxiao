package com.pldk.modules.modelstudio.service;

import com.pldk.common.enums.StatusEnum;
import com.pldk.modules.modelstudio.domain.StudioVideo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pldk
 * @date 2019/07/11
 */
public interface StudioVideoService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<StudioVideo> getPageList(Example<StudioVideo> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    StudioVideo getById(Long id);

    /**
     * 保存数据
     * @param studioVideo 实体对象
     */
    StudioVideo save(StudioVideo studioVideo);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    /**
     * 根据工作室ID查询所属工作室的视频
     * @param studioId
     * @return
     */
    List<StudioVideo> getAllByStudioId(Long studioId);

    /**
     *
     * 根据主键ID 删除视频资料
     * @param id
     * @return
     */
    int deleteById(Long id);


}