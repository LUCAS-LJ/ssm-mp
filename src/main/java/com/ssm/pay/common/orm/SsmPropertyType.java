/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm;

import java.util.Date;

/** 属性数据类型.
 * S代表String,I代表Integer,L代表Long, N代表Double, D代表Date,B代表Boolean
* @author Jiang.Li
* @version 2016年1月29日 上午11:01:27
*/
public enum SsmPropertyType {
	/**
	 * String
	 */
	S(String.class),
	/**
	 * Integer
	 */
	I(Integer.class),
	/**
	 * Long
	 */
	L(Long.class),
	/**
	 * Double
	 */
	N(Double.class), 
	/**
	 * Date
	 */
	D(Date.class), 
	/**
	 * Boolean
	 */
	B(Boolean.class);

	//类型Class
	private Class<?> clazz;

	private SsmPropertyType(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 获取类型Class
	 * 
	 * @return Class
	 */
	public Class<?> getValue() {
		return clazz;
	}
}
