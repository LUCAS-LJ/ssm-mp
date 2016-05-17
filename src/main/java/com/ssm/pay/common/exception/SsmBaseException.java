/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.exception;


/** 运行时异常基类
* @author Jiang.Li
* @version 2016年1月28日 下午11:36:58
*/
@SuppressWarnings("serial")
public class SsmBaseException extends RuntimeException{
	public SsmBaseException() {
		super();
	}

	public SsmBaseException(String message) {
		super(message);
	}

	public SsmBaseException(Throwable cause) {
		super(cause);
	}

	public SsmBaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
