/*@版权所有LIJIANG*/ 
package com.ssm.pay.utils;

/** 是/否公用枚举类
* @author Jiang.Li
* @version 2016年4月15日 上午11:00:47
*/
public enum SsmYesOrNo {
	/** 是(1) */
	YES(1, "是"),
	/** 否(0) */
	NO(0, "否");

	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;

	SsmYesOrNo(Integer value, String description) {
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

	public static SsmYesOrNo getYesOrNo(Integer value) {
		if (null == value)
			return null;
		for (SsmYesOrNo _enum : SsmYesOrNo.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static SsmYesOrNo getYesOrNo(String description) {
		if (null == description)
			return null;
		for (SsmYesOrNo _enum : SsmYesOrNo.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}
}
