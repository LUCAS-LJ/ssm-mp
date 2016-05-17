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
import com.ssm.pay.common.model.SsmCombobox;
import com.ssm.pay.common.model.SsmTreeNode;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.modules.sys.entity.SsmDictionary;
import com.ssm.pay.modules.sys.entity.SsmDictionaryType;
import com.ssm.pay.utils.SsmCacheConstants;

/** 数据字典实现类
* @author Jiang.Li
* @version 2016年4月11日 下午7:35:36
*/
@Service
public class SsmDictionaryManager extends SsmEntityManager<SsmDictionary, Long>{
	private SsmHibernateDao<SsmDictionary, Long> dictionaryDao;
    @Autowired
    private SsmDictionaryTypeManager dictionaryTypeManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dictionaryDao = new SsmHibernateDao<SsmDictionary, Long>(sessionFactory,
				SsmDictionary.class);
	}

	@Override
	protected SsmHibernateDao<SsmDictionary, Long> getEntityDao() {
		return dictionaryDao;
	}

	/**
	 * 新增或修改 保存对象.
	 */
    @CacheEvict(value = { SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE,
    		SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE,
    		SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE},allEntries = true)
	public void saveOrUpdate(SsmDictionary entity) throws SsmDaoException, SsmSystemException,
			SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE
                +","+SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE
                +","+SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE);
        Assert.notNull(entity, "参数[entity]为空!");
		dictionaryDao.saveOrUpdate(entity);
	}
	
	/**
	 * 新增或修改 保存对象.
	 */
    @CacheEvict(value = { SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE,
    		SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE,
    		SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE},allEntries = true)
	public void merge(SsmDictionary entity) throws SsmDaoException, SsmSystemException,
				SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE
                +","+SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE
                +","+SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE);
        Assert.notNull(entity, "参数[entity]为空!");
		dictionaryDao.merge(entity);
	}

    @CacheEvict(value = { SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE,
    		SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE,
    		SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE},allEntries = true)
    @Override
    public void saveEntity(SsmDictionary entity) throws SsmDaoException, SsmSystemException, SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE
                +","+SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE
                +","+SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE);
        super.saveEntity(entity);
    }

    @CacheEvict(value = { SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE,
    		SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE,
    		SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE},allEntries = true)
    @Override
    public void deleteByIds(List<Long> ids) throws SsmDaoException, SsmSystemException, SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE
                +","+SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE
                +","+SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE);
        super.deleteByIds(ids);
    }

	/**
	 * 根据编码code得到对象.
	 * 
	 * @param code
	 *            数据字典编码
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public SsmDictionary getByCode(String code) throws SsmDaoException,
		SsmSystemException, SsmServiceException {
		if (SsmStringUtils.isBlank(code)) {
			return null;
		}
		code = SsmStringUtils.strip(code);// 去除两边空格
		SsmParameter parameter = new SsmParameter(code);
		List<SsmDictionary> list = getEntityDao().find(
				"from Dictionary d where d.code = :p1",parameter);
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * 根据数据字典类型编码dictionaryTypeCode得到List<TreeNode>对象. <br>
	 * 当id不为空的时候根据id排除自身节点.
	 *
	 * @param excludeDictionaryId
	 *            需要排除数据字典ID 下级也会被排除
	 * @param isCascade
	 *            是否级联加载
	 * @return List<TreeNode> 映射关系： TreeNode.text-->Dicitonary.text;TreeNode.id-->Dicitonary.code;
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
    @Cacheable(value = { SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE} )
	@SuppressWarnings("unchecked")
	public List<SsmTreeNode> getByDictionaryTypeCode(String dictionaryTypeCode, Long excludeDictionaryId, boolean isCascade)
			throws SsmDaoException, SsmSystemException, SsmServiceException {
        logger.debug("缓存:{}", SsmCacheConstants.DICTIONARYS_CONBOTREE_BY_TYPE_CACHE);
        List<SsmDictionary> list = null;
		List<SsmTreeNode> treeNodes = Lists.newArrayList();
		if (SsmStringUtils.isBlank(dictionaryTypeCode)) {
			return treeNodes;
		}
		StringBuilder hql = new StringBuilder();
		SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue(),dictionaryTypeCode);
		hql.append("from Dictionary d where d.status = :p1 and d.dictionaryType.code = :p2  and d.parentDictionary is null order by d.orderNo");
		logger.debug(hql.toString());
		list = getEntityDao().find(hql.toString(), parameter);
        for (SsmDictionary d : list) {
        	SsmTreeNode t = getTreeNode(d, excludeDictionaryId, isCascade);
            if (t != null) {
                treeNodes.add(t);
            }

		}
		return treeNodes;
	}

	/**
	 * /** 根据数据字典类型编码dictionaryTypeCode得到List<TreeNode>对象. <br>
	 * 当id不为空的时候根据id排除自身节点.
	 *
	 * @param entity
	 *            数据字典对象
	 * @param excludeDictionaryId
	 *            数据字ID
	 * @param isCascade
	 *            是否级联加载
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public SsmTreeNode getTreeNode(SsmDictionary entity, Long excludeDictionaryId, boolean isCascade)
			throws SsmDaoException, SsmSystemException, SsmServiceException {
		SsmTreeNode node = new SsmTreeNode(entity.getCode(), entity.getName());
//        node.getAttributes().put("code",entity.getCode());
		// Map<String, Object> attributes = new HashMap<String, Object>();
		// node.setAttributes(attributes);
		List<SsmDictionary> subDictionaries = getByParentCode(entity.getCode());
        if (isCascade) {// 递归查询子节点
            List<SsmTreeNode> children = Lists.newArrayList();
            for (SsmDictionary d : subDictionaries) {
                boolean isInclude = true;// 是否包含到节点树
                SsmTreeNode treeNode = null;
                // 排除自身
                if (excludeDictionaryId != null) {
                    if (!d.getId().equals(excludeDictionaryId)) {
                        treeNode = getTreeNode(d, excludeDictionaryId, true);
                    } else {
                        isInclude = false;
                    }
                } else {
                    treeNode = getTreeNode(d, excludeDictionaryId, true);
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
     * @param dictionaryTypeCode 字典类型编码
     * @param dictionaryCode 字典项编码
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmDictionary getDictionaryByDC(
            String dictionaryTypeCode,String dictionaryCode) throws SsmDaoException, SsmSystemException,
    			SsmServiceException {
        Assert.notNull(dictionaryTypeCode, "参数[dictionaryTypeCode]为空!");
        Assert.notNull(dictionaryCode, "参数[dictionaryCode]为空!");
        SsmParameter parameter = new SsmParameter(dictionaryTypeCode,dictionaryCode);
        List<SsmDictionary> list = getEntityDao().find(
                "from Dictionary d where d.dictionaryType.code = :p1 and d.code = :p2 ",parameter);
        return list.isEmpty() ? null:list.get(0);
    }

    /**
     * @param dictionaryTypeCode 字典类型编码
     * @param dictionaryValue 字典项值
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmDictionary getDictionaryByDV(
            String dictionaryTypeCode,String dictionaryValue) throws SsmDaoException, SsmSystemException,
    		SsmServiceException {
        Assert.notNull(dictionaryTypeCode, "参数[dictionaryTypeCode]为空!");
//        Assert.notNull(dictionaryValue, "参数[dictionaryValue]为空!");
        SsmParameter parameter = new SsmParameter(dictionaryTypeCode,dictionaryValue);
        List<SsmDictionary> list = getEntityDao().find(
                "from Dictionary d where d.dictionaryType.code = :p1 and d.value = :p2 ",parameter);
        return list.isEmpty() ? null:list.get(0);
    }

	/**
	 * 根据数据字典类型编码得到数据字典列表.
	 *
	 * @param dictionaryTypeCode 字典分类编码
	 * @return
	 * @throws DaoException
	 *             ,SystemException,ServiceException
	 */
    @Cacheable(value = { SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE})
	@SuppressWarnings("unchecked")
	public List<SsmDictionary> getDictionarysByDictionaryTypeCode(
			String dictionaryTypeCode) throws SsmDaoException, SsmSystemException,
			SsmServiceException {
        Assert.notNull(dictionaryTypeCode, "参数[dictionaryTypeCode]为空!");
        SsmParameter parameter = new SsmParameter(dictionaryTypeCode);
        List<SsmDictionary> list = getEntityDao().find(
				"from Dictionary d where d.dictionaryType.code = :p1 order by d.orderNo",parameter);
        logger.debug("缓存:{}", SsmCacheConstants.DICTIONARYS_BY_TYPE_CACHE+" 参数：dictionaryTypeCode="+dictionaryTypeCode);
        return list;
	}

	/**
	 * Combobox下拉框数据.
	 *
	 * @param dictionaryTypeCode
	 *            数据字典类型编码
	 * @return List<Combobox> 映射关系： Combobox.text-->Dicitonary.text;Combobox.value-->Dicitonary.value;
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
    @Cacheable(value = { SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE})
	public List<SsmCombobox> getByDictionaryTypeCode(String dictionaryTypeCode)
			throws SsmDaoException, SsmSystemException, SsmServiceException {
		List<SsmDictionary> list = getDictionarysByDictionaryTypeCode(dictionaryTypeCode);
        List<SsmCombobox> cList = Lists.newArrayList();
        for (SsmDictionary d : list) {
        	SsmCombobox c = new SsmCombobox(d.getValue(), d.getName());
            cList.add(c);
        }
        logger.debug("缓存:{}", SsmCacheConstants.DICTIONARYS_CONBOBOX_BY_TYPE_CACHE+" 参数：dictionaryTypeCode="+dictionaryTypeCode);
        return cList;

	}

	/**
	 * 根据父ID得到list对象.
	 *
	 * @param parentCode
	 *            父级编码
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SsmDictionary> getByParentCode(String parentCode)
			throws SsmDaoException, SsmSystemException, SsmServiceException {
		StringBuilder sb = new StringBuilder();
		SsmParameter parameter = new SsmParameter();
		sb.append("from Dictionary d where d.status = :status ");
        parameter.put("status",SsmStatusState.normal.getValue());
		if (parentCode == null) {
			sb.append(" and d.parentDictionary is null ");
		} else {
			sb.append(" and d.parentDictionary.code  = :parentCode ");
            parameter.put("parentCode",parentCode);
		}
		sb.append(" order by d.orderNo");
		List<SsmDictionary> list = getEntityDao().find(sb.toString(), parameter);
		return list;
	}

	/**
	 * 得到排序字段的最大值.
	 *
	 * @return 返回排序字段的最大值
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Integer getMaxSort() throws SsmDaoException, SsmSystemException,
			SsmServiceException {
		Iterator<?> iterator = dictionaryDao.createQuery(
				"select max(d.orderNo)from Dictionary d ").iterate();
		Integer max = null;
		while (iterator.hasNext()) {
			// Object[] row = (Object[]) iterator.next();
			max = (Integer) iterator.next();
		}
		if (max == null) {
			max = 0;
		}
		return max;
	}

    /**
     * 根据字典类型编码 查找
     * @param groupDictionaryTypeCode 字典分类分组编码
     * @return Map<String, List<Dictionary>> key:分类编码（即子类编码） value: 数据字典项集合List<Dictionary>
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public Map<String, List<SsmDictionary>> getDictionaryTypesByGroupDictionaryTypeCode(String groupDictionaryTypeCode)
            throws SsmDaoException, SsmSystemException,SsmServiceException {
        Map<String, List<SsmDictionary>> map = Maps.newHashMap();
        SsmDictionaryType dictionaryType = dictionaryTypeManager.getByCode(groupDictionaryTypeCode);
        for (SsmDictionaryType subDictionaryType : dictionaryType.getSubDictionaryTypes()) {
            List<SsmDictionary> dictionaries = this.getDictionarysByDictionaryTypeCode(subDictionaryType.getCode());
            map.put(subDictionaryType.getCode(), dictionaries);
        }
        return map;
    }
}
