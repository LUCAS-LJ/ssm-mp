/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.ssm.pay.common.excel.annotation.SsmExcel;
import com.ssm.pay.common.orm.SsmBaseEntity;

/** 系统字典类型
* @author Jiang.Li
* @version 2016年3月25日 下午4:01:43
*/
@Entity
@Table(name = "T_SYS_DICTIONARYTYPE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler",
        "groupDictionaryType","subDictionaryTypes" })
@SuppressWarnings("serial")
public class SsmDictionaryType extends SsmBaseEntity{
	/**
	 * 类型名称
	 */
	@SsmExcel(exportName="类型名称", exportFieldWidth = 30)
	private String name;
	/**
	 * 类型编码
	 */
	@SsmExcel(exportName="类型编码", exportFieldWidth = 20)
	private String code;

    /**
     * 父级类型 即分组
     */
    private SsmDictionaryType groupDictionaryType;
    /**
     * @Transient 父级类型 即分组名称
     */
    private String groupDictionaryTypeName;
    /**
     * @Transient 父级类型 即分组编码
     */
    private String groupDictionaryTypeCode;
    /**
     * 子DictionaryType集合
     */
    private List<SsmDictionaryType> subDictionaryTypes = Lists.newArrayList();
    /**
     * 备注
     */
    private String remark;

	/**
	 * 排序
	 */
	@SsmExcel(exportName="排序", exportFieldWidth = 10)
	private Integer orderNo;

	public SsmDictionaryType() {
		super();
	}

    public SsmDictionaryType(Long id) {
        this();
        super.id = id;
    }

    /**
	 * 系统数据字典类型构造函数.
	 * 
	 * @param name
	 *            类型名称
	 * @param code
	 *            类型编码
	 * @param orderNo
	 *            排序
	 */
	public SsmDictionaryType(String name, String code, Integer orderNo) {
		super();
		this.name = name;
		this.code = code;
		this.orderNo = orderNo;
	}

	@Column(name = "NAME",length = 100, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CODE",length = 36, unique = true)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "GROUP_CODE", referencedColumnName = "CODE")
    public SsmDictionaryType getGroupDictionaryType() {
        return groupDictionaryType;
    }

    public void setGroupDictionaryType(SsmDictionaryType groupDictionaryType) {
        this.groupDictionaryType = groupDictionaryType;
    }

    @Transient
    public String getGroupDictionaryTypeName() {
        if(groupDictionaryType != null){
            groupDictionaryTypeName = groupDictionaryType.getName();
        }
        return groupDictionaryTypeName;
    }

    public void setGroupDictionaryTypeName(String groupDictionaryTypeName) {
        this.groupDictionaryTypeName = groupDictionaryTypeName;
    }

    @Transient
    @JsonProperty("_parentId")
    public String getGroupDictionaryTypeCode() {
        if(groupDictionaryType != null){
            groupDictionaryTypeCode = groupDictionaryType.getCode();
        }
        return groupDictionaryTypeCode;
    }

    public void setGroupDictionaryTypeCode(String groupDictionaryTypeCode) {
        this.groupDictionaryTypeCode = groupDictionaryTypeCode;
    }

    @OneToMany(mappedBy = "groupDictionaryType",fetch = FetchType.EAGER)
    public List<SsmDictionaryType> getSubDictionaryTypes() {
        return subDictionaryTypes;
    }

    public void setSubDictionaryTypes(List<SsmDictionaryType> subDictionaryTypes) {
        this.subDictionaryTypes = subDictionaryTypes;
    }
    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "ORDER_NO")
	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

}
