/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.notice.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.modules.disk.utils.SsmDiskUtils;
import com.ssm.pay.modules.notice._enum.SsmIsTop;
import com.ssm.pay.modules.notice._enum.SsmNoticeMode;
import com.ssm.pay.modules.notice.entity.SsmNotice;
import com.ssm.pay.modules.notice.entity.SsmNoticeScope;
import com.ssm.pay.modules.notice.vo.SsmNoticeQueryVo;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.modules.sys.service.SsmOrganManager;
import com.ssm.pay.modules.sys.service.SsmUserManager;
import com.ssm.pay.utils.SsmYesOrNo;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午11:04:55
*/
@Service
public class SsmNoticeManager extends SsmEntityManager<SsmNotice, Long> {
	@Autowired
	private SsmUserManager userManager;
	@Autowired
	private SsmOrganManager organManager;
	@Autowired
	private SsmNoticeScopeManager noticeScopeManager;


    private SsmHibernateDao<SsmNotice, Long> noticeDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		noticeDao = new SsmHibernateDao<SsmNotice, Long>(sessionFactory, SsmNotice.class);
	}

	@Override
	protected SsmHibernateDao<SsmNotice, Long> getEntityDao() {
		return noticeDao;
	}

    /**
     * 删除通知
     * @param noticeId
     */
    public void removeNotice(Long noticeId){
    	SsmNotice notice = this.loadById(noticeId);
        if(notice != null){
            List<SsmNoticeScope> noticeScopes = noticeScopeManager.getNoticeScopesByNoticeId(noticeId,null);
            noticeScopeManager.deleteAll(noticeScopes);
            List<Long> fileIds = notice.getFileIds();
            if (SsmCollections3.isNotEmpty(fileIds)) {
                for (Long fileId : fileIds) {
                	SsmDiskUtils.deleteFile(null, fileId);
                }
            }
            this.delete(notice);
        }
    }

    /**
     * 属性过滤器查找得到分页数据.
     *
     * @param page 分页对象
     * @param userId 发布人 查询所有则传null
     * @param noticeQueryVo 标查询条件
     * @return
     * @throws SystemException
     * @throws ServiceException
     * @throws DaoException
     */
	public SsmPage<SsmNotice> findPage(SsmPage<SsmNotice> page, Long userId, SsmNoticeQueryVo noticeQueryVo)
			throws SsmSystemException, SsmServiceException, SsmDaoException {
        StringBuilder hql = new StringBuilder();
        SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue());
        hql.append(" select n from SsmNotice n where n.status = :p1");
        if(userId != null){
            hql.append(" and n.userId = :userId");
            parameter.put("userId",userId);
        }

        if(noticeQueryVo != null){
            if (noticeQueryVo.getIsTop() != null) {
                hql.append(" and n.isTop = :isTop");
                parameter.put("isTop", noticeQueryVo.getIsTop());
            }
            if (noticeQueryVo.getIsRead() != null) {
                hql.append(" and n.isRead = :isRead");
                parameter.put("isRead", noticeQueryVo.getIsRead());
            }

            if (SsmStringUtils.isNotBlank(noticeQueryVo.getTitle())) {
                hql.append(" and n.title like :title");
                parameter.put("title","%" + noticeQueryVo.getTitle() + "%");
            }
            if (SsmStringUtils.isNotBlank(noticeQueryVo.getContent())) {
                hql.append(" and n.content like :content");
                parameter.put("content","%" + noticeQueryVo.getContent() + "%");
            }
            if (SsmCollections3.isNotEmpty(noticeQueryVo.getPublishUserIds())) {
                hql.append(" and n.userId in (:publishUserIds)");
                parameter.put("publishUserIds", noticeQueryVo.getPublishUserIds());
            }

            if (noticeQueryVo.getStartTime() != null && noticeQueryVo.getEndTime() != null) {
                hql.append(" and  (n.publishTime between :startTime and :endTime)");
                parameter.put("startTime", noticeQueryVo.getStartTime());
                parameter.put("endTime", noticeQueryVo.getEndTime());
            }
        }

        hql.append(" order by n.publishTime desc");
        return noticeDao.findPage(page,hql.toString(),parameter);

	}

    /**
     * 我的邮件 分页查询.
     * @param page
     * @param userId 用户ID
     * @param noticeQueryVo 查询条件
     * @return
     * @throws SystemException
     * @throws ServiceException
     * @throws DaoException
     */
	public SsmPage<SsmNoticeScope> findReadNoticePage(SsmPage<SsmNoticeScope> page, Long userId, SsmNoticeQueryVo noticeQueryVo) throws SsmSystemException,
		SsmServiceException, SsmDaoException {
		Assert.notNull(userId, "参数[userId]为空!");
		StringBuilder hql = new StringBuilder();
		SsmParameter parameter = new SsmParameter(userId, SsmStatusState.normal.getValue(),SsmNoticeMode.Effective.getValue());
		hql.append("select s from SsmNotice n,SsmNoticeScope s where s.noticeId = n.id and s.userId = :p1 and n.status = :p2 and n.noticeMode = :p3");
        if(noticeQueryVo != null){
            if (noticeQueryVo.getIsTop() != null) {
                hql.append(" and n.isTop = :isTop");
                parameter.put("isTop", noticeQueryVo.getIsTop());
            }
            if (noticeQueryVo.getIsRead() != null) {
                hql.append(" and n.isRead = :isRead");
                parameter.put("isRead", noticeQueryVo.getIsRead());
            }

            if (SsmStringUtils.isNotBlank(noticeQueryVo.getTitle())) {
                hql.append(" and n.title like :title");
                parameter.put("title","%" + noticeQueryVo.getTitle() + "%");
            }
            if (SsmStringUtils.isNotBlank(noticeQueryVo.getContent())) {
                hql.append(" and n.content like :content");
                parameter.put("content","%" + noticeQueryVo.getContent() + "%");
            }
            if (SsmCollections3.isNotEmpty(noticeQueryVo.getPublishUserIds())) {
                hql.append(" and n.userId in (:publishUserIds)");
                parameter.put("publishUserIds", noticeQueryVo.getPublishUserIds());
            }

            if (noticeQueryVo.getStartTime() != null && noticeQueryVo.getEndTime() != null) {
                hql.append(" and  (n.publishTime between :startTime and :endTime)");
                parameter.put("startTime", noticeQueryVo.getStartTime());
                parameter.put("endTime", noticeQueryVo.getEndTime());
            }
        }

		hql.append(" order by n.publishTime desc");
        return noticeScopeManager.findPage(page,hql.toString(),parameter);
	}

    /**
     * 发布公告
     *
     * @param noticeId
     *            公告ID
     */
    public void publish(Long noticeId) {
    	SsmNotice notice = this.loadById(noticeId);
        if (notice == null) {
            throw new SsmServiceException("公告[" + noticeId + "]不存在.");
        }
        publish(notice);
    }

	/**
	 * 发布公告
	 * 
	 * @param notice 通知
	 */
	public void publish(SsmNotice notice) {
        Date nowTime = Calendar.getInstance().getTime();
        //已经发布过 删除接收对象记录
        if(SsmNoticeMode.Effective.getValue().equals(notice.getNoticeMode())){
            List<SsmNoticeScope> noticeScopes = noticeScopeManager.getNoticeScopesByNoticeId(notice.getId(),null);
            noticeScopeManager.deleteAll(noticeScopes);
        }

        notice.setNoticeMode(SsmNoticeMode.Effective.getValue());
        notice.setPublishTime(nowTime);
		this.saveOrUpdate(notice);
        if(SsmYesOrNo.YES.getValue().equals(notice.getIsToAll())){
            List<SsmUser> userList = userManager.getAllNormal();
            for(SsmUser user:userList){
            	SsmNoticeScope noticeScope = new SsmNoticeScope();
                noticeScope.setNoticeId(notice.getId());
                noticeScope.setUserId(user.getId());
                noticeScope.setOrganId(user.getDefaultOrganId());
                noticeScopeManager.save(noticeScope);
            }

        }else{
            if (SsmCollections3.isNotEmpty(notice.getNoticeOrganIds())) {
                for (Long organId : notice.getNoticeOrganIds()) {
                    List<SsmUser> users = organManager.getById(organId).getUsers();
                    if (SsmCollections3.isNotEmpty(users)) {
                        for (SsmUser user : users) {
                            List<SsmNoticeScope> userNoticeScopes = noticeScopeManager.getNoticeScopesByNoticeId(notice.getId(),user.getId());
                            if(SsmCollections3.isNotEmpty(userNoticeScopes)){
                                break;
                            }
                            SsmNoticeScope noticeScope = new SsmNoticeScope();
                            noticeScope.setNoticeId(notice.getId());
                            noticeScope.setUserId(user.getId());
                            noticeScope.setOrganId(organId);
                            noticeScopeManager.save(noticeScope);
                        }
                    }
                }
            }

            if (SsmCollections3.isNotEmpty(notice.getNoticeUserIds())) {
                for (Long userId : notice.getNoticeUserIds()) {
                    List<SsmNoticeScope> userNoticeScopes = noticeScopeManager.getNoticeScopesByNoticeId(notice.getId(),userId);
                    if(SsmCollections3.isNotEmpty(userNoticeScopes)){
                        break;
                    }
                    SsmUser user = userManager.getById(userId);
                    SsmNoticeScope noticeScope = new SsmNoticeScope();
                    noticeScope.setNoticeId(notice.getId());
                    noticeScope.setUserId(userId);
                    noticeScope.setOrganId(user.getDefaultOrganId());
                    noticeScopeManager.save(noticeScope);
                }
            }
        }

	}

    public void saveFromModel(SsmNotice notice,boolean isPublish){
        this.saveEntity(notice);
        if(isPublish){
            this.publish(notice.getId());
        }
    }

    /**
     * 标记为已读
     * @param userId 所属用户ID
     * @param noticeIds 通知ID集合
     */
    public void markReaded(Long userId,List<Long> noticeIds){
        if (SsmCollections3.isNotEmpty(noticeIds)) {
            for (Long id : noticeIds) {
                noticeScopeManager.setRead(userId,id);
            }
        } else {
            logger.warn("参数[entitys]为空.");
        }

    }

    
    /**
     * 虚拟删除通知
     * @param ids
     */
	public void remove(List<Long> ids) {
		if (SsmCollections3.isNotEmpty(ids)) {
			for (Long id : ids) {
				SsmNotice notice = getById(id);
				notice.setStatus(SsmStatusState.delete.getValue());
				saveEntity(notice);
			}
		}
	}


    /**
     * 查找通知附件ID
     * @param noticeId
     * @return
     */
    public List<Long> getFileIds(Long noticeId){
        List<SsmNotice> list = getEntityDao().find("select n from SsmNotice n where n.id = :p1",new SsmParameter(noticeId));
        SsmNotice notice = list.isEmpty() ? null:list.get(0);
        if(notice !=null){
            return notice.getFileIds();
        }
        return null;
    }

    /**
     * 轮询通知 定时发布、到时失效、取消置顶
     * @throws SystemException
     * @throws ServiceException
     * @throws DaoException
     */
    public void pollNotice() throws SsmSystemException, SsmServiceException,
    		SsmDaoException {
    	SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue(),SsmNoticeMode.Invalidated.getValue());
        // 查询到今天为止所有未删除的通知
        String hql = " from SsmNotice n where n.status= :p1 and n.noticeMode <> :p2";
        Date nowTime = Calendar.getInstance().getTime();
        List<SsmNotice> noticeList = getEntityDao().find(hql, parameter);
        if (SsmCollections3.isNotEmpty(noticeList)) {
            for (SsmNotice n : noticeList) {
                if (SsmNoticeMode.UnPublish.getValue().equals(n.getNoticeMode())
                        && n.getEffectTime() != null
                        && nowTime.compareTo(n.getEffectTime()) != -1) {//定时发布
                    this.publish(n);
                }else if (SsmNoticeMode.Effective.getValue().equals(n.getNoticeMode())
                        && n.getEndTime() != null
                        && nowTime.compareTo(n.getEndTime()) != -1) {//到时失效
                    n.setNoticeMode(SsmNoticeMode.Invalidated.getValue());
                    getEntityDao().update(n);
                }
                //取消置顶
                if (SsmIsTop.Yes.getValue().equals(n.getIsTop())
                        && n.getEndTopDay() != null && n.getEndTopDay() >0) {
                    Date publishTime = (n.getPublishTime() == null) ? nowTime: n.getPublishTime();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(publishTime);
                    cal.add(Calendar.DATE, n.getEndTopDay());
                    if (nowTime.compareTo(cal.getTime()) != -1) {
                        n.setIsTop(SsmIsTop.No.getValue());
                        getEntityDao().update(n);
                    }
                }
            }
        }
    }
}
