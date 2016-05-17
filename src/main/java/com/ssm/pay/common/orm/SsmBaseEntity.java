/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssm.pay.common.excel.annotation.SsmExcel;
import com.ssm.pay.common.orm.entity.SsmAutoEntity;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.utils.SsmDateUtils;

/** 
* @author Jiang.Li
* @version 2016年1月28日 下午11:07:27
*/
@MappedSuperclass
public class SsmBaseEntity extends SsmAutoEntity implements Serializable,Cloneable{
	 /**
	  * 
	  */
	private static final long serialVersionUID = -4438922372629019136L;

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String TIME_FORMAT = "HH:mm:ss";

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

	public static final String TIMEZONE = "GMT+08:00";

	public static String date2String(Date date, String dateFormat) {
		return SsmDateUtils.format(date, dateFormat);
	}

	public static <T extends Date> T string2Date(String dateString,
			String dateFormat, Class<T> targetResultType) {
		return (T) SsmDateUtils.parse(dateString, dateFormat, targetResultType);
	}

	/**
	 * 记录状态标志位 正常(0) 已删除(1) 待审核(2) 锁定(3)
	 */
	protected Integer status = SsmStatusState.normal.getValue();
	/**
	 * 操作版本(乐观锁,用于并发控制)
	 */
	protected Integer version;

	/**
	 * 记录创建者用户登录名
	 */
	@SsmExcel(exportName="记录创建者", exportFieldWidth = 30)
	protected String createUser;
	/**
	 * 记录创建时间
	 */
	@SsmExcel(exportName="记录创建时间", exportFieldWidth = 30)
	protected Date createTime;

	/**
	 * 记录更新用户 用户登录名
	 */
	protected String updateUser;
	/**
	 * 记录更新时间
	 */
	protected Date updateTime;

	public SsmBaseEntity() {
		super();
	}


	/**
	 * 状态标志位
	 */
    @Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * 状态描述
	 */
	@Transient
	public String getStatusView() {
		SsmStatusState s = SsmStatusState.getSsmStatusState(status);
		String str = "";
		if(s != null){
			str =  s.getDescription();
		}
		return str;
	}


	/**
	 * 设置 状态标志位
	 * 
	 * @param status
	 *            状态标志位
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 版本号(乐观锁)
	 */
	@Version
    @Column(name = "VERSION")
	public Integer getVersion() {
		return version;
	}

	/**
	 * 设置 版本号(乐观锁)
	 * 
	 * @param version
	 *            版本号(乐观锁)
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * 记录创建者 用户登录名
	 */
	@Column(name = "CREATE_USER", updatable = false, length = 36)
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * 设置 记记录创建者 用户登录名
	 * 
	 * @param createUser
	 *            用户登录名
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * 记录创建时间.
	 */
	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIMEZONE)
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置 记录创建时间
	 * 
	 * @param createTime
	 *            记录创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 记录更新用户 用户登录名
	 */
	@Column(name = "UPDATE_USER", length = 36)
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * 设置 记录更新用户 用户登录名
	 * 
	 * @param updateUser
	 *            用户登录名
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * 记录更新时间
	 */
	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIMEZONE)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置 记录更新时间
	 * 
	 * @param updateTime
	 *            记录更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}



	public SsmBaseEntity clone() {
		SsmBaseEntity o = null;
		try {
			o = (SsmBaseEntity) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
