/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.ssm.pay.common.orm.SsmBaseEntity;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.utils.SsmConvertUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;

/**角色管理Role. 
* @author Jiang.Li
* @version 2016年1月29日 下午1:16:29
*/
@Entity
@Table(name = "T_SYS_ROLE")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//jackson标记不生成json对象的属性 
@JsonIgnoreProperties (value = { "hibernateLazyInitializer" , "handler","fieldHandler" ,
        "resources","users"})
public class SsmRole extends SsmBaseEntity implements Serializable{
	/**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 描述
     */
    private String remark;
    /**
     * 关联的资源
     */
    private List<SsmResource> resources = Lists.newArrayList();
    /**
     * 关联资源ID集合    @Transient
     */
    private List<Long> resourceIds = Lists.newArrayList();

    /**
     * 关联的用户
     */
    private List<SsmUser> users = Lists.newArrayList();
    /**
     * 关联用户ID集合    @Transient
     */
    private List<Long> userIds = Lists.newArrayList();



    public SsmRole() {

    }

	@Column(name = "NAME",length = 100,nullable = false,unique = true)
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

    @Column(name = "REMARK",length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "T_SYS_ROLE_RESOURCE", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID") })
//    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<SsmResource> getResources() {
        return resources;
    }

    public void setResources(List<SsmResource> resources) {
        this.resources = resources;
    }


    /**
     * 角色拥有的资源id字符串集合
     *
     * @return
     */
    @Transient
    @SuppressWarnings("unchecked")
    public List<Long> getResourceIds() {
        if (!SsmCollections3.isEmpty(resources)) {
            resourceIds = SsmConvertUtils.convertElementPropertyToList(resources, "id");
        }
        return resourceIds;
    }

    public void setResourceIds(List<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }

    /**
     * 角色拥有的资源字符串,多个之间以","分割
     *
     * @return
     */
    @Transient
    public String getResourceNames() {
    	List<SsmResource> ms = Lists.newArrayList();
    	for(SsmResource m: resources){
    		if(m.getStatus().equals(SsmStatusState.normal.getValue())){
    			ms.add(m);
    		}
    	}
        return SsmConvertUtils.convertElementPropertyToString(ms, "name",
                ", ");
    }

    /**
     * 用户拥有的角色字符串,多个之间以","分割
     *
     * @return
     */
    @Transient
    public String getUserNames() {
        return SsmConvertUtils.convertElementPropertyToString(users,
                "loginName", ", ");
    }


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    // 中间表定义,表名采用默认命名规则
    @JoinTable(name = "T_SYS_USER_ROLE", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "USER_ID") })
    @Where(clause = "status = 0")
    @OrderBy("id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<SsmUser> getUsers() {
        return users;
    }

    public void setUsers(List<SsmUser> users) {
        this.users = users;
    }

    @Transient
    public List<Long> getUserIds() {
        if (!SsmCollections3.isEmpty(users)) {
            userIds = SsmConvertUtils.convertElementPropertyToList(users, "id");
        }
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
