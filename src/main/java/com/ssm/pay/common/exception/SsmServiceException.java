/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.exception;

/** Service层异常, 继承自BaseException.
* @author Jiang.Li
* @version 2016年1月29日 上午11:34:18
*/
public class SsmServiceException extends SsmBaseException{
	private Integer code;
	private Object obj;

	public SsmServiceException() {
		super();
	}

	/**
	 * 
	 * @param code
	 *            状态码
	 * @param message
	 *            消息
	 * @param obj
	 *            其它数据
	 */
	public SsmServiceException(Integer code, String message, Object obj) {
		super(message);
		this.code = code;
		this.obj = obj;
	}

	public SsmServiceException(String message) {
		super(message);
	}

	public SsmServiceException(Throwable cause) {
		super(cause);
	}

	public SsmServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param code
	 *            状态码 @see com.ssm.pay.common.model.Result
	 * @param message
	 *            消息描述
	 * @param obj
	 *            其它信息
	 * @param cause
	 *            异常类
	 */
	public SsmServiceException(Integer code, String message, Object obj,
			Throwable cause) {
		super(message, cause);
		this.code = code;
		this.obj = obj;
	}

	public Integer getCode() {
		return code;
	}

	public Object getObj() {
		return obj;
	}
}
