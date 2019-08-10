package com.pldk.modules.modelstudio.service.impl;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.HttpServletUtil;
import com.pldk.modules.modelstudio.domain.MasterStudio;
import com.pldk.modules.modelstudio.domain.Modelstudio;
import com.pldk.modules.modelstudio.repository.MasterStudioRepository;
import com.pldk.modules.modelstudio.repository.ModelstudioRepository;
import com.pldk.modules.modelstudio.service.ModelstudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pldk
 * @date 2019/07/10
 */
@Service
public class ModelstudioServiceImpl implements ModelstudioService {

    @Autowired
    private ModelstudioRepository modelstudioRepository;
    @Autowired
    private MasterStudioRepository masterStudioRepository;
    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Modelstudio getById(Long id) {
        return modelstudioRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param modelstudio 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Modelstudio> getPageList(Modelstudio modelstudio,Integer level) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        // 使用Specification复杂查询
        return modelstudioRepository.findAll(new Specification<Modelstudio>(){
            @Override
            public Predicate toPredicate(Root<Modelstudio> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> preList = new ArrayList<>();
                if (level == 1){
                    if (modelstudio.getAudit_status() == null){
                        preList.add(cb.notEqual(root.get("audit_status").as(Byte.class), 0));
                    }
                }
                if(modelstudio.getAudit_status() != null){
                    preList.add(cb.equal(root.get("audit_status").as(Byte.class), modelstudio.getAudit_status()));
                }
                if(modelstudio.getState() != null){
                    preList.add(cb.equal(root.get("state").as(Byte.class), modelstudio.getState()));
                }
                if(modelstudio.getFlat_type() != null){
                    preList.add(cb.equal(root.get("flat_type").as(String.class), modelstudio.getFlat_type()));
                }
                if(modelstudio.getStudioName() != null){
                    preList.add(cb.equal(root.get("studioName").as(String.class), modelstudio.getStudioName()));
                }
                Predicate[] pres = new Predicate[preList.size()];
                return query.where(preList.toArray(pres)).getRestriction();
            }

        }, page);
    }

    /**
     * 保存数据
     * @param modelstudio 实体对象
     */
    @Override
    public Modelstudio save(Modelstudio modelstudio) {
        return modelstudioRepository.save(modelstudio);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return modelstudioRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public Modelstudio findOne(String studioName) {
        return modelstudioRepository.findAllByStudioName(studioName);
    }

    @Override
    public Map<String,Object> findAll() {
        Map<String,Object> map = new HashMap<>();
        List<Modelstudio> model = modelstudioRepository.findAll();
        List<MasterStudio> master = masterStudioRepository.findAll();
        model.forEach(mo->{
            map.put(mo.getId()+",1",mo.getStudioName());
        });
        master.forEach(ma->{
            map.put(ma.getId()+",2",ma.getName());
        });
        return map;
    }

    @Override
    public Boolean updateAuditStatus(Long id) {
        return modelstudioRepository.update(id) > 0?true:false;
    }

    @Override
    public Page<Map<String, Object>> findAllSZ(Byte state) {
//        // 创建分页对象
        PageRequest page = PageSort.pageRequest("id",Sort.Direction.ASC);
        return modelstudioRepository.findByStateMatch(state,page);
    }
}