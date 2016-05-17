/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.ssm.pay.common.orm.SsmBaseEntity;
import com.ssm.pay.common.utils.SsmConvertUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;

/** 岗位
* @author Jiang.Li
* @version 2016年1月29日 下午1:32:18
*/
@Entity
@Table(name = "T_SYS_POST")
// jackson标记不生成json对象的属性
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler",
        "organ","users"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SsmPost extends SsmBaseEntity{
	/**
     * 岗位名称
     */
    private String name;
    /**
     * 岗位编码
     */
    private String code;
    /**
     * 备注
     */
    private String remark;
    /**
     * 所属部门
     */
    private SsmOrgan organ;
    /**
     * 所属部门ID
     * @Transient
     */
    private Long organId;

    /**
     * 岗位用户
     */
    private List<SsmUser> users = Lists.newArrayList();

    public SsmPost() {
    }

    @Column(name = "NAME",length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CODE",length = 36)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "REMARK",length = 36)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "ORGAN_ID")
    public SsmOrgan getOrgan() {
        return organ;
    }

    public void setOrgan(SsmOrgan organ) {
        this.organ = organ;
    }


    /**
     * 机构ID VIEW
     * @return
     */
    @Transient
    public Long getOrganId() {
        if(this.getOrgan() != null){
            return this.organ.getId();
        }
        return null;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }

    /**
     * 机构名称 VIEW
     * @return
     */
    @Transient
    public String getOrganName() {
        if(this.getOrgan() != null){
            return this.organ.getName();
        }
        return null;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "T_SYS_USER_POST", joinColumns = {@JoinColumn(name = "POST_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
    @Where(clause = "status = 0")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<SsmUser> getUsers() {
        return users;
    }

    public void setUsers(List<SsmUser> users) {
        this.users = users;
    }


    /**
     * 岗位用户名称 VIEW 多个之间以"，"分割
     * @return
     */
    @Transient
    public String getUserNames() {
        return SsmConvertUtils.convertElementPropertyToString(users, "name", ",");
    }


    @Transient
    public List<Long> getUserIds() {
        if(SsmCollections3.isNotEmpty(users)){
            return SsmConvertUtils.convertElementPropertyToList(users, "id");
        }
        return Lists.newArrayList();
    }
}
