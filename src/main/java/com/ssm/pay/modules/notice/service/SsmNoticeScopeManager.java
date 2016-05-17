/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.notice.service;

import java.util.Calendar;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ssm.pay.common.exception.SsmDaoException;
import com.ssm.pay.common.exception.SsmServiceException;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.orm.SsmPage;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.modules.notice._enum.SsmNoticeMode;
import com.ssm.pay.modules.notice._enum.SsmNoticeReadMode;
import com.ssm.pay.modules.notice.entity.SsmNoticeScope;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午10:51:57
*/
@Service
public class SsmNoticeScopeManager extends SsmEntityManager<SsmNoticeScope, Long> {
	private SsmHibernateDao<SsmNoticeScope, Long> noticeScopeDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        noticeScopeDao = new SsmHibernateDao<SsmNoticeScope, Long>(sessionFactory, SsmNoticeScope.class);
    }

    @Override
    protected SsmHibernateDao<SsmNoticeScope, Long> getEntityDao() {
        return noticeScopeDao;
    }

    /**
     * 查看某个通知阅读情况
     * @param page
     * @param noticeId 通知ID
     * @return
     * @throws com.eryansky.common.exception.SystemException
     * @throws com.eryansky.common.exception.ServiceException
     * @throws com.eryansky.common.exception.DaoException
     */
    public SsmPage<SsmNoticeScope> findReadInfoPage(SsmPage<SsmNoticeScope> page, Long noticeId)
            throws SsmSystemException, SsmServiceException, SsmDaoException {
        Assert.notNull(noticeId, "参数[noticeId]为空!");
        StringBuilder hql = new StringBuilder();
        SsmParameter parameter = new SsmParameter(noticeId);
        hql.append("from SsmNoticeScope s where  s.noticeId = :p1");
        return getEntityDao().findPage(page,hql.toString(),parameter);
    }

    /**
     * 根据通知ID查找
     * @param noticeId 通知ID
     * @return
     */
    public List<SsmNoticeScope> getNoticeScopesByNoticeId(Long noticeId) {
        return getNoticeScopesByNoticeId(noticeId,null);
    }

    /**
     * 根据通知ID查找
     * @param noticeId 通知ID
     * @param userId 用户ID 可为null
     * @return
     */
    public List<SsmNoticeScope> getNoticeScopesByNoticeId(Long noticeId, Long userId) {
    	SsmParameter parameter = new SsmParameter(noticeId);
        StringBuffer hql = new StringBuffer();
        hql.append("from SsmNoticeScope s where s.noticeId = :p1");
        if(userId != null){
            hql.append(" and s.userId = :userId");
            parameter.put("userId",userId);
        }
        return getEntityDao().find(hql.toString(), parameter);
    }

    /**
     * 获取用户最近通知列表
     * @param userId 用户ID
     * @param maxSize 数量 默认值：{@link Page.DEFAULT_PAGESIZE}
     * @return
     * @throws SystemException
     * @throws ServiceException
     * @throws DaoException
     */
    public List<SsmNoticeScope> getUserNewNotices(Long userId, Integer maxSize) throws SsmSystemException,
    		SsmServiceException, SsmDaoException {
        StringBuilder hql = new StringBuilder();
        SsmParameter parameter = new SsmParameter(userId, SsmStatusState.normal.getValue(), SsmNoticeMode.Effective.getValue());
        hql.append("select s from SsmNotice n,SsmNoticeScope s where s.noticeId = n.id and s.userId = :p1 and n.status = :p2 and n.noticeMode = :p3")
                .append(" order by n.publishTime desc,n.isTop desc,s.isRead asc");
        Query query = getEntityDao().createQuery(hql.toString(), parameter);
        query.setFirstResult(0);
        query.setMaxResults(maxSize == null ? SsmPage.DEFAULT_PAGESIZE:maxSize);
        List<SsmNoticeScope> list = query.list();
        return list;

    }

    /**
     * 用户未读通知数量
     * @param userId 用户ID
     * @return
     * @throws SystemException
     * @throws ServiceException
     * @throws DaoException
     */
    public long getUserUnreadNoticeNum(Long userId)throws SsmSystemException,SsmServiceException, SsmDaoException {
        StringBuilder hql = new StringBuilder();
        SsmParameter parameter = new SsmParameter(userId, SsmNoticeReadMode.unreaded.getValue(), SsmStatusState.normal.getValue(), SsmNoticeMode.Effective.getValue());
        hql.append("select count(s.id) from SsmNotice n,SsmNoticeScope s where s.noticeId = n.id and s.userId = :p1 and s.isRead = :p2 and n.status = :p3 and n.noticeMode = :p4");
        List<Object> list = getEntityDao().find(hql.toString(),parameter);
        return (Long)list.get(0);
    }


    /**
     * 用户是否阅读通知
     * @param userId  用户ID
     * @param noticeId 通知ID
     * @return
     * @throws SystemException
     * @throws ServiceException
     * @throws DaoException
     */
    public boolean isRead(Long userId, Long noticeId) throws SsmSystemException,
    		SsmServiceException, SsmDaoException {
    	SsmParameter parameter = new SsmParameter(userId, noticeId, SsmNoticeReadMode.readed.getValue());
        StringBuffer hql = new StringBuffer();
        hql.append("select count(*) from SsmNoticeScope s where s.userId = :p1 and s.noticeId = :p2 and s.isRead = :p3");
        List<Object> list = getEntityDao().find(hql.toString(),parameter);
        Long count = (Long)list.get(0);
        if (count > 0L) {
            return true;
        }
        return false;
    }

    /**
     * 设置已读通知
     *
     * @param userId
     * @param noticeId
     */
    public synchronized int setRead(Long userId, Long noticeId) {
    	SsmParameter parameter = new SsmParameter(SsmNoticeReadMode.readed.getValue(), Calendar.getInstance().getTime(), noticeId, userId);
        return getEntityDao().createQuery("update SsmNoticeScope s set s.isRead = :p1,s.readTime = :p2 where s.noticeId = :p3 and s.userId= :p4", parameter).executeUpdate();
    }
}
