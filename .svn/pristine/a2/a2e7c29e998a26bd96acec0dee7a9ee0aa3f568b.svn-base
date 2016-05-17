/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys._enum;

/** 日志类型
* @author Jiang.Li
* @version 2016年1月29日 下午12:43:55
*/
public enum SsmLogType {
	/** 安全日志(0) */
    security(0, "安全"),
    /** 操作日志(1) */
    operate(1, "操作"),
    /** 访问日志(2) */
    access(2, "访问"),
    /** 异常(3) */
    exception(3, "异常");

	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;

	SsmLogType(Integer value, String description) {
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

	public static SsmLogType getLogType(Integer value) {
		if (null == value)
			return null;
		for (SsmLogType _enum : SsmLogType.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static SsmLogType getLogType(String description) {
		if (null == description)
			return null;
		for (SsmLogType _enum : SsmLogType.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}

}
