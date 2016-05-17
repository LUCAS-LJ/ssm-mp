/*@版权所有LIJIANG*/ 
package com.ssm.pay.core;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;

/** 
* Hibernate拦截器 实现修改人自动注入.	
* @author Jiang.Li
* @version 2016年1月28日 下午11:02:31
*/
public class SsmHibernateAspectInterceptor extends EmptyInterceptor{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5240391945830847299L;
	private static final Logger logger = Logger.getLogger(SsmHibernateAspectInterceptor.class);
	
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		logger.debug("onSave");
		SsmSessionInfo sessionInfo = null;
		try {
            sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
			if (sessionInfo == null) {
				logger.warn("session中未获取到用户.");
				return true;
			}
		} catch (Exception e) {
			return true;
		}
		try {
			// 添加数据
			for (int index = 0; index < propertyNames.length; index++) {
				if ("createUser".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"创建人名称"属性赋上值 */
					if (state[index] == null) {
						state[index] = sessionInfo.getLoginName();
					}
					continue;
				}
				if ("createTime".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"创建时间"属性赋上值 */
					if (state[index] == null) {
						state[index] = new Date();
					}
					continue;
				}

                if ("updateUser".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"修改人名称"属性赋上值 */
                    state[index] = sessionInfo.getLoginName();
                    continue;
                }
                if ("updateTime".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"修改时间"属性赋上值 */
                    state[index] = new Date();
                    continue;
                }
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		logger.debug("onFlushDirty");
        SsmSessionInfo sessionInfo = null;
        try {
            sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
			if (sessionInfo == null) {
				logger.warn("session中未获取到用户.");
				return true;
			}
		} catch (Exception e) {
			return true;
		}
		try {
			// 修改或添加数据
			for (int index = 0; index < propertyNames.length; index++) {
				if ("updateUser".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"修改人名称"属性赋上值 */
					currentState[index] = sessionInfo.getLoginName();
					continue;
				}
				if ("updateTime".equals(propertyNames[index])) {
					/* 使用拦截器将对象的"修改时间"属性赋上值 */
					currentState[index] = new Date();
					continue;
				}
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
