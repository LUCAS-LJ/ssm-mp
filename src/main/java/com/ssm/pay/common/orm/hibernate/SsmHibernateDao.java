/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

import com.ssm.pay.common.orm.SsmPage;
import com.ssm.pay.common.orm.SsmPropertyFilter;
import com.ssm.pay.common.orm.SsmPropertyFilter.SsmMatchType;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.reflection.SsmReflectionUtils;

/** 
 * 封装SpringSide扩展功能的Hibernate DAO泛型基类.
 * 
 * 扩展功能包括分页查询,按属性过滤条件列表查询. 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 * 
 * @param <T>
 *            DAO操作的对象类型
 * @param <PK>
 *            主键类型
 * @author Jiang.Li
 * @version 2016年1月29日 上午11:21:39
*/
public class SsmHibernateDao <T, PK extends Serializable> extends SsmSimpleHibernateDao<T, PK>{
	/**
	 * 用于Dao层子类的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends
	 * HibernateDao<User, Long>{ }
	 */
	public SsmHibernateDao() {
		super();
	}

	/**
	 * 用于省略Dao层, Service层直接使用通用HibernateDao的构造函数. 在构造函数中定义对象类型Class. eg.
	 * HibernateDao<User, Long> userDao = new HibernateDao<User,
	 * Long>(sessionFactory, User.class);
	 */
	public SsmHibernateDao(final SessionFactory sessionFactory,
			final Class<T> entityClass) {
		super(sessionFactory, entityClass);
	}

	// -- 分页查询函数 --//

	/**
	 * 分页获取全部对象.
	 */
	public SsmPage<T> getAll(final SsmPage<T> page) {
		return findPage(page);
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page
	 *            分页参数. 注意不支持其中的orderBy参数.
	 * @param hql
	 *            hql语句.
	 * @param values
	 *            数量可变的查询参数,按顺序绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public SsmPage<T> findPage(final SsmPage<T> page, final String hql,
			final Object... values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		List result = q.list();
		page.setResult(result);
		return page;
	}

    /**
     * 按HQL分页查询.
     *
     * @param page
     *            分页参数. 注意不支持其中的orderBy参数.
     * @param hql
     *            hql语句.
     * @param parameter
     *            数量可变的查询参数,按顺序绑定.
     *
     * @return 分页查询结果, 附带结果列表及所有查询输入参数.
     */
    @SuppressWarnings("unchecked")
    public SsmPage<T> findPage(final SsmPage<T> page, final String hql,
                            final SsmParameter parameter) {
        Assert.notNull(page, "page不能为空");

        Query q = createQuery(hql, parameter);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, parameter);
            page.setTotalCount(totalCount);
        }

        setPageParameterToQuery(q, page);

        List result = q.list();
        page.setResult(result);
        return page;
    }

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page
	 *            分页参数. 注意不支持其中的orderBy参数.
	 * @param hql
	 *            hql语句.
	 * @param values
	 *            命名参数,按名称绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public SsmPage<T> findPage(final SsmPage<T> page, final String hql,
			final Map<String, ?> values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		List result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按Criteria分页查询.
	 * 
	 * @param page
	 *            分页参数.
	 * @param criterions
	 *            数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public SsmPage<T> findPage(final SsmPage<T> page, final Criterion... criterions) {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria(criterions);

		if (page.isAutoCount()) {
			long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageParameterToCriteria(c, page);

		List result = c.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
    public Query setPageParameterToQuery(final Query q, final SsmPage<T> page) {
		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		// hibernate的firstResult的序号从0开始
		q.setFirstResult(page.getFirst() - 1);
		q.setMaxResults(page.getPageSize());
		return q;
	}

	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 */
	protected Criteria setPageParameterToCriteria(final Criteria c,
			final SsmPage<T> page) {
		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		// hibernate的firstResult的序号从0开始
		c.setFirstResult(page.getFirst() - 1);
		c.setMaxResults(page.getPageSize());

		//设置排序
		super.setPageParameterToCriteria(c, page.getOrderBy(), page.getOrder());
		return c;
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
    public long countHqlResult(final String hql, final Object... values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:"
					+ countHql, e);
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:"
					+ countHql, e);
		}
	}

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     *
     */
    protected long countHqlResult(final String hql, final SsmParameter parameter) {
        String countHql = prepareCountHql(hql);
        Long count = 0L;
        try {
//            Long count = (Long)createQuery(countHql, parameter).uniqueResult();

            Query query = createQuery(countHql, parameter);
            List<Object> list = query.list();
            if (list.size() > 0) {
                count = (Long)list.get(0);
            } else {
                count = Long.valueOf(list.size());
            }

            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:"
                    + countHql, e);
        }
    }


	private String prepareCountHql(String orgHql) {
		String fromHql = orgHql;
		// select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + SsmStringUtils.substringAfter(fromHql, "from");
		fromHql = SsmStringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;
		return countHql;
	}

	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */
	@SuppressWarnings("unchecked")
	protected long countCriteriaResult(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) SsmReflectionUtils.getFieldValue(impl,
					"orderEntries");
			SsmReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		Long totalCountObject = (Long) c.setProjection(Projections.rowCount())
				.uniqueResult();
		long totalCount = (totalCountObject != null) ? totalCountObject : 0;

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			SsmReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}

	// -- 属性过滤条件(PropertyFilter)查询函数 --//

	/**
	 * 按属性查找对象列表,支持多种匹配方式.
	 * 
	 * @param matchType
	 *            匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
	 */
	public List<T> findBy(final String propertyName, final Object value,
                          final SsmMatchType matchType) {
		Criterion criterion = buildCriterion(propertyName, value, matchType);
		return find(criterion);
	}

	/**
	 * 按属性过滤条件列表查找对象列表.
	 */
	public List<T> find(List<SsmPropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return find(criterions);
	}
	
	/**
	 * 按属性过滤条件列表查找对象列表.
	 * 
	 * @param orderBy
	 *            排序字段 多个排序字段时用','分隔.
	 * @param order
	 *            排序方式"asc"、"desc" 中间以","分割
	 */
	public List<T> find(final List<SsmPropertyFilter> filters,
			final String orderBy, final String order) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return find(orderBy, order, criterions);
	}

	/**
	 * 按属性过滤条件列表分页查找对象.
	 */
	public SsmPage<T> findPage(final SsmPage<T> page,
			final List<SsmPropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return findPage(page, criterions);
	}

	/**
	 * 按属性条件参数创建Criterion,辅助函数.
	 */
	protected Criterion buildCriterion(final String propertyName,
			final Object propertyValue, final SsmMatchType matchType) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = null;
		String value;
		Character ESCAPE = '!';
		// 根据MatchType构造criterion
		switch (matchType) {
		case EQ:
			criterion = Restrictions.eq(propertyName, propertyValue);
			break;
		case NE:
			criterion = Restrictions.ne(propertyName, propertyValue);
			break;
		case LIKE:
			// 过滤特殊字符
			value = (String) propertyValue;
			if ((ESCAPE.toString()).equals(value)) {
				criterion = Restrictions.like(propertyName,
						(String) propertyValue, MatchMode.ANYWHERE);
			} else {
				criterion = new SsmLikeExpression(propertyName,
						(String) propertyValue, MatchMode.ANYWHERE, ESCAPE,
						true);
			}
			break;
		case SLIKE:
			// 过滤特殊字符
			value = (String) propertyValue;
			if ((ESCAPE.toString()).equals(value)) {
				criterion = Restrictions.like(propertyName,
						(String) propertyValue, MatchMode.START);
			} else {
				criterion = new SsmLikeExpression(propertyName,
						(String) propertyValue, MatchMode.START, ESCAPE,
						true);
			}
			break;
		case ELIKE:
			// 过滤特殊字符
			value = (String) propertyValue;
			if ((ESCAPE.toString()).equals(value)) {
				criterion = Restrictions.like(propertyName,
						(String) propertyValue, MatchMode.END);
			} else {
				criterion = new SsmLikeExpression(propertyName,
						(String) propertyValue, MatchMode.END, ESCAPE,
						true);
			}
			break;
		case LE:
			criterion = Restrictions.le(propertyName, propertyValue);
			break;
		case LT:
			criterion = Restrictions.lt(propertyName, propertyValue);
			break;
		case GE:
			criterion = Restrictions.ge(propertyName, propertyValue);
			break;
		case GT:
			criterion = Restrictions.gt(propertyName, propertyValue);
            break;
        case ISNULL:
            criterion = Restrictions.isNull(propertyName);
		}
		return criterion;
	}

	/**
	 * 按属性条件列表创建Criterion数组,辅助函数.
	 */
	public Criterion[] buildCriterionByPropertyFilter(
			final List<SsmPropertyFilter> filters) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		for (SsmPropertyFilter filter : filters) {
			if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
				Criterion criterion = buildCriterion(filter.getPropertyName(),
						filter.getMatchValue(), filter.getMatchType());
				criterionList.add(criterion);
			} else {// 包含多个属性需要比较的情况,进行or处理.
				Disjunction disjunction = Restrictions.disjunction();
				for (String param : filter.getPropertyNames()) {
					Criterion criterion = buildCriterion(param,
							filter.getMatchValue(), filter.getMatchType());
					disjunction.add(criterion);
				}
				criterionList.add(disjunction);
			}
		}
        return criterionList.toArray(new Criterion[criterionList.size()]);
	}

	/**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 * 
	 * @param uniquePropertyNames
	 *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	public boolean isUnique(T entity, String uniquePropertyNames) {
		Assert.hasText(uniquePropertyNames);
		Criteria criteria = getSession().createCriteria(entityClass).setProjection(
				Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一列
			for (int i = 0; i < nameList.length; i++) {
				criteria.add(Restrictions.eq(nameList[i],
						PropertyUtils.getProperty(entity, nameList[i])));
			}

			// 以下代码为了如果是update的情况,排除entity自身.

			String idName = getSessionFactory().getClassMetadata(
					entity.getClass()).getIdentifierPropertyName();
			if (idName != null) {
				// 取得entity的主键值
				Serializable id = (Serializable) PropertyUtils.getProperty(
						entity, idName);

				// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
				if (id != null)
					criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
			}
		} catch (Exception e) {
			SsmReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
		return ((Number) criteria.uniqueResult()).intValue() == 0;
	}


    // -------------- SQL Query --------------

    /**
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @return
     */
    public <E> SsmPage<E> findBySql(SsmPage<E> page, String sqlString) {
        return findBySql(page, sqlString, null, null);
    }

    /**
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @param parameter
     * @return
     */
    public <E> SsmPage<E> findBySql(SsmPage<E> page, String sqlString, SsmParameter parameter) {
        return findBySql(page, sqlString, parameter, null);
    }

    /**
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @param resultClass
     * @return
     */
    public <E> SsmPage<E> findBySql(SsmPage<E> page, String sqlString, Class<?> resultClass) {
        return findBySql(page, sqlString, null, resultClass);
    }

    /**
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @param resultClass
     * @param parameter
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> SsmPage<E> findBySql(SsmPage<E> page, String sqlString, SsmParameter parameter, Class<?> resultClass) {
        // get count
        String countSqlString = "select count(*) " + super.removeSelect(super.removeOrders(sqlString));
        // page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
        Query countQuery = createSqlQuery(countSqlString, parameter);
        List<Object> list = countQuery.list();
        if (list.size() > 0) {
            page.setTotalCount(Long.valueOf(list.get(0).toString()));
        } else {
            page.setTotalCount(list.size());
        }
        if (page.getTotalCount() < 1) {
            return page;
        }
        // order by
        String sql = sqlString;
		if (SsmStringUtils.isNotBlank(page.getOrderBy()) && SsmStringUtils.isNotBlank(page.getOrder())) {
			String[] orderByArray = SsmStringUtils.split(page.getOrderBy(), ',');
			String[] orderArray = SsmStringUtils.split(page.getOrder(), ',');

			Assert.isTrue(orderByArray.length == orderArray.length,
					"分页多重排序参数中,排序字段与排序方向的个数不相等");
			sql += " order by ";
			for (int i = 0; i < orderByArray.length; i++) {
				sql += orderByArray[i] + " " + orderArray[i];
				if (i != orderByArray.length - 1) {
					sql += ",";
				}
			}
		}


        SQLQuery query = createSqlQuery(sql, parameter);
        query.setFirstResult(page.getFirstResult());
        query.setMaxResults(page.getMaxResults());
        setResultTransformer(query, resultClass);
        page.setResult(query.list());
        return page;
    }
}
