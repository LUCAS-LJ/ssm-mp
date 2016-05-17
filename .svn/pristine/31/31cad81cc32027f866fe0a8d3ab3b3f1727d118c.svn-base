/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.service;

import java.util.Date;
import java.util.GregorianCalendar;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.pay.common.exception.SsmDaoException;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.modules.sys.entity.SsmLog;

/** 
* @author Jiang.Li
* @version 2016年2月27日 下午11:19:28
*/
@Service
public class SsmLogManager extends SsmEntityManager<SsmLog, Long>{
	private SsmHibernateDao<SsmLog, Long> logDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        logDao = new SsmHibernateDao<SsmLog, Long>(
                sessionFactory, SsmLog.class);
    }

    @Override
    protected SsmHibernateDao<SsmLog, Long> getEntityDao() {
        return logDao;
    }

    /**
     * 清空所有日志
     * @throws DaoException
     * @throws SystemException
     */
    public void removeAll() throws SsmDaoException,SsmSystemException{
        int reslutCount = getEntityDao().batchExecute("delete from Log ");
        logger.debug("清空日志：{}",reslutCount);
    }

    /**
     * 清空有效期之外的日志
     * @param  day 保留时间 （天）
     * @throws DaoException
     * @throws SystemException
     */
    public void clearInvalidLog(int day) throws SsmDaoException,SsmSystemException{
        if(day <0){
            throw new SsmSystemException("参数[day]不合法，需要大于0.输入为："+day);
        }
        Date now = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(now); // 得到gc格式的时间
        gc.add(5, -day); // 2表示月的加减，年代表1依次类推(３周....5天。。)
        // 把运算完的时间从新赋进对象
        gc.set(gc.get(gc.YEAR), gc.get(gc.MONTH), gc.get(gc.DATE));
        SsmParameter parameter = new SsmParameter(gc.getTime());
        logDao.createQuery("delete from Log l where l.operTime < :operTime", parameter).executeUpdate();
    }
}
