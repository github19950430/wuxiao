package com.pldk.modules.system.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.StatusUtil;
import com.pldk.component.excel.annotation.Excel;
import com.pldk.component.excel.enums.ExcelType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Pldk
 * @date 2018/8/14
 */
@Entity
@Table(name="sys_user")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update sys_user" + StatusUtil.sliceDelete)
@Where(clause = StatusUtil.notDelete)
@Excel("用户数据")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Excel(value = "用户ID", type = ExcelType.EXPORT)
    private Long id;
    @Excel("用户名")
    private String username;
    @Excel(value = "密码", type = ExcelType.IMPORT)
    private String password;
    private String salt;
    @Excel("真实姓名")
    private String realname;
    private String picture;
    @Excel(value = "性别", dict = "USER_SEX")
    private String sex;
    // 评审类型
    @Excel(value = "专家评审类型", dict = "RESULT_TYPE")
    private String reviewType;
    
    //身份证号码
    private String  idCardNo; 
    //职称
    private String  technical;
    //住址电话
    private String addressPhone;
    //工作简历
    private String jobResume;
    
    @Excel(value = "状态", dict = "DATA_STATUS", type = ExcelType.EXPORT)
    private Byte status = StatusEnum.OK.getCode();
    @Excel(value ="所属工会",joinField="title", type = ExcelType.EXPORT)
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="dept_id")
    @JsonIgnore
    private Dept dept;
    @JsonIgnore
    @Transient
    private String deptStr; 
    @Excel("手机号码")
    private String phone;
    @Excel(value ="电子邮箱", type = ExcelType.EXPORT)
    private String email;
    @CreatedDate
    @Excel(value ="创建时间" , type = ExcelType.EXPORT)
    private Date createDate;
    @LastModifiedDate
    @Excel(value ="更新时间" , type = ExcelType.EXPORT)
    private Date updateDate;
    @Excel("备注")
    private String remark;
  
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>(0);
    //额外字段
    private String extra1;
    //是否 是专家（1：是 0：不是）
    private Integer isExpert;
    //专家账户有效开始时间
    private String expBeginDate;
    //专家账户有效结束时间
    private String expEndDate;
    //单位类型: 1 工会 2 团省委 3 省科协
    private Integer unitType;
}
