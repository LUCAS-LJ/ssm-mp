/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.ssm.pay.common.model.SsmTreeNode;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.modules.sys._enum.SsmSexType;
import com.ssm.pay.modules.sys.entity.SsmOrgan;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.utils.SsmCacheConstants;

/** 
* @author Jiang.Li
* @version 2016年1月29日 下午2:05:13
*/
@Service
public class SsmOrganManager extends SsmEntityManager<SsmOrgan, Long>{
	@Autowired
    private SsmUserManager userManager;

    private SsmHibernateDao<SsmOrgan, Long> organDao;// 默认的泛型DAO成员变量.

    /**
     * 通过注入的sessionFactory初始化默认的泛型DAO成员变量.
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        organDao = new SsmHibernateDao<SsmOrgan, Long>(sessionFactory, SsmOrgan.class);
    }

    @Override
    protected SsmHibernateDao<SsmOrgan, Long> getEntityDao() {
        return organDao;
    }

    /**
     * 保存或修改.
     */
    @CacheEvict(value = { SsmCacheConstants.ORGAN_USER_TREE_CACHE},allEntries = true)
    public void saveOrUpdate(SsmOrgan entity) throws SsmDaoException, SsmSystemException,SsmServiceException {
        logger.debug("清空缓存:{}");
        Assert.notNull(entity, "参数[entity]为空!");
        organDao.saveOrUpdate(entity);
    }

    /**
     * 保存或修改.
     */
    @CacheEvict(value = { SsmCacheConstants.ORGAN_USER_TREE_CACHE},allEntries = true)
    public void merge(SsmOrgan entity) throws SsmDaoException, SsmSystemException,SsmServiceException {
        Assert.notNull(entity, "参数[entity]为空!");
        organDao.merge(entity);
    }

    @CacheEvict(value = { SsmCacheConstants.ORGAN_USER_TREE_CACHE},allEntries = true)
    @Override
    public void saveEntity(SsmOrgan entity) throws SsmDaoException, SsmSystemException, SsmServiceException {
        super.saveEntity(entity);
    }

    /**
     * 删除(根据主键ID).
     *
     * @param id
     *            主键ID
     */
    @CacheEvict(value = { SsmCacheConstants.ORGAN_USER_TREE_CACHE},allEntries = true)
    public void deleteById(final Long id){
        getEntityDao().delete(id);
    }

    /**
     * 自定义删除方法.
     */
    @CacheEvict(value = { SsmCacheConstants.ORGAN_USER_TREE_CACHE},allEntries = true)
    public void deleteByIds(List<Long> ids) throws SsmDaoException, SsmSystemException,SsmServiceException {
        super.deleteByIds(ids);
    }

    /**
     * Organ转TreeNode
     * @param organ 机构
     * @param organType 机构类型
     * @param isCascade       是否级联
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    private SsmTreeNode organToTreeNode(SsmOrgan organ,Integer organType,boolean isCascade) throws SsmDaoException, SsmSystemException,SsmServiceException {
        if(organType!=null){
            if(!organType.equals(organ.getType())){
                return null;
            }
        }
        SsmTreeNode treeNode = new SsmTreeNode(organ.getId().toString(),
                organ.getName());
        // 自定义属性 url
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("code", organ.getCode());
        attributes.put("sysCode", organ.getSysCode());
        attributes.put("type", organ.getType());
        treeNode.setAttributes(attributes);
        if(isCascade){
            List<SsmTreeNode> childrenTreeNodes = Lists.newArrayList();
            for(SsmOrgan subOrgan:organ.getSubOrgans()){
            	SsmTreeNode node = organToTreeNode(subOrgan,organType,isCascade);
                if(node !=null){
                    childrenTreeNodes.add(node);
                }
            }
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;
    }


    /**
     * @param entity
     * @param excludeOrganId
     * @param isCascade 是否级联
     * @param adduser   是否带用户
     * @return
     * @throws com.ssm.pay.common.exception.DaoException
     * @throws com.ssm.pay.common.exception.SystemException
     * @throws com.ssm.pay.common.exception.ServiceException
     */
    public SsmTreeNode getTreeNode(SsmOrgan entity, Long excludeOrganId, boolean isCascade, boolean adduser,List<Long> checkedUserIds) throws SsmDaoException, SsmSystemException, SsmServiceException {
    	SsmTreeNode node = this.organToTreeNode(entity, null, false);
        List<SsmOrgan> subOrgans = this.getByParentId(entity.getId(), SsmStatusState.normal.getValue());
        if (isCascade) {// 递归查询子节点
            if (SsmCollections3.isNotEmpty(subOrgans)) {
                List<SsmTreeNode> children = Lists.newArrayList();
                if (adduser) {
                    for (SsmUser user : entity.getDefautUsers()) {
                    	SsmTreeNode userNode = new SsmTreeNode();
                        userNode.setId(user.getId().toString());
                        userNode.setText(user.getName());
                        if(SsmSexType.girl.getValue().equals(user.getSex())){
                            userNode.setIconCls("eu-icon-user_red");
                        }else{
                            userNode.setIconCls("eu-icon-user");
                        }
                        if(SsmCollections3.isNotEmpty(checkedUserIds)){
                            if(checkedUserIds.contains(user.getId())){
                                userNode.setChecked(true);
                            }
                        }

                        Map<String, Object> attributes = Maps.newHashMap();
                        attributes.put("nType", "u");
                        userNode.setAttributes(attributes);
                        children.add(userNode);
                    }
                }
                for (SsmOrgan d : subOrgans) {
                    boolean isInclude = true;// 是否包含到节点树
                    SsmTreeNode treeNode = null;
                    // 排除自身
                    if (excludeOrganId != null) {
                        if (!d.getId().equals(excludeOrganId)) {
                            treeNode = getTreeNode(d, excludeOrganId, true, adduser,checkedUserIds);
                        } else {
                            isInclude = false;
                        }
                    } else {
                        treeNode = getTreeNode(d, excludeOrganId, true, adduser,checkedUserIds);
                    }
                    if (isInclude) {
                        Map<String, Object> attributes = Maps.newHashMap();
                        attributes.put("nType", "o");
                        attributes.put("type", d.getType());
                        attributes.put("sysCode", d.getSysCode());
                        treeNode.setAttributes(attributes);
                        treeNode.setIconCls("eu-icon-group");
                        children.add(treeNode);
                        node.setState(SsmTreeNode.STATE_CLOASED);
                    } else {
                        node.setState(SsmTreeNode.STATE_OPEN);
                    }
                }
                node.setChildren(children);
            } else {
                if (adduser) {
                    if (SsmCollections3.isNotEmpty(entity.getDefautUsers())) {
                        List<SsmTreeNode> userTreeNodes = Lists.newArrayList();
                        for (SsmUser user : entity.getDefautUsers()) {
                        	SsmTreeNode userNode = new SsmTreeNode();
                            userNode.setId(user.getId().toString());
                            userNode.setText(user.getName());
                            if(SsmSexType.girl.getValue().equals(user.getSex())){
                                userNode.setIconCls("eu-icon-user_red");
                            }else{
                                userNode.setIconCls("eu-icon-user");
                            }
                            if(SsmCollections3.isNotEmpty(checkedUserIds)){
                                if(checkedUserIds.contains(user.getId())){
                                    userNode.setChecked(true);
                                }
                            }

                            Map<String, Object> attributes = Maps.newHashMap();
                            attributes.put("nType", "u");
                            userNode.setAttributes(attributes);
                            userTreeNodes.add(userNode);
                        }
                        node.setState(SsmTreeNode.STATE_CLOASED);
                        node.setChildren(userTreeNodes);
                    }
                }
            }
        }
        return node;
    }

    /**
     * 用户最大权限机构（管理员 不适用此方法）
     * <br/>机构最短
     * @param userId
     * @return
     */
    private List<SsmOrgan> getUserPermissionOrgan(Long userId){
        Assert.notNull(userId, "参数[userId]为空!");
        SsmUser user = userManager.loadById(userId);
        if(user == null){
            throw new SsmServiceException("用户【"+userId+"】不存在或已被删除.");
        }
        List<SsmOrgan> userOrgans = user.getOrgans();//根据机构系统编码升序排列
        int minOrganLength = 0;
        List<SsmOrgan> minOrgans = Lists.newArrayList();
        if(SsmCollections3.isNotEmpty(userOrgans)){
            Iterator<SsmOrgan> iterator = userOrgans.iterator();
            while(iterator.hasNext()){
            	SsmOrgan organ = iterator.next();
                if(minOrganLength==0){
                    minOrganLength = organ.getSysCode().length();
                    minOrgans.add(organ);
                }else if(organ.getSysCode().length() <= minOrganLength){
                    minOrganLength = organ.getSysCode().length();
                    minOrgans.add(organ);
                }
            }
//            if(Collections3.isEmpty(minOrgans) && user.getDefaultOrgan() != null){
//                minOrgans.add(user.getDefaultOrgan());
//            }
        }
        return minOrgans;
    }

    /**
     * 获取用户机构树
     * @param userId
     * @return
     */
    @Cacheable(SsmCacheConstants.ORGAN_USER_TREE_CACHE)
    public List<SsmTreeNode> getUserOrganTree(Long userId){
        Assert.notNull(userId, "参数[userId]为空!");
        List<SsmTreeNode> treeNodes = Lists.newArrayList();
        SsmUser sessionUser = userManager.loadById(userId);
        SsmUser sueperUser = userManager.getSuperUser();
        if(sessionUser !=null && sueperUser !=null && sessionUser.getId().equals(sueperUser.getId())){
            treeNodes = this.getOrganTree(null,null,true,false,null);
        }else{
            List<SsmOrgan> organs = this.getUserPermissionOrgan(userId);
            for (SsmOrgan rs:organs) {
            	SsmTreeNode rootNode = getTreeNode(rs, null, true,false,null);
                treeNodes.add(rootNode);
            }
        }
        return treeNodes;
    }

    /**
     * 获取所有导航机构.
     *
     * @param parentId       父级ID 为null，则可查询所有节点；不为null，则查询该级以及以下
     * @param excludeOrganId 需要排除的机构ID 子级也会被排除
     * @param isCascade      是否级联
     * @param adduser        是否带用户
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public List<SsmTreeNode> getOrganTree(Long parentId, Long excludeOrganId, boolean isCascade, boolean adduser,List<Long> checkedUserIds) throws SsmDaoException, SsmSystemException,
    					SsmServiceException {
        List<SsmTreeNode> treeNodes = Lists.newArrayList();
        // 顶级机构

        List<SsmOrgan> organs = Lists.newArrayList();
        if (parentId == null) {
            organs = getByParentId(null,
            		SsmStatusState.normal.getValue());
        } else {
        	SsmOrgan parent = this.loadById(parentId);
            organs.add(parent);
        }

        for (SsmOrgan rs : organs) {
        	SsmTreeNode rootNode = getTreeNode(rs, excludeOrganId, isCascade, adduser,checkedUserIds);
            rootNode.setIconCls("icon-organ-root");
            treeNodes.add(rootNode);
        }
        return treeNodes;

    }



    /**
     *
     * 根据name得到Organ.
     *
     * @param name
     *            机构名称
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmOrgan getByName(String name) throws SsmDaoException, SsmSystemException,SsmServiceException {
        if (SsmStringUtils.isBlank(name)) {
            return null;
        }
        name = SsmStringUtils.strip(name);// 去除两边空格
        return organDao.findUniqueBy("name", name);
    }
    /**
     *
     * 根据系统编码得到Organ.
     *
     * @param sysCode
     *            机构系统编码
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmOrgan getBySysCode(String sysCode) throws SsmDaoException, SsmSystemException,SsmServiceException {
        if (SsmStringUtils.isBlank(sysCode)) {
            return null;
        }
        return organDao.findUniqueBy("sysCode", sysCode);
    }

    /**
     *
     * 根据编码得到Organ.
     *
     * @param code
     *            机构编码
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmOrgan getByCode(String code) throws SsmDaoException, SsmSystemException,SsmServiceException {
        if (SsmStringUtils.isBlank(code)) {
            return null;
        }
        return organDao.findUniqueBy("code", code);
    }

    /**
     *
     * 根据父ID得到 Organ. <br>
     * 默认按 orderNo asc,id asc排序.
     *
     * @param parentId
     *            父节点ID(当该参数为null的时候查询顶级机构列表)
     * @param status
     *            数据状态 @see com.ssm.pay.common.orm.entity.StatusState
     *            <br>status传null则使用默认值 默认值:StatusState.normal.getValue()
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    @SuppressWarnings("unchecked")
    public List<SsmOrgan> getByParentId(Long parentId, Integer status)
            throws SsmDaoException, SsmSystemException, SsmServiceException {
        //默认值 正常
        if(status == null){
            status = SsmStatusState.normal.getValue();
        }
        StringBuilder sb = new StringBuilder();
        SsmParameter parameter = new SsmParameter();
        sb.append("from Organ o where o.status = :status  ");
        parameter.put("status",status);
        sb.append(" and o.parentOrgan");
        if (parentId == null) {
            sb.append(" is null ");
        } else {
            sb.append(".id = :parentId ");
            parameter.put("parentId",parentId);
        }
        sb.append(" order by o.orderNo asc,o.id asc");

        List<SsmOrgan> list = organDao.find(sb.toString(), parameter);
        return list;
    }


    /**
     * 得到排序字段的最大值.
     *
     * @return 返回排序字段的最大值
     */
    public Integer getMaxSort() throws SsmDaoException, SsmSystemException,SsmServiceException {
        Iterator<?> iterator = organDao.createQuery(
                "select max(o.orderNo)from Organ o ").iterate();
        Integer max = 0;
        while (iterator.hasNext()) {
            max = (Integer) iterator.next();
            if (max == null) {
                max = 0;
            }
        }
        return max;
    }

    /**U
     * 查询指定机构以及子机构
     * @param sysCode 机构系统编码
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public List<SsmOrgan> findOrgansBySysCode(String sysCode) throws SsmDaoException, SsmSystemException,SsmServiceException{
    	SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue(),sysCode+"%");
        StringBuilder hql = new StringBuilder();
        hql.append("from Organ o where o.status = :p1  and o.sysCode like  :p2 order by o.sysCode asc");
        List<SsmOrgan> list = organDao.find(hql.toString(), parameter);
        return list;
    }

    /**
     * 根据ID查找
     * @param organIds 机构ID集合
     * @return
     */
    public List<SsmOrgan> findOrgansByIds(List<Long> organIds) {
    	SsmParameter parameter = new SsmParameter(organIds);
        return getEntityDao().find("from Organ o where o.id in :p1",parameter);
    }
}
