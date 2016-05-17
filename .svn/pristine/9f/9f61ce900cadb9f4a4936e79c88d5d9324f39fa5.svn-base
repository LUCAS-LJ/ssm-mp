/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.service;

import java.util.List;
import org.apache.commons.lang3.Validate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.modules.sys.entity.SsmPost;
import com.ssm.pay.modules.sys.entity.SsmUser;

/** 岗位管理 Service
* @author Jiang.Li
* @version 2016年1月29日 下午2:17:55
*/
@Service
public class SsmPostManager  extends SsmEntityManager<SsmPost, Long>{
	private SsmHibernateDao<SsmPost, Long> postDao;

    @Autowired
    private SsmUserManager userManager;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        postDao = new SsmHibernateDao<SsmPost, Long>(
                sessionFactory, SsmPost.class);
    }

    @Override
    protected SsmHibernateDao<SsmPost, Long> getEntityDao() {
        return postDao;
    }

    /**
     * 根据机构ID以及岗位名称查找
     * @param organId 机构ID
     * @param postName 岗位名称
     */
    public SsmPost getPostByON(Long organId,String postName){
        Validate.notNull(organId, "参数[organId]不能为null");
        Validate.notNull(postName, "参数[postName]不能为null或空");
        SsmParameter parameter = new SsmParameter(organId,postName);
        List<SsmPost> list = getEntityDao().find("from Post p where p.organ.id = :p1 and p.name = :p2",parameter);
        return list.isEmpty() ? null:list.get(0);
    }


    /**
     * 根据机构ID以及岗位编码查找
     * @param organId 机构ID
     * @param postCode 岗位编码
     */
    public SsmPost getPostByOC(Long organId,String postCode){
        Validate.notNull(organId, "参数[organId]不能为null");
        Validate.notNull(postCode, "参数[postCode]不能为null或空");
        SsmParameter parameter = new SsmParameter(organId,postCode);
        List<SsmPost> list = getEntityDao().find("from Post p where p.organ.id = :p1 and p.code = :p2",parameter);
        return list.isEmpty() ? null:list.get(0);
    }


    /**
     * 得到岗位所在部门的所有用户
     * @param postId 岗位ID
     * @return
     */
    public List<SsmUser> getPostOrganUsersByPostId(Long postId){
        Validate.notNull(postId, "参数[postId]不能为null");
        SsmPost post = super.loadById(postId);
        if(post == null){
            return null;
        }
        return post.getOrgan().getUsers();
    }


    /**
     * 用户可选岗位列表
     * 如果organId不为null，则忽略参数userId；
     * 如果userId、organId都为空，则返回所有.
     * @param userId 用户ID
     * @param organId 机构ID
     * @return
     */
    public List<SsmPost> getSelectablePosts(Long userId,Long organId) {
        if(userId ==null && organId == null){
            logger.warn("参数[userId，organId]至少有一个不为null.");
        }

        List<SsmPost> list = null;
        StringBuffer hql = new StringBuffer();
        SsmParameter parameter = new SsmParameter();
        hql.append("from Post p where p.status = :status ");
        parameter.put("status",SsmStatusState.normal.getValue());
        if(organId != null){
            hql.append(" and  p.organ.id = :organId");
            parameter.put("organId",organId);
        }else{
            if (userId != null) {
            	SsmUser user = userManager.loadById(userId);
                List<Long> userOrganIds = user.getOrganIds();
                if(SsmCollections3.isNotEmpty(userOrganIds)){
                    hql.append(" and  p.organ.id in (:userOrganIds)");
                    parameter.put("userOrganIds",userOrganIds);
                }else{
                    logger.warn("用户[{}]未设置部门.",new Object[]{user.getLoginName()});
                }

            }

        }
        list = getEntityDao().find(hql.toString(),parameter);
        return list;
    }
}
