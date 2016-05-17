/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys._enum;

/** 
* @author Jiang.Li
* @version 2016年1月29日 下午1:13:02
*/
public enum SsmResourceType {
	/** 菜单(0) */
	menu(0, "菜单"),
	/** 按钮(1) */
	function(1, "功能");

	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;

	SsmResourceType(Integer value, String description) {
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

	public static SsmResourceType getResourceType(Integer value) {
		if (null == value)
			return null;
		for (SsmResourceType _enum : SsmResourceType.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static SsmResourceType getResourceType(String description) {
		if (null == description)
			return null;
		for (SsmResourceType _enum : SsmResourceType.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}
}
