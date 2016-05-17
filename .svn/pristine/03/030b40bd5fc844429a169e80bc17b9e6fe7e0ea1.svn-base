/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.service;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ssm.pay.common.exception.SsmDaoException;
import com.ssm.pay.common.exception.SsmServiceException;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.modules.sys.entity.SsmRole;
import com.ssm.pay.utils.SsmCacheConstants;

/** 角色Role管理 Service层实现类.
* @author Jiang.Li
* @version 2016年1月29日 下午2:13:27
*/
@Service
public class SsmRoleManager extends SsmEntityManager<SsmRole, Long>{
	private SsmHibernateDao<SsmRole, Long> roleDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		roleDao = new SsmHibernateDao<SsmRole, Long>(sessionFactory,
				SsmRole.class);
	}
	
	@Override
	protected SsmHibernateDao<SsmRole, Long> getEntityDao() {
		return roleDao;
	}
	
	/**
	 * 删除角色.
	 * <br>删除角色的时候 会给角色重新授权菜单 更新导航菜单缓存.
	 */
    @CacheEvict(value = {  SsmCacheConstants.ROLE_ALL_CACHE,
    		SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
    		SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE,
    		SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE},allEntries = true)
	@Override
	public void deleteByIds(List<Long> ids) throws SsmDaoException,SsmSystemException, SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.ROLE_ALL_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE);
        super.deleteByIds(ids);
	}
	/**
	 * 新增或修改角色.
	 * <br>修改角色的时候 会给角色重新授权菜单 更新导航菜单缓存.
	 */
    @CacheEvict(value = {  SsmCacheConstants.ROLE_ALL_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE},allEntries = true)
    public void saveOrUpdate(SsmRole entity) throws SsmDaoException,SsmSystemException,SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.ROLE_ALL_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE);
		Assert.notNull(entity, "参数[entity]为空!");
		roleDao.saveOrUpdate(entity);
		logger.warn("保存色Role:{}",entity.getId());
	}
	
	/**
	 * 新增或修改角色.
	 * <br>修改角色的时候 会给角色重新授权菜单 更新导航菜单缓存.
	 */
    @CacheEvict(value = {  SsmCacheConstants.ROLE_ALL_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE},allEntries = true)
    public void merge(SsmRole entity) throws SsmDaoException,SsmSystemException,SsmServiceException {
		Assert.notNull(entity, "参数[entity]为空!");
		roleDao.merge(entity);
		logger.warn("保存色Role:{}",entity.getId());
	}

    /**
     * 新增或修改角色.
     * @param entity
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    @CacheEvict(value = {  SsmCacheConstants.ROLE_ALL_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE},allEntries = true)
    @Override
    public void saveEntity(SsmRole entity) throws SsmDaoException, SsmSystemException, SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.ROLE_ALL_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE);
        super.saveEntity(entity);
    }

    /**
     * 根据角色编码查找
     * @param code 角色编
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmRole getByCode(String code) throws SsmDaoException,SsmSystemException,SsmServiceException {
        return this.findUniqueBy("code",code);
    }

    /**
     * 查找所有
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    @Cacheable(value = { SsmCacheConstants.ROLE_ALL_CACHE})
    public List<SsmRole> getAll() throws SsmDaoException,SsmSystemException,SsmServiceException {
        List<SsmRole> list = super.getAll();
		logger.debug("缓存:{}",SsmCacheConstants.ROLE_ALL_CACHE);
		return list;
	}


    /**
     * 根据ID查找
     * @param roleIds 角色ID集合
     * @return
     */
    public List<SsmRole> findRolesByIds(List<Long> roleIds) {
    	SsmParameter parameter = new SsmParameter(roleIds);
        return getEntityDao().find("from Role r where r.id in :p1",parameter);
    }
}
