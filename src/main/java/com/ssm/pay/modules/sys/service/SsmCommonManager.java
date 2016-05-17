/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.service;

import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.google.common.collect.Maps;
import com.ssm.pay.common.exception.SsmDaoException;
import com.ssm.pay.common.exception.SsmServiceException;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.orm.hibernate.SsmDefaultEntityManager;
import com.ssm.pay.common.orm.jdbc.SsmJdbcDao;

/** 
* Service层实现类. <br>
* 提供常用的共同方法.
* @author Jiang.Li
* @version 2016年3月25日 下午2:35:36
*/
@Service
public class SsmCommonManager extends SsmDefaultEntityManager<Object,Long>{
	/**
	 * Spring jdbc工具类.
	 */
	@Autowired
	protected SsmJdbcDao jdbcDao;
	
	/**
	 * 根据表名、字段名、字段值返回该对象id 无改对象则返回null.
	 * @param tableName 表名
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getIdByTFO(String tableName, String fieldName,
			Object fieldValue) throws SsmDaoException, SsmSystemException,
			SsmServiceException {
		Assert.notNull(tableName, "参数[tableName]为空!");
		Assert.notNull(fieldName, "参数[fieldName]为空!");
		Map<String, Object> map = Maps.newHashMap();
	    map.put(fieldName, fieldValue);
	    StringBuilder sb = new StringBuilder();
	    sb.append("select id from ").append(tableName).append(" where ").append(fieldName).append(" = ?");
	    Map<String, Object> result = jdbcDao.queryForMap(sb.toString(), fieldValue);
	    Long id = null;
	    if(result!=null){
	    	id =  (Long) result.get("id");
	    }
	    return id;
	}

	/**
	 * 根据某个属性得到单个对象.
	 *
	 * @param entityName
	 *            BO名称 例如: "Resource"
	 * @param propertyName
	 *            属性名 例如:"name"
	 * @param propertyValue
	 *            属性值
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getIdByProperty(String entityName, String propertyName,
			Object propertyValue) throws SsmDaoException, SsmSystemException,
			SsmServiceException {
		Assert.notNull(entityName, "参数[entityName]为空!");
		Assert.notNull(propertyName, "参数[propertyName]为空!");
		StringBuilder sb = new StringBuilder();
		sb.append("select e.id from e.").append(entityName).append(" e where ")
				.append(propertyName).append(" = ?");
		logger.debug(sb.toString());
		Iterator<?> iterator = entityDao.createQuery(sb.toString(),
				new Object[] { propertyValue }).iterate();
		Long id = null;
		while (iterator.hasNext()) {
			id = (Long) iterator.next();
		}
		return id;
	}
}
