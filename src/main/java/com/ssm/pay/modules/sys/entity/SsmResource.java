/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.ssm.pay.common.orm.SsmBaseEntity;
import com.ssm.pay.common.utils.SsmConvertUtils;
import com.ssm.pay.modules.sys._enum.SsmResourceType;

/** 受保护的资源Resource.
* @author Jiang.Li
* @version 2016年1月29日 下午1:10:53
*/
@SuppressWarnings("serial")
@Entity
@Table(name = "T_SYS_RESOURCE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties (value = { "hibernateLazyInitializer" , "handler","fieldHandler" ,  "parentResource",
        "roles","roleNames", "users", "subResources", "navigation" })
public class SsmResource extends SsmBaseEntity implements Serializable{
	 /**
     * 资源名称
     */
    private String name;
    /**
     * 资源编码
     */
    private String code;
    /**
     * 资源url地址
     */
    private String url;
    /**
     * 排序
     */
    private Integer orderNo;
    /**
     * 图标
     */
    private String iconCls;
    /**
     * 应用程序图标地址
     */
    private String icon;
    /**
     * 父级Resource
     */
    private SsmResource parentResource;
    /**
     * 父级ResourceId @Transient
     */
    private Long _parentId;
    /**
     * 标记url
     */
    private String markUrl;
    /**
     * 资源类型 资源(0) 功能(1)
     */
    private Integer type = SsmResourceType.menu.getValue();
    /**
     * 有序的关联对象集合
     */
    private List<SsmRole> roles = Lists.newArrayList();
    /**
     * 有序的关联对象集合
     */
    private List<SsmUser> users = Lists.newArrayList();
    /**
     * 子Resource集合
     */
    private List<SsmResource> subResources = Lists.newArrayList();

    public SsmResource() {
    }

    @Column(name = "MARK_URL",length = 2000)
    public String getMarkUrl() {
        return markUrl;
    }

    public void setMarkUrl(String markUrl) {
        this.markUrl = markUrl;
    }

    @NotBlank(message = "{resource_name.notblank}")
    @Length(max = 20, message = "{resource_name.length}")
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

    @Column(name = "URL",length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "ORDER_NO")
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name="ICON_CLS",length = 255)
    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    @Column(name="ICON",length = 255)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name="TYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "PARENT_ID")
    public SsmResource getParentResource() {
        return parentResource;
    }

    public void setParentResource(SsmResource parentResource) {
        this.parentResource = parentResource;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "T_SYS_ROLE_RESOURCE", joinColumns = {@JoinColumn(name = "RESOURCE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<SsmRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SsmRole> roles) {
        this.roles = roles;
    }

    /**
     * 角色拥有的资源字符串,多个之间以","分割
     *
     * @return
     */
    @Transient
    public String getRoleNames() {
        return SsmConvertUtils.convertElementPropertyToString(roles, "name",
                ", ");
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "T_SYS_USER_RESOURCE", joinColumns = {@JoinColumn(name = "RESOURCE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
    public List<SsmUser> getUsers() {
        return users;
    }

    public void setUsers(List<SsmUser> users) {
        this.users = users;
    }

    @OneToMany(mappedBy = "parentResource",cascade = {CascadeType.REMOVE})
    @OrderBy("orderNo asc")
    public List<SsmResource> getSubResources() {
        return subResources;
    }

    public void setSubResources(List<SsmResource> subResources) {
        this.subResources = subResources;
    }

    @Transient
    public List<SsmResource> getNavigation() {
        ArrayList<SsmResource> arrayList = new ArrayList<SsmResource>();
        SsmResource resource = this;
        arrayList.add(resource);
        while (null != resource.parentResource) {
            resource = resource.parentResource;
            arrayList.add(0, resource);
        }
        return arrayList;
    }

    @Transient
    public Long get_parentId() {
        if (parentResource != null) {
            _parentId = parentResource.getId();
        }
        return _parentId;
    }

    public void set_parentId(Long _parentId) {
        this._parentId = _parentId;
    }


    /**
     * 资源类型描述
     */
    @Transient
    public String getTypeView() {
        SsmResourceType r = SsmResourceType.getResourceType(type);
        String str = "";
        if(r != null){
            str = r.getDescription();
        }
        return str;
    }
}
