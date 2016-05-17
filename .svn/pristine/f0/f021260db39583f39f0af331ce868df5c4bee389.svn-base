/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.model;

import java.io.Serializable;

import com.ssm.pay.common.utils.mapper.SsmJsonMapper;

/** Ajax请求Json响应结果模型.
* @author Jiang.Li
* @version 2016年2月27日 下午9:39:07
*/
@SuppressWarnings("serial")
public class SsmResult implements Serializable{
	/**
	 * 成功
	 */
	public static final int SUCCESS = 1;
	/**
	 * 警告
	 */
	public static final int WARN = 2;
	/**
	 * 失败
	 */
	public static final int ERROR = 0;
	
	/**
	 * 成功消息
	 */
	public static final String SUCCESS_MSG = "操作成功！";
	/**
	 * 失败消息
	 */
	public static final String ERROR_MSG = "操作失败:发生未知异常！";

	/**
	 * 结果状态码(可自定义结果状态码) 1:操作成功 0:操作失败
	 */
	private int code;
	/**
	 * 响应结果描述
	 */
	private String msg;
	/**
	 * 其它数据信息（比如跳转地址）
	 */
	private Object obj;

	public SsmResult() {
		super();
	}

	/**
	 * 
	 * @param code
	 *            结果状态码(可自定义结果状态码或者使用内部静态变量 1:操作成功 0:操作失败 2:警告)
	 * @param msg
	 *            响应结果描述
	 * @param obj
	 *            其它数据信息（比如跳转地址）
	 */
	public SsmResult(int code, String msg, Object obj) {
		super();
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}

	/**
	 * 默认操作成功结果.
	 */
	public static SsmResult successResult() {
		return new SsmResult(SUCCESS, SUCCESS_MSG, null);
	}

	/**
	 * 默认操作失败结果.
	 */
	public static SsmResult errorResult() {
		return new SsmResult(ERROR, ERROR_MSG, null);
	}

	/**
	 * 结果状态码(可自定义结果状态码) 1:操作成功 0:操作失败
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 设置结果状态码(约定 1:操作成功 0:操作失败 2:警告)
	 * 
	 * @param code
	 *            结果状态码
	 */
	public SsmResult setCode(int code) {
		this.code = code;
        return this;
	}

	/**
	 * 响应结果描述
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 设置响应结果描述
	 * 
	 * @param msg
	 *            响应结果描述
	 */
	public SsmResult setMsg(String msg) {
		this.msg = msg;
        return this;
	}

	/**
	 * 其它数据信息（比如跳转地址）
	 */
	public Object getObj() {
		return obj;
	}

	/**
	 * 设置其它数据信息（比如跳转地址）
	 * 
	 * @param obj
	 *            其它数据信息（比如跳转地址）
	 */
	public SsmResult setObj(Object obj) {
		this.obj = obj;
        return this;
	}

	@SuppressWarnings("static-access")
	@Override
	public String toString() {
		return new SsmJsonMapper().nonDefaultMapper().toJson(this);
	}
}
