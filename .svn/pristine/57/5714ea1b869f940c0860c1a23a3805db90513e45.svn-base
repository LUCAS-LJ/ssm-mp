/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.entity;

/** 状态标识 枚举类型.
* @author Jiang.Li
* @version 2016年1月28日 下午11:29:12
*/
public enum SsmStatusState {
	/** 正常(0) */ 
	normal(0, "正常"),
	/** 已删除(1) */
	delete(1, "已删除"),
	/** 待审核(2) */
	audit(2, "待审核"), 
	/** 锁定(3) */
	lock(3, "已锁定");

	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;

	SsmStatusState(Integer value, String description) {
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

	public static SsmStatusState getSsmStatusState(Integer value) {
		if (null == value)
			return null;
		for (SsmStatusState _enum : SsmStatusState.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static SsmStatusState getSsmStatusState(String description) {
		if (null == description)
			return null;
		for (SsmStatusState _enum : SsmStatusState.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}

}
