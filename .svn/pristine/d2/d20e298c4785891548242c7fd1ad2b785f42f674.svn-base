/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.web.springmvc;

import java.io.Serializable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssm.pay.common.exception.SsmActionException;
import com.ssm.pay.common.model.SsmDatagrid;
import com.ssm.pay.common.model.SsmResult;
import com.ssm.pay.common.orm.SsmPage;
import com.ssm.pay.common.orm.SsmPropertyFilter;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateWebUtils;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.reflection.SsmMyBeanUtils;
import com.ssm.pay.common.utils.reflection.SsmReflectionUtils;

/** 控制器支持类基类
* @author Jiang.Li
* @version 2016年4月12日 下午8:28:23
*/
public abstract class SsmBaseController<T, PK extends Serializable> extends SsmSimpleController {
	protected Class<T> entityClass;

    protected SsmBaseController() {
        this.entityClass = SsmReflectionUtils.getClassGenricType(getClass());
    }

    /**
     * EntityManager.
     */
    public abstract SsmEntityManager<T, PK> getEntityManager();

    @ModelAttribute
    public T getModel(@RequestParam(value = "id", required = false) PK id, Model model) {
        T cloneEntity = null;
        boolean flag = (id != null);
        if(id != null && id instanceof String){
            flag = SsmStringUtils.isNotBlank((String)id);
        }
        if (flag) {
            T entity = getEntityManager().getById(id);
            //对象拷贝
            if(entity != null){
                try {
                    cloneEntity = (T) SsmMyBeanUtils.cloneBean(entity);
                } catch (Exception e) {
                    cloneEntity = entity;
                    logger.error(e.getMessage(),e);
                }
            }else{
                throw new SsmActionException("ID为["+id+"]的记录不存在或已被其它用户删除！");
            }
            model.addAttribute("model", cloneEntity);
        }
        return cloneEntity;
    }


    /**
     * 新增或修改.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"save"})
    @ResponseBody
    public SsmResult save(@ModelAttribute("model")T model) {
        getEntityManager().saveEntity(model);
        return SsmResult.successResult();
    }

    /**
     * 根据ID删除
     *
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = {"delete/{id}"})
    @ResponseBody
    public SsmResult delete(@PathVariable PK id) {
        getEntityManager().deleteById(id);
        return SsmResult.successResult();
    }

    /**
     * 根据ID集合批量删除.
     *
     * @param ids 主键ID集合
     * @return
     */
    @RequestMapping(value = {"remove"})
    @ResponseBody
    public SsmResult remove(@RequestParam(value = "ids", required = false) List<PK> ids) {
        getEntityManager().deleteByIds(ids);
        return SsmResult.successResult();
    }


    /**
     * EasyUI 列表数据
     * @return
     */
    @RequestMapping(value = {"datagrid"})
    @ResponseBody
    public SsmDatagrid<T> datagrid() {
        HttpServletRequest request = SsmSpringMVCHolder.getRequest();
        // 自动构造属性过滤器
        List<SsmPropertyFilter> filters = SsmHibernateWebUtils.buildPropertyFilters(request);
        SsmPage<T> p = new SsmPage<T>(request);
        p = getEntityManager().findPage(p, filters,true);
        SsmDatagrid<T> datagrid = new SsmDatagrid<T>(p.getTotalCount(), p.getResult());
        return datagrid;
    }

    /**
     * 初始化数据绑定
     * 1. 设置被排除的属性 不自动绑定
     * 2. 将所有传递进来的String进行HTML编码，防止XSS攻击
     * 3. 将字段中Date类型转换为String类型
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        super.initBinder(binder);
        //设置被排除的属性 不自动绑定
        Object annotationValue = AnnotationUtils.getValue(AnnotationUtils.findAnnotation(entityClass, JsonIgnoreProperties.class));
        if (annotationValue != null) {
            String[] jsonIgnoreProperties = (String[]) annotationValue;
            binder.setDisallowedFields(jsonIgnoreProperties);
        }
    }
}
