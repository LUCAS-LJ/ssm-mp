/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys._enum;

/** 性别标识 枚举类型.
* @author Jiang.Li
* @version 2016年1月29日 下午1:23:40
*/
public enum SsmSexType {
	/** 女(0) */
	girl(0, "女"),
	/** 男(1) */
	boy(1, "男"),
	/** 保密(2) */
	secrecy(2, "保密");

	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;

	SsmSexType(Integer value, String description) {
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

	public static SsmSexType getSexType(Integer value) {
		if (null == value)
			return null;
		for (SsmSexType _enum : SsmSexType.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static SsmSexType getSexType(String description) {
		if (null == description)
			return null;
		for (SsmSexType _enum : SsmSexType.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}

}
