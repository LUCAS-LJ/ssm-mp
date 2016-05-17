/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.ListUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ssm.pay.common.exception.SsmDaoException;
import com.ssm.pay.common.exception.SsmServiceException;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.model.SsmMenu;
import com.ssm.pay.common.model.SsmTreeNode;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.modules.sys._enum.SsmResourceType;
import com.ssm.pay.modules.sys.entity.SsmResource;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.utils.SsmCacheConstants;

/** 资源Resource管理 Service层实现类.
 * <br>树形资源使用缓存 当保存、删除操作时清除缓存
* @author Jiang.Li
* @version 2016年1月29日 下午1:09:25
*/
@Service
public class SsmResourceManager extends SsmEntityManager<SsmResource, Long>{
	@Autowired
	private SsmUserManager userManager;

	private SsmHibernateDao<SsmResource, Long> resourceDao;// 默认的泛型DAO成员变量.

	/**
	 * 通过注入的sessionFactory初始化默认的泛型DAO成员变量.
	 */
	@Autowired
	public void setSessionFactory(final SessionFactory sessionFactory) {
		resourceDao = new SsmHibernateDao<SsmResource, Long>(sessionFactory, SsmResource.class);
	}

	@Override
	protected SsmHibernateDao<SsmResource, Long> getEntityDao() {
		return resourceDao;
	}

	/**
	 * 保存或修改.
	 */
	//清除缓存
	@CacheEvict(value = { SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE,
			SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE},allEntries = true)
	public void saveOrUpdate(SsmResource entity) throws SsmDaoException, SsmSystemException,
				SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE);
		Assert.notNull(entity, "参数[entity]为空!");
		resourceDao.saveOrUpdate(entity);
	}

	/**
	 * 保存或修改.
	 */
	//清除缓存
    @CacheEvict(value = { SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE,
    		SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE},allEntries = true)
	public void merge(SsmResource entity) throws SsmDaoException, SsmSystemException,
					SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE);
		Assert.notNull(entity, "参数[entity]为空!");
		resourceDao.merge(entity);
	}

    @CacheEvict(value = { SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE},allEntries = true)
    @Override
    public void saveEntity(SsmResource entity) throws SsmDaoException, SsmSystemException, SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE);
        super.saveEntity(entity);
    }

    /**
     * 自定义保存资源.
     * <br/>说明：如果保存的资源类型为“功能” 则将所有子资源都设置为“功能”类型
     * @param entity 资源对象
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    @CacheEvict(value = { SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE},allEntries = true)
    public void saveResource(SsmResource entity) throws SsmDaoException, SsmSystemException, SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE);
        Assert.notNull(entity,"参数[entity]为空!");
        this.saveEntity(entity);
        if(entity.getType() !=null && SsmResourceType.function.getValue().equals(entity.getType())){
            List<SsmResource> subResources = entity.getSubResources();
            while (!SsmCollections3.isEmpty(subResources)){
                Iterator<SsmResource> iterator = subResources.iterator();
                while(iterator.hasNext()){
                	SsmResource subResource = iterator.next();
                     subResource.setType(SsmResourceType.function.getValue());
                     iterator.remove();
                     subResources = ListUtils.union(subResources,subResource.getSubResources());
                     super.update(subResource);
                 }
            }
        }
    }


    /**
	 * 自定义删除方法.
	 */
	//清除缓存
    @CacheEvict(value = { SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE,
            SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE,
            SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE},allEntries = true)
	public void deleteByIds(List<Long> ids) throws SsmDaoException, SsmSystemException,
		SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE
                +","+SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE);
        if(!SsmCollections3.isEmpty(ids)){
            for(Long id :ids){
            	SsmResource resource = getEntityDao().load(id);
                resource.setRoles(null);
                resource.setUsers(null);
                getEntityDao().delete(resource);
            }
        }else{
            logger.warn("参数[ids]为空.");
        }

	}

    /**
     * 根据资源编码获取对象
     * @param resourceCode 资源编码
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmResource getResourceByCode(String resourceCode) throws SsmDaoException, SsmSystemException,
    		SsmServiceException {
        return getEntityDao().findUniqueBy("code",resourceCode);
    }

    /**
     * 检查用户是否具有某个资源编码的权限
     * @param userId 用户ID
     * @param resourceCode 资源编码
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public boolean isUserPermittedResourceCode(Long userId, String resourceCode) throws SsmDaoException, SsmSystemException,
    				SsmServiceException {
        Assert.notNull(userId, "参数[userId]为空!");
        Assert.notNull(resourceCode, "参数[resourceCode]为空!");
        List<SsmResource> list = this.getResourcesByUserId(userId);
        boolean flag = false;
        for (SsmResource resource : list) {
            if (resource != null && SsmStringUtils.isNotBlank(resource.getCode()) && resource.getCode().equalsIgnoreCase(resourceCode)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 用户导航菜单(排除非菜单资源).
     * @param userId 用户ID
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    @Cacheable(value = { SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE},key = "#userId +'getNavMenuTreeByUserId'")
    public List<SsmTreeNode> getNavMenuTreeByUserId(Long userId) throws SsmDaoException,
    				SsmSystemException, SsmServiceException {
        List<SsmTreeNode> nodes = Lists.newArrayList();
        List<SsmResource> userResources = Lists.newArrayList();
        SsmUser user = userManager.loadById(userId);
        SsmUser superUser = userManager.getSuperUser();
        boolean isSuperUser = false; //是否是超级管理员
        if (user != null && superUser != null
                && user.getId().equals(superUser.getId())) {// 超级用户
            isSuperUser = true;
            userResources = this.getByParentId(null,SsmStatusState.normal.getValue());
        } else if (user != null) {
           userResources = this.getResourcesByUserId(userId);
        }
        for(SsmResource resource:userResources){
            if(isSuperUser){
                SsmTreeNode node =  this.resourceToTreeNode(resource, SsmResourceType.menu.getValue(),true);
                if(node !=null){
                    nodes.add(node);
                }
            } else{
                if(resource != null && resource.getParentResource() == null){
                    SsmTreeNode node =  this.resourceToTreeNode(userResources,resource, SsmResourceType.menu.getValue(),true);
                    if(node !=null){
                        nodes.add(node);
                    }
                }
            }
        }

        logger.debug("缓存:{}", SsmCacheConstants.RESOURCE_USER_MENU_TREE_CACHE +" 参数：userId="+userId);
        return nodes;
    }

    public List<SsmResource> getResourcesByUserId(Long userId) throws SsmDaoException,
    				SsmSystemException, SsmServiceException {
        Assert.notNull(userId, "userId不能为空");
        SsmParameter parameter = new SsmParameter(userId,SsmStatusState.normal.getValue());
        //角色权限
        List<SsmResource> roleResources = resourceDao.distinct(resourceDao.createQuery("select ms from User u left join u.roles rs left join rs.resources ms where u.id= :p1 and ms.status = :p2 order by ms.orderNo asc", parameter)).list();
        //用户直接权限
        SsmUser user = userManager.loadById(userId);
        List<SsmResource> userResources = user.getResources();
        //去除空对象
        Iterator<SsmResource> roleIterator  = roleResources.iterator();
        while (roleIterator.hasNext()){
        	SsmResource roleResource = roleIterator.next();
            if(roleResource == null){
                roleIterator.remove();
            }
        }
        List<SsmResource> rs = SsmCollections3.aggregate(roleResources,userResources);
        Collections.sort(rs, new Comparator<SsmResource>() {
//            @Override
            public int compare(SsmResource o1, SsmResource o2) {
                if (o1.getOrderNo() != null && o2.getOrderNo() != null) {
                    return o1.getOrderNo().compareTo(o2.getOrderNo());
                }
                return 0;
            }
        });
        return rs;
    }

    public List<SsmResource> getResourcesByUserId(Long userId, SsmResource parentResource) throws SsmDaoException,
    			SsmSystemException, SsmServiceException {
        List<SsmResource> list = new ArrayList<SsmResource>();
        List<SsmResource> resources =   this.getResourcesByUserId(userId);
        if (null == parentResource){
               for(SsmResource resource:resources){
                   if(resource != null && resource.getParentResource() == null
                           && SsmStatusState.normal.getValue().equals(resource.getStatus())){
                       list.add(resource);
                   }
               }
        }else{
            for(SsmResource resource:resources){
                if(resource != null && resource.getParentResource() != null && resource.getParentResource().getId().equals(parentResource.getId())
                        && SsmStatusState.normal.getValue().equals(resource.getStatus())){
                    list.add(resource);
                }
            }
        }
        return list;
    }

    /**
     * 根据用户ID得到导航栏资源（权限控制）.
     * @param userId 用户ID
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    //使用缓存
   @Cacheable(value = { SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE},key = "#userId +'getResourceTreeByUserId'")
    public List<SsmTreeNode> getResourceTreeByUserId(Long userId) throws SsmDaoException,
    				SsmSystemException, SsmServiceException {
        // Assert.notNull(userId, "参数[userId]为空!");
        List<SsmTreeNode> nodes = Lists.newArrayList();
        List<SsmResource> userResources = Lists.newArrayList();
        SsmUser user = userManager.loadById(userId);
        SsmUser superUser = userManager.getSuperUser();
        boolean isSuperUser = false; //是否是超级管理员
        if (user != null && superUser != null
                && user.getId().equals(superUser.getId())) {// 超级用户
            userResources = this.getByParentId(null,SsmStatusState.normal.getValue());
        } else if (user != null) {
            userResources = this.getResourcesByUserId(userId);
        }
        for(SsmResource resource:userResources){
            if(isSuperUser){
                SsmTreeNode node =  this.resourceToTreeNode(resource, null,true);
                if(node !=null){
                    nodes.add(node);
                }
            } else{
                SsmTreeNode node =  this.resourceToTreeNode(userResources,resource, null,true);
                if(node !=null){
                    nodes.add(node);
                }
            }

        }

        logger.debug("缓存:{}", SsmCacheConstants.RESOURCE_USER_RESOURCE_TREE_CACHE + " 参数：userId=" + userId);
        return nodes;
    }

    /**
     * Resource转TreeNode
     * @param resource 资源
     * @param resourceType 资源类型
     * @param isCascade       是否级联
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    private SsmTreeNode resourceToTreeNode(SsmResource resource,Integer resourceType,boolean isCascade) throws SsmDaoException, SsmSystemException,
    			SsmServiceException {
        if(resourceType!=null){
            if(!resourceType.equals(resource.getType())){
                return null;
            }
        }
        SsmTreeNode treeNode = new SsmTreeNode(resource.getId().toString(),
                resource.getName(), resource.getIconCls());
        // 自定义属性 url
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("url", resource.getUrl());
        attributes.put("markUrl", resource.getMarkUrl());
        attributes.put("code", resource.getCode());
        attributes.put("type", resource.getType());
        treeNode.setAttributes(attributes);
        if(isCascade){
            List<SsmTreeNode> childrenTreeNodes = Lists.newArrayList();
            for(SsmResource subResource:resource.getSubResources()){
                SsmTreeNode node = resourceToTreeNode(subResource,resourceType,isCascade);
                if(node !=null){
                    childrenTreeNodes.add(node);
                }
            }
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;
    }

    /**
     * Resource转TreeNode
     * @param repositoryResources 资源库
     * @param resource 资源
     * @param resourceType 资源类型
     * @param isCascade       是否级联
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    private SsmTreeNode resourceToTreeNode(List<SsmResource> repositoryResources,SsmResource resource,Integer resourceType,boolean isCascade) throws SsmDaoException, SsmSystemException,
    			SsmServiceException {
        if(resource==null || !repositoryResources.contains(resource)){
            return null;
        }
        if(resourceType!=null){
            if(!resourceType.equals(resource.getType())){
                return null;
            }
        }
        SsmTreeNode treeNode = new SsmTreeNode(resource.getId().toString(),
                resource.getName(), resource.getIconCls());
        // 自定义属性 url
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("url", resource.getUrl());
        attributes.put("markUrl", resource.getMarkUrl());
        attributes.put("code", resource.getCode());
        attributes.put("type", resource.getType());
        treeNode.setAttributes(attributes);
        if(isCascade){
            List<SsmTreeNode> childrenTreeNodes = Lists.newArrayList();
            for(SsmResource subResource:resource.getSubResources()){
                SsmTreeNode node = resourceToTreeNode(repositoryResources,subResource,resourceType,isCascade);
                if(node !=null){
                    childrenTreeNodes.add(node);
                }
            }
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;
    }


    /**
     * Resource转Easy UI Menu
     * @param resource
     * @param isCascade 是否递归遍历子节点
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmMenu resourceToMenu(SsmResource resource,boolean isCascade) throws SsmDaoException, SsmSystemException,
    				SsmServiceException {
        Assert.notNull(resource,"参数resource不能为空");
        if(SsmResourceType.menu.getValue().equals(resource.getType())){
            SsmMenu menu = new SsmMenu();
            menu.setId(resource.getId().toString());
            menu.setText(resource.getName());
            menu.setHref(resource.getUrl());
            if(isCascade){
                List<SsmMenu> childrenMenus = Lists.newArrayList();
                for(SsmResource subResource:resource.getSubResources()){
                    if(SsmResourceType.menu.getValue().equals(subResource.getType())){
                        childrenMenus.add(resourceToMenu(subResource,true));
                    }
                }
                menu.setChildren(childrenMenus);
            }
            return menu;
        }
        return null;
    }


    public List<SsmMenu> getAppMenusByUserId(Long userId){
        List<SsmMenu> menus = Lists.newArrayList();
        List<SsmResource> resources = Lists.newArrayList();
        SsmUser user = userManager.loadById(userId);
        SsmUser superUser = userManager.getSuperUser();
        if (user != null && superUser != null
                && user.getId().equals(superUser.getId())) {// 超级用户
            resources = super.getAll();
        } else if (user != null) {
            resources = getResourcesByUserId(userId);
        }
        for(SsmResource resource:resources){
            if(SsmStringUtils.isNotBlank(resource.getUrl())){
                if(SsmResourceType.menu.getValue().equals(resource.getType())) {
                    SsmMenu menu = new SsmMenu();
                    menu.setId(resource.getId().toString());
                    menu.setText(resource.getName());
                    menu.setHref(resource.getUrl());
                    menus.add(menu);
                }
            }

        }
        return menus;
    }
    /**
     * 得到开始菜单.
     * @param userId 用户ID
     */
    public List<SsmMenu> getMenusByUserId(Long userId){
        List<SsmMenu> menus = Lists.newArrayList();
        List<SsmResource> rootResources = Lists.newArrayList();
        SsmUser user = userManager.loadById(userId);
        SsmUser superUser = userManager.getSuperUser();
        if (user != null && superUser != null
                && user.getId().equals(superUser.getId())) {// 超级用户
           rootResources = getByParentId(null,SsmStatusState.normal.getValue());
        } else if (user != null) {
            rootResources = getResourcesByUserId(userId,null);
            //去除非菜单资源
            Iterator<SsmResource> iterator = rootResources.iterator();
            while (iterator.hasNext()){
                if(!SsmResourceType.menu.getValue().equals(iterator.next().getType())) {
                    iterator.remove();
                }
            }
        }
        for(SsmResource parentResource:rootResources){
            SsmMenu menu = resourceToMenu(parentResource,true);
            if(menu!=null){
                menus.add(menu);
            }
        }
        return menus;
    }

    /**
     *
     * @param entity
     * @param excludeReourceId
     * @param isCascade 是否级联
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmTreeNode getTreeNode(SsmResource entity, Long excludeReourceId, boolean isCascade)
            throws SsmDaoException, SsmSystemException, SsmServiceException {
        SsmTreeNode node = this.resourceToTreeNode(entity,null,false);
        List<SsmResource> subResources = this.getByParentId(entity.getId(),SsmStatusState.normal.getValue());
        if (isCascade) {// 递归查询子节点
            List<SsmTreeNode> children = Lists.newArrayList();
            for (SsmResource d : subResources) {
                boolean isInclude = true;// 是否包含到节点树
                SsmTreeNode treeNode = null;
                // 排除自身
                if (excludeReourceId != null) {
                    if (!d.getId().equals(excludeReourceId)) {
                        treeNode = getTreeNode(d, excludeReourceId, true);
                    } else {
                        isInclude = false;
                    }
                } else {
                    treeNode = getTreeNode(d, excludeReourceId, true);
                }
                if (isInclude) {
                    children.add(treeNode);
                    node.setState(SsmTreeNode.STATE_CLOASED);
                } else {
                    node.setState(SsmTreeNode.STATE_OPEN);
                }
            }

            node.setChildren(children);
        }
        return node;
    }

    /**
     * 获取所有导航资源（无权限限制）.
     * @param excludeResourceId 需要排除的资源ID 子级也会被排除
     * @param isCascade 是否级联
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public List<SsmTreeNode> getResourceTree(Long excludeResourceId,boolean isCascade) throws SsmDaoException, SsmSystemException,
    		SsmServiceException {
        List<SsmTreeNode> treeNodes = Lists.newArrayList();
        // 顶级资源
        List<SsmResource> resources = getByParentId(null,
                SsmStatusState.normal.getValue());
        for (SsmResource rs:resources) {
            SsmTreeNode rootNode = getTreeNode(rs, excludeResourceId, isCascade);
            treeNodes.add(rootNode);
        }
        return treeNodes;

    }



    /**
	 *
	 * 根据name得到Resource.
	 *
	 * @param name
	 *            资源名称
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public SsmResource getByName(String name) throws SsmDaoException, SsmSystemException,
			SsmServiceException {
		if (SsmStringUtils.isBlank(name)) {
			return null;
		}
		name = SsmStringUtils.strip(name);// 去除两边空格
		return resourceDao.findUniqueBy("name", name);
	}


    /**
     *
     * 根据编码得到Resource.
     *
     * @param code
     *            资源编码
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmResource getByCode(String code) throws SsmDaoException, SsmSystemException,SsmServiceException {
        if (SsmStringUtils.isBlank(code)) {
            return null;
        }
        return resourceDao.findUniqueBy("code", code);
    }

	/**
	 *
	 * 根据父ID得到 Resource. <br>
	 * 默认按 orderNo asc,id asc排序.
	 *
	 * @param parentId
	 *            父节点ID(当该参数为null的时候查询顶级资源列表)
	 * @param status
	 *            数据状态 @see com.ssm.pay.common.orm.entity.StatusState
	 *            <br>status传null则使用默认值 默认值:StatusState.normal.getValue()
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SsmResource> getByParentId(Long parentId, Integer status)
			throws SsmDaoException, SsmSystemException, SsmServiceException {
		//默认值 正常
		if(status == null){
			status = SsmStatusState.normal.getValue();
		}
		StringBuilder hql = new StringBuilder();
		SsmParameter parameter = new SsmParameter();
		hql.append("from SsmResource r where r.status = :status  ");
        parameter.put("status",status);
        hql.append(" and r.parentResource");
        if (parentId == null) {
			hql.append(" is null ");
		} else {
			hql.append(".id = :parentId ");
            parameter.put("parentId",parentId);
		}
		hql.append(" order by r.orderNo asc,r.id asc");

		List<SsmResource> list = getEntityDao().find(hql.toString(), parameter);
		return list;
	}



    /**
     * 根据请求地址判断用户是否有权访问该url
     * @param requestUrl 请求URL地址
     * @param userId 用户ID
     * @return
     */
    @Cacheable(value = {SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE},key = "#requestUrl + #userId +'isAuthority'")
    public boolean isAuthority(String requestUrl,Long userId)
            throws SsmDaoException,SsmSystemException,SsmServiceException{
        //如果是超级管理员 直接允许被授权
        if(userManager.getSuperUser().getId().equals(userId)) {
            return true;
        }
        //检查该URL是否需要拦截
        boolean isInterceptorUrl = this.isInterceptorUrl(requestUrl);
        if (isInterceptorUrl){
            //用户权限Lo
            List<String> userAuthoritys = this.getUserAuthoritysByUserId(userId);
            for(String markUrl :userAuthoritys){
                String[] markUrls = markUrl.split(";");
                for(int i=0;i<markUrls.length;i++){
                    if(SsmStringUtils.isNotBlank(markUrls[i]) && SsmStringUtils.simpleWildcardMatch(markUrls[i],requestUrl)){
                        return true;
                    }
                }
            }
            return false;
        }
        logger.debug("缓存:{}", SsmCacheConstants.RESOURCE_USER_AUTHORITY_URLS_CACHE +"参数：requestUrl="+requestUrl+",userId="+userId);
        return true;
    }


    /**
     * 查找需要拦截的所有url规则
     * @return
     */
    public List<String> getAllInterceptorUrls()
            throws SsmDaoException,SsmSystemException,SsmServiceException{
        List<String> markUrls = Lists.newArrayList();
        //查找所有资源
//        List<Resource> resources = this.findBy("NEI_status",StatusState.delete.getValue());
        List<SsmResource> resources = this.getAll();
        for(SsmResource resource : resources){
            if(SsmStringUtils.isNotBlank(resource.getMarkUrl())){
                markUrls.add(resource.getMarkUrl());
            }
        }
        return markUrls;
    }

    /**
     * 检查某个URL是都需要拦截
     * @param requestUrl 检查的URL地址
     * @return
     */
    public boolean isInterceptorUrl(String requestUrl)
            throws SsmDaoException,SsmSystemException,SsmServiceException{
        List<String> markUrlList = this.getAllInterceptorUrls();
        for(String markUrl :markUrlList){
            String[] markUrls = markUrl.split(";");
            for(int i=0;i<markUrls.length;i++){
                if(SsmStringUtils.isNotBlank(markUrls[i]) && SsmStringUtils.simpleWildcardMatch(markUrls[i],requestUrl)){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 根据用户ID查找用户拥有的URL权限
     * @param userId   用户ID
     * @return    List<String> 用户拥有的markUrl地址
     */
    public List<String> getUserAuthoritysByUserId(Long userId)
            throws SsmDaoException,SsmSystemException,SsmServiceException{
        List<String> userAuthoritys = Lists.newArrayList();
        List<SsmTreeNode> treeNodes = this.getResourceTreeByUserId(userId);
        for(SsmTreeNode node : treeNodes){
            Object obj = node.getAttributes().get("markUrl");
            if(obj != null){
                String markUrl = (String)obj ;
                if(SsmStringUtils.isNotBlank(markUrl)){
                    userAuthoritys.add(markUrl);
                }
            }
            //二级目录
            List<SsmTreeNode> childrenNodes =  node.getChildren();
            for(SsmTreeNode childrenNode : childrenNodes){
                Object childrenObj = childrenNode.getAttributes().get("markUrl");
                if(childrenObj != null){
                    String markUrl = (String)childrenObj ;
                    if(SsmStringUtils.isNotBlank(markUrl)){
                        userAuthoritys.add(markUrl);
                    }
                }
            }
        }
        return  userAuthoritys;
    }


    /**
	 * 得到排序字段的最大值.
	 * 
	 * @return 返回排序字段的最大值
	 */
	public Integer getMaxSort() throws SsmDaoException, SsmSystemException,
					SsmServiceException {
		Iterator<?> iterator = resourceDao.createQuery(
				"select max(m.orderNo)from Resource m ").iterate();
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
}
