/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.utils;

import com.ssm.pay.common.utils.io.SsmPropertiesLoader;

/** 项目中使用的静态变量
* @author Jiang.Li
* @version 2016年1月28日 下午10:37:15
*/
public class SsmSysConstants {
	/**
     * session 验证码key
     */
    public static final String SESSION_VALIDATE_CODE = "validateCode";
    
    private static SsmPropertiesLoader appconfig = null;
    private static SsmPropertiesLoader sensitive = null;
    private static SsmPropertiesLoader sqlfilter = null;
    
    /**
     * 配置文件(appconfig.properties)
     */
    public static SsmPropertiesLoader getAppConfig() {
    	if(appconfig == null){
    		appconfig = new SsmPropertiesLoader("appconfig.properties");
    	}
        return appconfig;
    }

    /**
     * 修改配置文件
     * @param key
     * @param value
     */
    public static void modifyAppConfig(String key,String value) {
        String filePath = "appconfig.properties";
        if(appconfig == null){
            appconfig = new SsmPropertiesLoader(filePath);
        }
        appconfig.modifyProperties(filePath,key,value);
    }
    
    /**
     * 配置文件(sensitive.properties)
     */
    public static SsmPropertiesLoader getSensitive() {
    	if(sensitive == null){
    		sensitive = new SsmPropertiesLoader("sensitive.properties");
    	}
        return sensitive;
    }
    
    /**
     * SQL参数过滤配置文件(sqlfilter.properties)
     */
    public static SsmPropertiesLoader getSqlfilter() {
    	if(sqlfilter == null){
    		sqlfilter = new SsmPropertiesLoader("sqlfilter.properties");
    	}
        return sqlfilter;
    }

    /**
     * jdbc type连接参数(默认:"").
     */
    public static String getJdbcType(){
        return SsmSysConstants.getAppConfig().getProperty("jdbc.type","");
    }
    
    /**
     * jdbc url连接参数(默认:"").
     */
    public static String getJdbcUrl(){
    	return SsmSysConstants.getAppConfig().getProperty("jdbc.url","");
    }

    /**
     * jdbc 驱动类
     * @return
     */
    public static String getJdbcDriverClassName(){
        return SsmSysConstants.getAppConfig().getProperty("jdbc.driverClassName","");
    }

    /**
     * jdbc 用户名
     * @return
     */
    public static String getJdbcUserName(){
        return SsmSysConstants.getAppConfig().getProperty("jdbc.username","");
    }

    /**
     * jdbc 密码
     * @return
     */
    public static String getJdbcPassword(){
        return SsmSysConstants.getAppConfig().getProperty("jdbc.password","");
    }


    /**
     * 获取是否是开发模式(默认:false).
     */
    public static boolean isdevMode(){
    	return getAppConfig().getBoolean("devMode",false);
    }

}
