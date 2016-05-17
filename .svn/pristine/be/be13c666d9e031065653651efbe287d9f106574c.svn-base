/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.service;

import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.Validate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.common.utils.io.SsmPropertiesLoader;
import com.ssm.pay.modules.sys.entity.SsmConfig;
import com.ssm.pay.utils.SsmAppConstants;

/** 系统配置参数
* @author Jiang.Li
* @version 2016年2月27日 下午8:58:33
*/
@Service
public class SsmConfigManager extends SsmEntityManager<SsmConfig, Long>{
	private SsmHibernateDao<SsmConfig, Long> configDao;


    /**
     * 通过注入的sessionFactory初始化默认的泛型DAO成员变量.
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        configDao = new SsmHibernateDao<SsmConfig, Long>(sessionFactory, SsmConfig.class);
    }

    @Override
    protected SsmHibernateDao<SsmConfig, Long> getEntityDao() {
        return configDao;
    }

    /**
     * 根据标识查找
     * @param code 配置标识
     * @return
     */
    public SsmConfig getConfigByCode(String code){
        Validate.notBlank("code", "参数[code]不能为空.");
        List<SsmConfig> list = getEntityDao().find("from SsmConfig c where c.code = :p1",new SsmParameter(code));
        return list.isEmpty() ? null:list.get(0);
    }

    /**
     * 根据标识查找
     * @param code 配置标识
     * @return
     */
    public String getConfigValueByCode(String code){
        Validate.notBlank("code", "参数[code]不能为空.");
        SsmConfig config = getConfigByCode(code);
        return config == null ? null:config.getValue();
    }

    /**
     * 从配置文件同步
     * @param overrideFromProperties
     */
    public void syncFromProperties(Boolean overrideFromProperties){
    	SsmPropertiesLoader propertiesLoader = SsmAppConstants.getConfig();
        Properties properties = propertiesLoader.getProperties();
        for(String key:properties.stringPropertyNames()){
        	SsmConfig config = getConfigByCode(key);
            if(config == null){
                config = new SsmConfig(key,properties.getProperty(key),null);
                this.save(config);
            }else{
                if(overrideFromProperties != null && overrideFromProperties == true){
                    config.setValue(properties.getProperty(key));
                    this.update(config);
                }
            }

        }
    }
}
