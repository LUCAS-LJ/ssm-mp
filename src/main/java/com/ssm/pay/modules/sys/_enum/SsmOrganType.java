/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys._enum;

/**机构类型 
* @author Jiang.Li
* @version 2016年1月29日 下午1:28:38
*/
public enum SsmOrganType {
	 /** 机构(0) */
    organ(0, "机构(法人单位)"),
    /** 部门(1) */
    department(1, "部门"),
    /** 小组(2) */
    group(2, "小组");

	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;

	SsmOrganType(Integer value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * 获取值
	 * @return value
	 */
	public Integer getValue() {
		return value;
	}

	/**
     * 获取描述信息
     * @return description
     */
	public String getDescription() {
		return description;
	}

	public static SsmOrganType getOrganType(Integer value) {
		if (null == value)
			return null;
		for (SsmOrganType _enum : SsmOrganType.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static SsmOrganType getOrganType(String description) {
		if (null == description)
			return null;
		for (SsmOrganType _enum : SsmOrganType.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}
}
