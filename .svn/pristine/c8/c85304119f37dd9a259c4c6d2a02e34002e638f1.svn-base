/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.hibernate;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ssm.pay.common.utils.reflection.SsmReflectionUtils;

/**
* 含默认泛型DAO的EntityManager.
 * 
 * @param <T>
 *            领域对象类型
 * @param <PK>
 *            领域对象的主键类型 
* @author Jiang.Li
* @version 2016年1月29日 上午10:17:21
*/
public class SsmDefaultEntityManager<T,PK extends Serializable> extends SsmEntityManager<T,PK>{
	protected SsmHibernateDao<T, PK> entityDao;// 默认的泛型DAO成员变量.

	/**
	 * 通过注入的sessionFactory初始化默认的泛型DAO成员变量.
	 */
	@Autowired
	public void setSessionFactory(final SessionFactory sessionFactory) {
		Class<T> entityClass = SsmReflectionUtils
				.getClassGenricType(getClass());
		entityDao = new SsmHibernateDao<T, PK>(sessionFactory, entityClass);
	}

	@Override
	protected SsmHibernateDao<T, PK> getEntityDao() {
		return entityDao;
	}
}
