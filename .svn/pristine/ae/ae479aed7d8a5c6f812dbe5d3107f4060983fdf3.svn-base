/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/** 数据库访问层异常,继承自BaseException.
* @author Jiang.Li
* @version 2016年1月29日 上午10:40:19
*/
@SuppressWarnings("serial")
public class SsmDaoException extends SsmBaseException{
	private Throwable rootCause;

	public SsmDaoException() {
		super();
	}

	public SsmDaoException(String message) {
		super(message);
	}

	public SsmDaoException(Throwable cause) {
		super(cause);
		this.rootCause = cause;
	}

	public SsmDaoException(String message, Throwable cause) {
		super(message, cause);
		this.rootCause = cause;
	}

	public String getTraceInfo() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}

	public Throwable getRootCause() {
		return rootCause;
	}
}
