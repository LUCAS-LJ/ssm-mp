/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.hibernate;

import java.util.HashMap;

/** 查询参数类
* @author Jiang.Li
* @version 2016年1月29日 上午10:42:19
*/
public class SsmParameter extends HashMap<String, Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6676459302965467285L;

	/**
	 * 构造类，例：new SsmParameter(id, parentIds)
	 * @param values 参数值
	 */
	public SsmParameter(Object... values) {
		if (values != null){
			for (int i=0; i<values.length; i++){
				put("p"+(i+1), values[i]);
			}
		}
	}
	
	/**
	 * 构造类，例：new SsmParameter(new Object[][]{{"id", id}, {"parentIds", parentIds}})
	 * @param parameters 参数二维数组
	 */
	public SsmParameter(Object[][] parameters) {
		if (parameters != null){
			for (Object[] os : parameters){
				if (os.length == 2){
					put((String)os[0], os[1]);
				}
			}
		}
	}
}
