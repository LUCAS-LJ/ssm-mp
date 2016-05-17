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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.ssm.pay.common.orm.SsmBaseEntity;
import com.ssm.pay.common.utils.SsmConvertUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.modules.sys._enum.SsmOrganType;

/** 组织机构
* @author Jiang.Li
* @version 2016年1月29日 下午1:27:18
*/
@SuppressWarnings("serial")
@Entity
@Table(name = "T_SYS_ORGAN")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//jackson标记不生成json对象的属性
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler" ,
        "parentOrgan","subOrgans", "users","superManagerUser"})
public class SsmOrgan extends SsmBaseEntity implements Serializable{
	/**
     * 机构名称
     */
    private String name;
    /**
     * 机构编码
     */
    private String code;
    /**
     * 机构系统编码
     */
    private String sysCode;
    /**
     * 机构类型 {@link OrganType}
     */
    private Integer type = SsmOrganType.organ.getValue();
    /**
     * 地址
     */
    private String address;
    /**
     * 父级组织机构
     */
    private SsmOrgan parentOrgan;
    /**
     * 父级OrganId @Transient
     */
    private Long _parentId;
    /**
     * 子级组织机构
     */
    private List<SsmOrgan> subOrgans = Lists.newArrayList();
    /**
     * 分管领导
     */
    private SsmUser superManagerUser;
    /**
     * 机构负责人
     */
    private Long managerUserId;

    /**
     * 机构负责人登录名 @Transient
     */
    private String managerUserLoginName;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 传真号
     */
    private String fax;
    /**
     * 排序
     */
    private Integer orderNo;
    /**
     * 机构用户ID @Transient
     */
    private List<SsmUser> users = Lists.newArrayList();

    /**
     * 机构用户
     */
    private List<Long> userIds = Lists.newArrayList();
    /**
     * 部门岗位
     */
    private List<SsmPost> posts = Lists.newArrayList();


    public SsmOrgan() {
    }

    @Column(name = "NAME",nullable = false, length = 255, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "SYS_CODE",length = 36)
    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    @Column(name = "CODE",length = 36)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "TYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 资源类型 显示
     */
    @Transient
    public String getTypeView() {
    	SsmOrganType r = SsmOrganType.getOrganType(type);
        String str = "";
        if(r != null){
            str = r.getDescription();
        }
        return str;
    }

    @Column(name = "ADDRESS",length = 255)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "PHONE",length = 64)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "FAX",length = 64)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name = "ORDER_NO")
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "MANAGER_USER_ID")
    public Long getManagerUserId() {
        return managerUserId;
    }

    public void setManagerUserId(Long managerUserId) {
        this.managerUserId = managerUserId;
    }

    @Transient
    public String getManagerUserLoginName() {
        if(!SsmCollections3.isEmpty(users)) {
            for(SsmUser user:users){
                 if(managerUserId != null && user.getId().equals(managerUserId)){
                     managerUserLoginName = user.getLoginName();
                 }
            }
        }

        return managerUserLoginName;
    }

    public void setManagerUserLoginName(String managerUserLoginName) {
        this.managerUserLoginName = managerUserLoginName;
    }

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "SUPER_MANAGER_USER_ID")
    public SsmUser getSuperManagerUser() {
        return superManagerUser;
    }

    public void setSuperManagerUser(SsmUser superManagerUser) {
        this.superManagerUser = superManagerUser;
    }

    /**
     * 分管领导名称
     * @return
     */
    @Transient
    public String getSuperManagerUserName() {
        if (superManagerUser != null) {
            return superManagerUser.getName();
        }
        return null;
    }

    /**
     * 分管领导名称
     * @return
     */
    @Transient
    public String getSuperManagerUserLoginName() {
        if (superManagerUser != null) {
            return superManagerUser.getLoginName();
        }
        return null;
    }

    /**
     * 分管领导ID
     * @return
     */
    @Transient
    public Long getSuperManagerUserId() {
        if (superManagerUser != null) {
            return superManagerUser.getId();
        }
        return null;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "T_SYS_USER_ORGAN", joinColumns = {@JoinColumn(name = "ORGAN_ID")}, inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
    @Fetch(FetchMode.SUBSELECT)
    @Where(clause = "status = 0")
    @OrderBy("id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<SsmUser> getUsers() {
        return users;
    }

    public void setUsers(List<SsmUser> users) {
        this.users = users;
    }


    /**
     * 组织机构拥有的用户id字符串，多个用户id以","分割
     *
     * @return
     */
    @Transient
    @SuppressWarnings("unchecked")
    public List<Long> getUserIds() {
        if(!SsmCollections3.isEmpty(users)){
            userIds =  SsmConvertUtils.convertElementPropertyToList(users, "id");
        }
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    /**
     * 组织机构拥有的用户登录名字符串，多个用户登录名以","分割
     *
     * @return
     */
    @Transient
    public String getUserLoginNames() {
        return SsmConvertUtils.convertElementPropertyToString(users, "loginName", ", ");
    }

    /**
     * 组织机构拥有的用户姓名字符串，多个用户登录名以","分割
     *
     * @return
     */
    @Transient
    public String getUserNames() {
        return SsmConvertUtils.convertElementPropertyToString(users, "name", ", ");
    }


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PARENT_ID")
    public SsmOrgan getParentOrgan() {
        return parentOrgan;
    }

    public void setParentOrgan(SsmOrgan parentOrgan) {
        this.parentOrgan = parentOrgan;
    }

    @Transient
    public Long get_parentId() {
        if(parentOrgan != null){
            _parentId = parentOrgan.getId();
        }
        return _parentId;
    }

    public void set_parentId(Long _parentId) {
        this._parentId = _parentId;
    }

    @OneToMany(mappedBy = "parentOrgan", cascade = {CascadeType.REMOVE})
    public List<SsmOrgan> getSubOrgans() {
        return subOrgans;
    }

    public void setSubOrgans(List<SsmOrgan> subOrgans) {
        this.subOrgans = subOrgans;
    }

    @OneToMany(mappedBy = "organ", cascade = {CascadeType.REMOVE})
    public List<SsmPost> getPosts() {
        return posts;
    }

    public void setPosts(List<SsmPost> posts) {
        this.posts = posts;
    }

    /**
     * 机构下默认用户
     * @return
     */
    @Transient
    public List<SsmUser> getDefautUsers() {
        List<SsmUser> list = Lists.newArrayList();
        List<SsmUser> users = getUsers();
        for (SsmUser user : users) {
            if (user.getDefaultOrganId() != null && user.getDefaultOrganId().equals(id)) {
                list.add(user);
            }
        }
        return list;
    }
}
