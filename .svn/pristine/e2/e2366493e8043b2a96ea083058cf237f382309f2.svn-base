/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.ssm.pay.common.orm.SsmBaseEntity;
import com.ssm.pay.common.utils.SsmStringUtils;

/** 系统数据字典
* @author Jiang.Li
* @version 2016年3月25日 下午3:59:00
*/
@Entity
@Table(name = "T_SYS_DICTIONARY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// jackson标记不生成json对象的属性
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler", "parentDictionary",
		"dictionaryType", "subDictionarys" })
@SuppressWarnings("serial")
public class SsmDictionary extends SsmBaseEntity{
	/**
	 * 参数名称
	 */
	private String name;
	/**
	 * 参数编码
	 */
	private String code;
    /**
     * 参数值
     */
    private String value;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 排序
	 */
	private Integer orderNo;
	/**
	 * 上级编码
	 */
	private SsmDictionary parentDictionary;
	/**
	 * 子Dictionary集合 @Transient
	 */
	private List<SsmDictionary> subDictionarys = Lists.newArrayList();

	/**
	 * 上级编码 @Transient
	 */
	private String parentDictionaryCode;
	/**
	 * 上级名称 @Transient
	 */
	private String parentDictionaryName;

	/**
	 * 系统字典类型
	 */
	private SsmDictionaryType dictionaryType;

	/**
	 * 系统字典类型 编码code @Transient
	 */
	private String dictionaryTypeCode;

	/**
	 * 系统字典类型 名称name @Transient
	 */
	private String dictionaryTypeName;

	public SsmDictionary() {
        super();
	}

    public SsmDictionary(Long id) {
        this();
        this.id = id;
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

    @Column(name = "VALUE",length = 100)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    @Column(name = "REMAK",length = 100)
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

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "PARENT_CODE", referencedColumnName = "CODE")
	public SsmDictionary getParentDictionary() {
		return parentDictionary;
	}

	public void setParentDictionary(SsmDictionary parentDictionary) {
		this.parentDictionary = parentDictionary;
	}

	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "parentDictionary")
	// @OrderBy("orderNo asc")
	// @LazyCollection(LazyCollectionOption.FALSE)
	@Transient
	public List<SsmDictionary> getSubDictionarys() {
		return subDictionarys;
	}

	public void setSubDictionarys(List<SsmDictionary> subDictionarys) {
		this.subDictionarys = subDictionarys;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "DICTIONARYTYPE_CODE", referencedColumnName = "CODE")
	public SsmDictionaryType getDictionaryType() {
		return dictionaryType;
	}

	public void setDictionaryType(SsmDictionaryType dictionaryType) {
		this.dictionaryType = dictionaryType;
	}

	@Transient
	public String getParentDictionaryCode() {
		if (SsmStringUtils.isBlank(parentDictionaryCode)
				&& parentDictionary != null) {
			parentDictionaryCode = parentDictionary.getCode();
		}
		return parentDictionaryCode;
	}

	public void setParentDictionaryCode(String parentDictionaryCode) {
		this.parentDictionaryCode = parentDictionaryCode;
	}

	@Transient
	public String getParentDictionaryName() {
		if (parentDictionary != null) {
			parentDictionaryName = parentDictionary.getName();
		}
		return parentDictionaryName;
	}

	public void setParentDictionaryName(String parentDictionaryName) {
		this.parentDictionaryName = parentDictionaryName;
	}

	@Transient
	public String getDictionaryTypeCode() {
		if (SsmStringUtils.isBlank(dictionaryTypeCode) && dictionaryType != null) {
			dictionaryTypeCode = dictionaryType.getCode();
		}
		return dictionaryTypeCode;
	}

	public void setDictionaryTypeCode(String dictionaryTypeCode) {
		this.dictionaryTypeCode = dictionaryTypeCode;
	}

	@Transient
	public String getDictionaryTypeName() {
		if (dictionaryType != null) {
			dictionaryTypeName = dictionaryType.getName();
		}
		return dictionaryTypeName;
	}

	public void setDictionaryTypeName(String dictionaryTypeName) {
		this.dictionaryTypeName = dictionaryTypeName;
	}
}
