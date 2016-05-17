/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
import com.ssm.pay.common.exception.SsmDaoException;
import com.ssm.pay.common.exception.SsmServiceException;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.orm.SsmPage;
import com.ssm.pay.common.orm.SsmPropertyFilter;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.modules.sys.entity.SsmOrgan;
import com.ssm.pay.modules.sys.entity.SsmPost;
import com.ssm.pay.modules.sys.entity.SsmResource;
import com.ssm.pay.modules.sys.entity.SsmRole;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.utils.SsmCacheConstants;

/** 用户管理User Service层实现类.
* @author Jiang.Li
* @version 2016年1月29日 下午2:03:17
*/
@Service
public class SsmUserManager extends SsmEntityManager<SsmUser, Long>{
	private SsmHibernateDao<SsmUser, Long> userDao;

    @Autowired
    private SsmOrganManager organManager;
    @Autowired
    private SsmRoleManager roleManager;
    @Autowired
    private SsmPostManager postManager;
    @Autowired
    private SsmResourceManager resourceManager;

    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		userDao = new SsmHibernateDao<SsmUser, Long>(sessionFactory, SsmUser.class);
	}
	
	@Override
	protected SsmHibernateDao<SsmUser, Long> getEntityDao() {
		return userDao;
	}

    /**
     * 新增或修改角色.
     * <br>修改角色的时候 会给角色重新授权菜单 更新导航菜单缓存.
     */
    @CacheEvict(value = {  SsmCacheConstants.ROLE_ALL_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE,
            SsmCacheConstants.ORGAN_USER_TREE_CACHE},allEntries = true)
    public void saveOrUpdate(SsmUser entity) throws SsmDaoException,SsmSystemException,SsmServiceException {
        logger.debug("清空缓存:{}",SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE
                +","+SsmCacheConstants.ORGAN_USER_TREE_CACHE);
        userDao.saveOrUpdate(entity);
    }

    /**
     * 新增或修改角色.
     * <br>修改角色的时候 会给角色重新授权菜单 更新导航菜单缓存.
     */
    @CacheEvict(value = {  SsmCacheConstants.ROLE_ALL_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE,
            SsmCacheConstants.ORGAN_USER_TREE_CACHE},allEntries = true)
    public void merge(SsmUser entity) throws SsmDaoException,SsmSystemException,SsmServiceException {
        logger.debug("清空缓存:{}",SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE
                +","+SsmCacheConstants.ORGAN_USER_TREE_CACHE);
        userDao.merge(entity);
    }

    /**
     * 新增或修改角色.
     * <br>修改角色的时候 会给角色重新授权菜单 更新导航菜单缓存.
     */
    @CacheEvict(value = {  SsmCacheConstants.ROLE_ALL_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE,
            SsmCacheConstants.ORGAN_USER_TREE_CACHE},allEntries = true)
    @Override
    public void saveEntity(SsmUser entity) throws SsmDaoException, SsmSystemException, SsmServiceException {
        logger.debug("清空缓存:{}",SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE
                +","+SsmCacheConstants.ORGAN_USER_TREE_CACHE);
        super.saveEntity(entity);
    }

    /**
	 * 自定义删除方法.
	 */
    @CacheEvict(value = {  SsmCacheConstants.ROLE_ALL_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE,
            SsmCacheConstants.ORGAN_USER_TREE_CACHE},allEntries = true)
	public void deleteByIds(List<Long> ids) throws SsmDaoException,SsmSystemException,SsmServiceException {
        logger.debug("清空缓存:{}",SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE
                +","+SsmCacheConstants.ORGAN_USER_TREE_CACHE);
		if(!SsmCollections3.isEmpty(ids)){
			for(Long id :ids){
				SsmUser superUser = this.getSuperUser();
				if (id.equals(superUser.getId())) {
					throw new SsmSystemException("不允许删除超级用户!");
				}
				SsmUser user = userDao.get(id);
				if(user != null){
					//清空关联关系
                    user.setDefaultOrgan(null);
                    user.setOrgans(null);
					user.setRoles(null);
                    user.setResources(null);
					//逻辑删除
					//手工方式(此处不使用 由注解方式实现逻辑删除)
//					user.setStatus(StatusState.delete.getValue());
					//注解方式 由注解设置用户状态
					userDao.delete(user);
				}
			}
		}else{
			logger.warn("参数[ids]为空.");
		}
	}
	
	/**
	 * 得到当前登录用户.
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public SsmUser getCurrentUser() throws SsmDaoException,SsmSystemException,SsmServiceException{
		SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
		SsmUser user = getEntityDao().load(sessionInfo.getUserId());
        return user;
    }

	/**
	 * 得到超级用户.
	 *
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public SsmUser getSuperUser() throws SsmDaoException,SsmSystemException,SsmServiceException {
		SsmUser superUser = userDao.load(1l);//超级用户ID为1
        if(superUser == null){
            throw new SsmSystemException("系统未设置超级用户.");
        }
        return superUser;
	}

    /**
     * 判断当前用户是否是超级用户
     * @param userId 用户Id
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public boolean isSuperUser(Long userId) throws SsmDaoException,SsmSystemException,SsmServiceException{
        boolean flag = false;
        SsmUser user = getEntityDao().load(userId);
        SsmUser superUser = getSuperUser();

        if(user != null && user.getId().equals(superUser.getId())){
            flag = true;
        }
        return flag;
    }

	/**
	 * 根据登录名、密码查找用户.
	 * <br/>排除已删除的用户
	 * @param loginName
	 *            登录名
	 * @param password
	 *            密码
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public SsmUser getUserByLP(String loginName, String password)
			throws SsmDaoException,SsmSystemException,SsmServiceException {
		Assert.notNull(loginName, "参数[loginName]为空!");
		Assert.notNull(password, "参数[password]为空!");
		SsmParameter parameter = new SsmParameter(loginName, password,SsmStatusState.delete.getValue());
		List<SsmUser> list = userDao.find(
					"from User u where u.loginName = :p1 and u.password = :p2 and u.status <> :p3",parameter);
		return list.isEmpty() ? null:list.get(0);
	}

	/**
	 * 根据登录名查找.
	 * <br>注：排除已删除的对象
	 * @param loginName 登录名
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public SsmUser getUserByLoginName(String loginName)
			throws SsmDaoException,SsmSystemException,SsmServiceException {
		Assert.notNull(loginName, "参数[loginName]为空!");
		Assert.notNull(loginName, "参数[status]为空!");
		SsmParameter parameter = new SsmParameter(loginName, SsmStatusState.delete.getValue());
        List<SsmUser> list = userDao.find(
                "from User u where u.loginName = :p1 and u.status <> :p2",parameter);
		return list.isEmpty() ? null:list.get(0);
	}

    /**
     * 获得所有可用用户
     * @return
     */
    public List<SsmUser> getAllNormal(){
    	SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue());
        return userDao.find("from User u where u.status = :p1",parameter);
    }

    /**
     * 根据组织机构Id以及登录名或姓名分页查询
     * @param organId 组织机构ID （注；如果organSysCode参数不为空则 忽略organId）
     * @param organSysCode 组织机构系统编码
     * @param loginNameOrName 姓名或手机号码
     * @param page 排序方式 增序:'asc',降序:'desc'
     * @return
     */
    public SsmPage<SsmUser> getUsersByQuery(Long organId,String organSysCode, String loginNameOrName, Integer userType,SsmPage<SsmUser> page) {
        //条件都为空的时候能够查询出所有数据
        if(organId == null && SsmStringUtils.isBlank(organSysCode) && SsmStringUtils.isBlank(loginNameOrName) && userType == null){
//            return super.find(page,rows,sort,order,new ArrayList<PropertyFilter>());
            page.setOrderBy("defaultOrgan.id,orderNo");
            page.setOrder("asc,asc");
            return super.findPage(page,new ArrayList<SsmPropertyFilter>());
        }
        SsmParameter parameter = new SsmParameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select distinct(u) from User u left join u.organs uo  where u.status <> :status ");
        List<Long> organIds = null;
        parameter.put("status",SsmStatusState.delete.getValue());
        if(SsmStringUtils.isNotBlank(organSysCode)){
        	SsmOrgan organ = organManager.findUniqueBy("sysCode",organSysCode);
//            hql.append("and uo.sysCode like :sysCode ");
            hql.append("and (uo.sysCode like :sysCode or uo.sysCode is null) ");
            parameter.put("sysCode",organ.getSysCode()+"%");
        }else {
            if(organId !=null){
                hql.append("and uo.id = :organId ");
                parameter.put("organId",organId);
            }
        }
        if(SsmStringUtils.isNotBlank(loginNameOrName)){
            hql.append("and (u.loginName like :loginName or u.name like :name) ");
            parameter.put("loginName","%"+loginNameOrName+"%");
            parameter.put("name","%"+loginNameOrName+"%");
        }
        if(userType != null){
            hql.append("and u.userType = :userType ");
            parameter.put("userType",userType);
        }

        //设置分页
        page = userDao.findPage(page,hql.toString(),parameter);

        //重新计算总数 特殊处理 hql包含distinct语句导致总数出错问题
        String fromHql = hql.toString();
        // select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + SsmStringUtils.substringAfter(fromHql, "from");
        fromHql = SsmStringUtils.substringBefore(fromHql, "order by");

        String countHql = "select count(distinct u.id) " + fromHql;
        Query query = getEntityDao().createQuery(countHql, parameter);
        List<Object> list = query.list();
        Long count = 0L;
        if (list.size() > 0) {
            count = (Long)list.get(0);
        } else {
            count = Long.valueOf(list.size());
        }
        page.setTotalCount(count);

        return page;
    }

    /**
     * 根据组织机构Id以及登录名或姓名分页查询
     * @param inUserIds 用户id集合
     * @param loginNameOrName 姓名或手机号码
     * @param page 第几页
     * @param rows 页大小
     * @param sort 排序字段
     * @param order 排序方式 增序:'asc',降序:'desc'
     * @return
     */
    public SsmPage<SsmUser> getUsersByQuery(List<Long> inUserIds, String loginNameOrName, int page, int rows, String sort, String order) {
    	SsmParameter parameter = new SsmParameter();
        StringBuilder hql = new StringBuilder();
        hql.append("from User u where 1=1 ");
        if(!SsmCollections3.isEmpty(inUserIds)){
            hql.append("and (u.id in (:inUserIds) ");
            parameter.put("inUserIds",inUserIds);
            if(SsmStringUtils.isNotBlank(loginNameOrName)){
                hql.append("or u.loginName like :loginName or u.name like :name ) ");
                parameter.put("loginName","%"+loginNameOrName+"%");
                parameter.put("name","%"+loginNameOrName+"%");
            }
        }else{
            if(SsmStringUtils.isNotBlank(loginNameOrName)){
                hql.append("and (u.loginName like :loginName or u.name like :name) ");
                parameter.put("loginName","%"+loginNameOrName+"%");
                parameter.put("name","%"+loginNameOrName+"%");
            }
        }

        hql.append("and u.status = :status ");
        parameter.put("status",SsmStatusState.normal.getValue());
        //设置分页
        SsmPage<SsmUser> p = new SsmPage<SsmUser>(rows);
        p.setPageNo(page);
        p = userDao.findPage(p,hql.toString(),parameter);
        return p;
    }

    /**
     *
     * @param organId 机构ID
     * @param roleId 角色ID
     * @param loginNameOrName 登录名或姓名
     * @param sort
     * @param order
     * @return
     */
    public List<SsmUser> getUsersByOrgOrRole(Long organId, Long roleId, String loginNameOrName, String sort, String order) {
    	SsmParameter parameter = new SsmParameter();
        StringBuilder hql = new StringBuilder();
        hql.append("from User u where u.status = :status ");
        parameter.put("status",SsmStatusState.normal.getValue());
        if (organId != null) {
        	SsmOrgan organ = organManager.loadById(organId);
            hql.append("and :organ in elements(u.organs) ");
            parameter.put("organ",organ);
        }
        if (roleId != null) {
        	SsmRole role = roleManager.loadById(roleId);
            hql.append("and :role in elements(u.roles) ");
            parameter.put("role",role);
        }
        if (SsmStringUtils.isNotBlank(loginNameOrName)) {
            hql.append("and  (u.name like :name or loginName like :loginName) ");
            parameter.put("name","%" + loginNameOrName + "%");
            parameter.put("loginName","%" + loginNameOrName + "%");
        }
        List<SsmUser> users = userDao.find(hql.toString(), parameter);
        return users;
    }

    /**
     * 获取机构用户
     * @param organId
     * @return
     */
    public List<SsmUser> getUsersByOrganId(Long organId) {
        Assert.notNull(organId, "参数[organId]为空!");
        SsmOrgan organ  = organManager.loadById(organId);
        if(organ == null){
            throw new SsmServiceException("机构["+organId+"]不存在.");
        }
        List<SsmUser> users = organ.getUsers();
        return users;
    }

    /**
     * 根据登录名或姓名、密码查找用户.
     * <br/>排除已删除的用户
     * @param loginNameOrName
     *            登录名或姓名
     * @param password
     *            密码
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    @SuppressWarnings("unchecked")
    public SsmUser getUserByLNP(String loginNameOrName, String password)
            throws SsmDaoException,SsmSystemException,SsmServiceException {
        Assert.notNull(loginNameOrName, "参数[loginNameOrName]为空!");
        Assert.notNull(password, "参数[password]为空!");
        SsmParameter parameter = new SsmParameter(loginNameOrName, loginNameOrName, password, SsmStatusState.delete.getValue());
        List<SsmUser> list = getEntityDao().find(
                "from SsmUser u where (u.loginName = :p1 or u.name = :p2) and u.password = :p3 and u.status <> :p4",
                parameter);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 得到排序字段的最大值.
     *
     * @return 返回排序字段的最大值
     */
    public Integer getMaxSort() throws SsmDaoException, SsmSystemException,SsmServiceException {
        Iterator<?> iterator = getEntityDao().createQuery(
                "select max(u.orderNo)from User u ").iterate();
        Integer max = 0;
        while (iterator.hasNext()) {
            // Object[] row = (Object[]) iterator.next();
            max = (Integer) iterator.next();
            if (max == null) {
                max = 0;
            }
        }
        return max;
    }


    /**
     * 排序号交换
     * @param upUserId 需要上位的用户ID
     * @param downUserId 需要下位的用户ID
     * @param moveUp 是否上移 是；true 否（下移）：false
     */
    public void changeOrderNo(Long upUserId, Long downUserId, boolean moveUp) {
        Validate.notNull(upUserId, "参数[upUserId]不能为null!");
        Validate.notNull(downUserId, "参数[downUserId]不能为null!");
        SsmUser upUser = this.loadById(upUserId);
        SsmUser downUser = this.loadById(downUserId);
        if (upUser == null) {
            throw new SsmServiceException("用户[" + upUserId + "]不存在.");
        }
        Integer upUserOrderNo = upUser.getOrderNo();
        Integer downUserOrderNo = downUser.getOrderNo();
        if (upUser.getOrderNo() == null) {
            upUserOrderNo = 1;
        }
        if (downUser == null) {
            throw new SsmServiceException("用户[" + downUserId + "]不存在.");
        }
        if (downUser.getOrderNo() == null) {
            downUserOrderNo = 1;
        }
        if (upUserOrderNo == downUserOrderNo) {
            if (moveUp) {
                upUser.setOrderNo(upUserOrderNo - 1);
            } else {
                downUser.setOrderNo(downUserOrderNo + 1);
            }
        } else {
            upUser.setOrderNo(downUserOrderNo);
            downUser.setOrderNo(upUserOrderNo);
        }

        this.saveOrUpdate(upUser);
        this.saveOrUpdate(downUser);
    }

    public SsmSessionInfo getUser(String loginName){
        Assert.notNull(loginName, "参数[loginName]为空!");
        SsmUser user = findUniqueBy("loginName",loginName);
        if(user == null){
            throw new SsmServiceException("用户["+loginName+"]不存在.");
        }

        return SsmSecurityUtils.userToSessionInfo(user);
    }

    /**
     * 批量更新用户 机构信息
     * @param userIds 用户Id集合
     * @param organIds 所所机构ID集合
     * @param defaultOrganId 默认机构
     */
    public void updateUserOrgan(List<Long> userIds,List<Long> organIds, Long defaultOrganId){
        if(SsmCollections3.isNotEmpty(userIds)){
            for(Long userId:userIds){
            	SsmUser model = this.loadById(userId);
                if(model == null){
                    throw new SsmServiceException("用户["+userId+"]不存在.");
                }
                List<SsmOrgan> oldOrgans = model.getOrgans();
                //绑定组织机构
                model.setOrgans(null);
                List<SsmOrgan> organs = Lists.newArrayList();
                if (SsmCollections3.isNotEmpty(organIds)) {
                    for (Long organId : organIds) {
                    	SsmOrgan organ = organManager.loadById(organId);
                        organs.add(organ);
                        if (SsmCollections3.isNotEmpty(oldOrgans)) {
                            Iterator<SsmOrgan> iterator = oldOrgans.iterator();
                            while (iterator.hasNext()) {
                            	SsmOrgan oldOrgan = iterator.next();
                                if (oldOrgan.getId().equals(organ.getId())) {
                                    iterator.remove();
                                }
                            }

                        }
                    }
                }


                //去除用户已删除机构下的岗位信息
                List<SsmPost> userPosts = model.getPosts();
                if (SsmCollections3.isNotEmpty(oldOrgans)) {//已删除的机构
                    Iterator<SsmOrgan> iterator = oldOrgans.iterator();
                    while (iterator.hasNext()) {
                    	SsmOrgan oldOrgan = iterator.next();
                        List<SsmPost> organPosts = oldOrgan.getPosts();
                        for (SsmPost organPost : organPosts) {
                            if (SsmCollections3.isNotEmpty(userPosts)) {
                                Iterator<SsmPost> iteratorPost = userPosts.iterator();
                                while (iteratorPost.hasNext()) {
                                	SsmPost userPost = iteratorPost.next();
                                    if (userPost.getId().equals(organPost.getId())) {
                                        iteratorPost.remove();
                                    }
                                }
                            }
                        }
                    }

                }


                model.setOrgans(organs);

                //绑定默认组织机构
                model.setDefaultOrgan(null);
                SsmOrgan defaultOrgan = null;
                if (defaultOrganId != null) {
                    defaultOrgan = organManager.loadById(defaultOrganId);
                }
                model.setDefaultOrgan(defaultOrgan);

                this.saveOrUpdate(model);
            }
        }
    }


    /**
     * 设置用户岗位 批量
     * @param userIds 用户ID集合
     * @param roleIds 角色ID集合
     */
    public void updateUserRole(List<Long> userIds,List<Long> roleIds){
        if(SsmCollections3.isNotEmpty(userIds)){
            for(Long userId:userIds){
            	SsmUser model = this.loadById(userId);
                if(model == null){
                    throw new SsmServiceException("用户["+userId+"]不存在.");
                }
                List<SsmRole> rs = Lists.newArrayList();
                if (SsmCollections3.isNotEmpty(roleIds)) {
                    for (Long id : roleIds) {
                    	SsmRole role = roleManager.loadById(id);
                        rs.add(role);
                    }
                }

                model.setRoles(rs);
                this.saveOrUpdate(model);
            }
        }else{
            logger.warn("参数[userIds]为空.");
        }
    }

    /**
     * 设置用户岗位 批量
     * @param userIds 用户ID集合
     * @param postIds 岗位ID集合
     */
    public void updateUserPost(List<Long> userIds,List<Long> postIds) throws SsmServiceException{
        if(SsmCollections3.isNotEmpty(userIds)){
            for(Long userId:userIds){
            	SsmUser model = this.loadById(userId);
                if(model == null){
                    throw new SsmServiceException("用户["+userId+"]不存在.");
                }
                List<SsmPost> ps = Lists.newArrayList();
                if (SsmCollections3.isNotEmpty(postIds)) {
                    for (Long id : postIds) {
                    	SsmPost post = postManager.loadById(id);
                        if(!this.checkPostForUser(model,post)){
                            throw new SsmServiceException("用户["+model.getName()+"]不允许设置为岗位["+post.getName()+"],用户所属机构不存在此岗位.");
                        }
                        ps.add(post);
                    }
                }

                model.setPosts(ps);

                this.saveOrUpdate(model);
            }
        }else{
            logger.warn("参数[userIds]为空.");
        }
    }

    /**
     * 设置用户岗位 批量
     * @param userIds 用户ID集合
     * @param resourceIds 资源ID集合
     */
    public void updateUserResource(List<Long> userIds,List<Long> resourceIds) throws SsmServiceException{
        if(SsmCollections3.isNotEmpty(userIds)){
            for(Long userId:userIds){
            	SsmUser model = this.loadById(userId);
                if(model == null){
                    throw new SsmServiceException("用户["+userId+"]不存在.");
                }
                List<SsmResource> rs = Lists.newArrayList();
                if(SsmCollections3.isNotEmpty(resourceIds)){
                    for (Long id : resourceIds) {
                    	SsmResource resource = resourceManager.loadById(id);
                        rs.add(resource);
                    }
                }

                model.setResources(rs);
                this.saveOrUpdate(model);
            }
        }else{
            logger.warn("参数[userIds]为空.");
        }
    }

    /**
     * 设置用户岗位 批量
     * @param userIds 用户ID集合
     * @param password 密码(md5加密)
     */
    public void updateUserPassword(List<Long> userIds,String password) throws SsmServiceException{
        if(SsmCollections3.isNotEmpty(userIds)){
            for(Long userId:userIds){
            	SsmUser model = this.loadById(userId);
                if(model == null){
                    throw new SsmServiceException("用户["+userId+"]不存在或已被删除.");
                }
                model.setPassword(password);
                this.saveOrUpdate(model);
            }
        }else{
            logger.warn("参数[userIds]为空.");
        }
    }

    public boolean checkPostForUser(SsmUser user,SsmPost post){
        Validate.notNull(user, "参数[user]为空!");
        Validate.notNull(post, "参数[post]为空!");
        boolean flag = false;
        List<Long> userOrganIds = user.getOrganIds();
        if(SsmCollections3.isNotEmpty(userOrganIds) && userOrganIds.contains(post.getOrganId())){
            flag = true;
        }
        return flag;
    }

    /**
     * 锁定用户 批量
     * @param userIds 用户ID集合
     */
    public void lockUsers(List<Long> userIds,int status){
        if(SsmCollections3.isNotEmpty(userIds)){
            for(Long userId:userIds){
            	SsmUser user = this.loadById(userId);
                if(user == null){
                    throw new SsmServiceException("用户["+userId+"]不存在.");
                }
                user.setStatus(status);
                this.saveOrUpdate(user);
            }
        }else{
            logger.warn("参数[userIds]为空.");
        }
    }

    /**
     * 根据ID查找
     * @param userIds 用户ID集合
     * @return
     */
    public List<SsmUser> findUsersByIds(List<Long> userIds) {
        SsmParameter parameter = new SsmParameter(userIds);
        return getEntityDao().find("from User u where u.id in :p1 order by u.orderNo",parameter);
    }
}
