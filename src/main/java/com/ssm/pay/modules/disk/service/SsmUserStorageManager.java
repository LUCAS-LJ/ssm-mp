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
import com.ssm.pay.modules.disk.entity.SsmUserStorage;
import com.ssm.pay.utils.SsmAppConstants;

/** 用户云盘存储空间配置 管理
* @author Jiang.Li
* @version 2016年4月14日 下午5:43:03
*/
@Service
public class SsmUserStorageManager extends SsmEntityManager<SsmUserStorage, Long> {
	private SsmHibernateDao<SsmUserStorage, Long> userStorageDao;


    /**
     * 通过注入的sessionFactory初始化默认的泛型DAO成员变量.
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        userStorageDao = new SsmHibernateDao<SsmUserStorage, Long>(sessionFactory, SsmUserStorage.class);
    }

    @Override
    protected SsmHibernateDao<SsmUserStorage, Long> getEntityDao() {
        return userStorageDao;
    }

    /**
     * 查找用户云盘存储空间配置信息
     * @param userId 用户ID
     * @return
     */
    public SsmUserStorage getUserStorage(Long userId){
        Validate.notNull(userId, "参数[userId]不能为null.");
        StringBuffer hql = new StringBuffer();
        hql.append("from UserStorage e where e.userId = :p1");
        SsmParameter parameter = new SsmParameter(userId);
        List<SsmUserStorage> list =  getEntityDao().find(hql.toString(),parameter);
        return list.isEmpty() ? null:list.get(0);
    }

    /**
     * 查找用户可用存储字节数
     * @param userId 用户ID
     * @return
     */
    public long getUserAvaiableStorage(Long userId) {
        Validate.notNull(userId, "参数[userId]不能为null.");
        SsmUserStorage userStorage = getUserStorage(userId);
        int diskUserLimitSize = SsmAppConstants.getDiskUserLimitSize().intValue();
        if (userStorage != null && userStorage.getLimitSize() != null) {
            diskUserLimitSize = userStorage.getLimitSize();
        }
        return Long.valueOf(diskUserLimitSize) * 1024L * 1024L;
    }
}
