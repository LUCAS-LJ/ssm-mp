/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.service;

import java.util.List;
import org.apache.commons.lang3.Validate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.modules.disk.entity.SsmOrganStorage;
import com.ssm.pay.utils.SsmAppConstants;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午10:05:57
*/
@Service
public class SsmOrganStorageManager extends SsmEntityManager<SsmOrganStorage, Long> {
	 private SsmHibernateDao<SsmOrganStorage, Long> organStorageDao;


	    /**
	     * 通过注入的sessionFactory初始化默认的泛型DAO成员变量.
	     */
	    @Autowired
	    public void setSessionFactory(final SessionFactory sessionFactory) {
	        organStorageDao = new SsmHibernateDao<SsmOrganStorage, Long>(sessionFactory, SsmOrganStorage.class);
	    }

	    @Override
	    protected SsmHibernateDao<SsmOrganStorage, Long> getEntityDao() {
	        return organStorageDao;
	    }


	    /**
	     * 查找部门云盘存储空间配置信息
	     * @param organId 部门ID
	     * @return
	     */
	    public SsmOrganStorage getOrganStorage(Long organId){
	        Validate.notNull(organId, "参数[organId]不能为null.");
	        StringBuffer hql = new StringBuffer();
	        hql.append("from OrganStorage e where e.organId = :p1");
	        SsmParameter parameter = new SsmParameter(organId);
	        List<SsmOrganStorage> list =  getEntityDao().find(hql.toString(),parameter);
	        return list.isEmpty() ? null:list.get(0);
	    }


	    /**
	     * 查找部门可用存储字节数
	     * @param organId 用户ID
	     * @return
	     */
	    public long getOrganAvaiableStorage(Long organId) {
	        Validate.notNull(organId, "参数[organId]不能为null.");
	        SsmOrganStorage organStorage = getOrganStorage(organId);
	        int diskOrganLimitSize = SsmAppConstants.getDiskOrganLimitSize().intValue();
	        if (organStorage != null && organStorage.getLimitSize() != null) {
	            diskOrganLimitSize = organStorage.getLimitSize();
	        }
	        return Long.valueOf(diskOrganLimitSize) * 1024L * 1024L;
	    }
}
