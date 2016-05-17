/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.Assert;
import org.springframework.web.util.WebUtils;

import com.ssm.pay.common.orm.SsmPropertyFilter;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.SsmSysConstants;
import com.ssm.pay.common.utils.reflection.SsmReflectionUtils;

/** 
* @author Jiang.Li
* @version 2016年4月12日 下午8:42:23
*/
public class SsmHibernateWebUtils {
	public static final String FILTERPREFIX = "filter_";
	private static Properties sqlfilterProperties;
	
	static{
		sqlfilterProperties = SsmSysConstants.getSqlfilter().getProperties();
	}

    private SsmHibernateWebUtils() {
    }

    /**
     * 根据对象ID集合,整理合并集合.
     * <p/>
     * 默认对象主键的名称名为"id".
     *
     * @see #mergeByCheckedIds(java.util.Collection, java.util.Collection, Class, String)
     */
    public static <T, ID> void mergeByCheckedIds(final Collection<T> srcObjects, final Collection<ID> checkedIds,
                                                 final Class<T> clazz) {
        mergeByCheckedIds(srcObjects, checkedIds, clazz, "id");
    }

    /**
     * 根据对象ID集合,整理合并集合.
     * <p/>
     * 页面发送变更后的子对象id列表时,删除原来的子对象集合再根据页面id列表创建一个全新的集合这种看似最简单的做法是不行的.
     * 因此采用如此的整合算法：在源集合中删除id不在目标集合中的对象,根据目标集合中的id创建对象并添加到源集合中.
     * 因为新建对象只有ID被赋值, 因此本函数不适合于cascade-save-or-update的情形.
     *
     * @param srcObjects 源集合,元素为对象.
     * @param checkedIds 目标集合,元素为ID.
     * @param clazz      集合中对象的类型
     * @param idName     对象主键的名称
     */
    public static <T, ID> void mergeByCheckedIds(final Collection<T> srcObjects, final Collection<ID> checkedIds,
                                                 final Class<T> clazz, final String idName) {

        //参数校验
        Assert.notNull(srcObjects, "scrObjects不能为空");
        Assert.hasText(idName, "idName不能为空");
        Assert.notNull(clazz, "clazz不能为空");

        //目标集合为空,删除源集合中所有对象后直接返回.
        if (checkedIds == null) {
            srcObjects.clear();
            return;
        }

        //遍历源集合,如果其id不在目标ID集合中的对象,进行删除.
        //同时,在目标集合中删除已在源集合中的id,使得目标集合中剩下的id均为源集合中没有的id.
        Iterator<T> srcIterator = srcObjects.iterator();
        try {

            while (srcIterator.hasNext()) {
                T element = srcIterator.next();
                Object id;
                id = PropertyUtils.getProperty(element, idName);

                if (!checkedIds.contains(id)) {
                    srcIterator.remove();
                } else {
                    checkedIds.remove(id);
                }
            }

            //ID集合目前剩余的id均不在源集合中,创建对象,为id属性赋值并添加到源集合中.
            for (ID id : checkedIds) {
                T obj = clazz.newInstance();
                PropertyUtils.setProperty(obj, idName, id);
                srcObjects.add(obj);
            }
        } catch (Exception e) {
            throw SsmReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 根据按PropertyFilter命名规则的Request参数,创建PropertyFilter列表.
     * 默认Filter属性名前缀为filter_.
     *
     * @see #buildPropertyFilters(HttpServletRequest, String)
     */
    public static List<SsmPropertyFilter> buildPropertyFilters(final HttpServletRequest request) {
        return buildPropertyFilters(request, FILTERPREFIX);
    }

    /**
     * 根据按PropertyFilter命名规则的Request参数,创建PropertyFilter列表.
     * PropertyFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
     * <p/>
     * eg.
     * filter_EQS_name
     * filter_LIKES_name_OR_email
     */
    public static List<SsmPropertyFilter> buildPropertyFilters(final HttpServletRequest request, final String filterPrefix) {
        return SsmHibernateWebUtils.buildPropertyFilters(request,FILTERPREFIX,true);
    }

    /**
     * 根据按PropertyFilter命名规则的Request参数,创建PropertyFilter列表.
     * PropertyFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
     * <p/>
     * eg.
     * filter_EQS_name
     * filter_LIKES_name_OR_email
     */
    public static List<SsmPropertyFilter> buildPropertyFilters(final HttpServletRequest request, final String filterPrefix,boolean filterSQL) {
        List<SsmPropertyFilter> filterList = new ArrayList<SsmPropertyFilter>();

        //从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
        Map<String, Object> filterParamMap = WebUtils.getParametersStartingWith(request, filterPrefix);

        //分析参数Map,构造PropertyFilter列表
        for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
            String filterName = entry.getKey();
            String value = (String) entry.getValue();

            //如果value值为空,则忽略此filter.
            if (SsmStringUtils.isNotEmpty(value)) {
                if(filterSQL){
                    for (Object oj : sqlfilterProperties.keySet()) {
                        String key = (String) oj;
                        value = value.replaceAll(key, sqlfilterProperties.getProperty(key));
                    }
                }
                SsmPropertyFilter filter = new SsmPropertyFilter(filterName.replaceAll("__", "."), value);
                filterList.add(filter);
            }
        }
        return filterList;
    }
}
