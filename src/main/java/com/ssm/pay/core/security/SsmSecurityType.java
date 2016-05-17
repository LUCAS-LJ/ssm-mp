/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.security;

/** 
* @author Jiang.Li
* @version 2016年1月28日 下午9:35:22
*/
public enum SsmSecurityType {
	/** 用户登录(0) */
	login(0, "用户登录"),
	/** 用户注销(1) */
	logout(1, "用户注销"),
	/** 用户非正常注销(2) */
	logout_abnormal(2, "用户非正常注销"),
	/** 强制下线(3) */
	offline(3, "强制下线");
	
	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;

	SsmSecurityType(Integer value, String description) {
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

	public static SsmSecurityType getSecurityType(Integer value) {
		if (null == value)
			return null;
		for (SsmSecurityType _enum : SsmSecurityType.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static SsmSecurityType getSecurityType(String description) {
		if (null == description)
			return null;
		for (SsmSecurityType _enum : SsmSecurityType.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}

}
