/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.service;

import java.util.Iterator;
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
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.modules.sys.entity.SsmDictionaryType;
import com.ssm.pay.utils.SsmCacheConstants;

/** 数据字典类型实现类
* @author Jiang.Li
* @version 2016年4月11日 下午7:44:53
*/
@Service
public class SsmDictionaryTypeManager extends SsmEntityManager<SsmDictionaryType, Long> {
	private SsmHibernateDao<SsmDictionaryType, Long> dictionaryTypeDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dictionaryTypeDao = new SsmHibernateDao<SsmDictionaryType, Long>(
				sessionFactory, SsmDictionaryType.class);
	}

	@Override
	protected SsmHibernateDao<SsmDictionaryType, Long> getEntityDao() {
		return dictionaryTypeDao;
	}

    @CacheEvict(value = { SsmCacheConstants.DICTIONARY_TYPE_ALL_CACHE,
    		SsmCacheConstants.DICTIONARY_TYPE_GROUPS_CACHE},allEntries = true)
	public void saveOrUpdate(SsmDictionaryType entity) throws SsmDaoException,
				SsmSystemException, SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.DICTIONARY_TYPE_ALL_CACHE
                +","+SsmCacheConstants.DICTIONARY_TYPE_GROUPS_CACHE);
        Assert.notNull(entity, "参数[entity]为空!");
		dictionaryTypeDao.saveOrUpdate(entity);
	}

    @CacheEvict(value = { SsmCacheConstants.DICTIONARY_TYPE_ALL_CACHE,
    		SsmCacheConstants.DICTIONARY_TYPE_GROUPS_CACHE},allEntries = true)
	public void deleteByIds(List<Long> ids) throws SsmDaoException, SsmSystemException,
			SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.DICTIONARY_TYPE_ALL_CACHE
                +","+SsmCacheConstants.DICTIONARY_TYPE_GROUPS_CACHE);
        if(!SsmCollections3.isEmpty(ids)){
            for (Long id:ids) {
            	SsmDictionaryType dictionaryType = this.getById(id);
                List<SsmDictionaryType> subDictionaryTypes = dictionaryType.getSubDictionaryTypes();
                if (!subDictionaryTypes.isEmpty()) {
                    dictionaryTypeDao.deleteAll(subDictionaryTypes);
                }
                dictionaryTypeDao.delete(dictionaryType);
            }
        }else{
            logger.warn("参数[ids]为空.");
        }
	}


    @CacheEvict(value = { SsmCacheConstants.DICTIONARY_TYPE_ALL_CACHE,
    		SsmCacheConstants.DICTIONARY_TYPE_GROUPS_CACHE},allEntries = true)
    @Override
    public void saveEntity(SsmDictionaryType entity) throws SsmDaoException, SsmSystemException, SsmServiceException {
        logger.debug("清空缓存:{}", SsmCacheConstants.DICTIONARY_TYPE_ALL_CACHE
                +","+SsmCacheConstants.DICTIONARY_TYPE_GROUPS_CACHE);
        super.saveEntity(entity);
    }

    /**
	 * 根据名称name得到对象.
	 * 
	 * @param name
	 *            数据字典名称
	 * @return
	 * @throws DaoException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public SsmDictionaryType getByName(String name) throws SsmDaoException,
			SsmSystemException, SsmServiceException {
		if (SsmStringUtils.isBlank(name)) {
			return null;
		}
		name = SsmStringUtils.strip(name);// 去除两边空格
		SsmParameter parameter = new SsmParameter(name);
		List<SsmDictionaryType> list = getEntityDao().find("from DictionaryType d where d.name = :p1",parameter);
		return list.isEmpty() ? null : list.get(0);
	}

    /**
     * 根据分组编码以及名称查找对象
     * @param groupDictionaryTypeCode 分组类型编码
     * @param name   类型名称
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public SsmDictionaryType getByGroupCode_Name(String groupDictionaryTypeCode,String name) throws SsmDaoException,
    			SsmSystemException, SsmServiceException {
        Assert.notNull(groupDictionaryTypeCode, "参数[groupDictionaryTypeCode]为空!");
        Assert.notNull(name, "参数[name]为空!");
        SsmParameter parameter = new SsmParameter(groupDictionaryTypeCode,name);
        List<SsmDictionaryType> list = getEntityDao().find("from DictionaryType d where d.groupDictionaryType.code = :p1 and d.name = :p2",parameter);
        return list.isEmpty() ? null : list.get(0);
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
	public SsmDictionaryType getByCode(String code) throws SsmDaoException,
			SsmSystemException, SsmServiceException {
		if (SsmStringUtils.isBlank(code)) {
			return null;
		}
		code = SsmStringUtils.strip(code);// 去除两边空格
		SsmParameter parameter = new SsmParameter(code);
		List<SsmDictionaryType> list = getEntityDao().find("from DictionaryType d where d.code = :p1",parameter);
		return list.isEmpty() ? null : list.get(0);
	}


    @Cacheable(value = { SsmCacheConstants.DICTIONARY_TYPE_ALL_CACHE})
	public List<SsmDictionaryType> getAll() throws SsmDaoException, SsmSystemException,
			SsmServiceException {
		logger.debug("缓存:{}", SsmCacheConstants.DICTIONARY_TYPE_ALL_CACHE);
		return dictionaryTypeDao.getAll();
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
		Iterator<?> iterator = dictionaryTypeDao.createQuery(
				"select max(d.orderNo)from DictionaryType d ").iterate();
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
     * 查找所有分组列表.
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    @Cacheable(value = {SsmCacheConstants.DICTIONARY_TYPE_GROUPS_CACHE})
    public List<SsmDictionaryType> getGroupDictionaryTypes() throws SsmDaoException, SsmSystemException,
    		SsmServiceException {
        List<SsmDictionaryType> list = dictionaryTypeDao.find(
                "from DictionaryType d where d.groupDictionaryType is null");
        logger.debug("缓存:{}", SsmCacheConstants.DICTIONARY_TYPE_GROUPS_CACHE);
        return list;
    }
}
