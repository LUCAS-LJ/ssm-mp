/*@版权所有LIJIANG*/ 
package com.ssm.pay.utils;

import com.ssm.pay.common.spring.SsmSpringContextHolder;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.SsmSysConstants;
import com.ssm.pay.common.utils.io.SsmPropertiesLoader;
import com.ssm.pay.modules.sys.service.SsmConfigManager;

/** 系统使用的静态变量.
* @author Jiang.Li
* @version 2016年2月27日 下午9:05:03
*/
public class SsmAppConstants extends SsmSysConstants{
	private static SsmConfigManager configManager = SsmSpringContextHolder.getBean(SsmConfigManager.class);

    /**
     * 系统初始化时间
     */
    public static long SYS_INIT_TIME = System.currentTimeMillis();

    /**
     * 系统管理员角色编号
     */
    public static final String ROLE_SYSTEM_MANAGER = "system_manager";
    /**
     * 云盘管理员
     */
    public static final String ROLE_DISK_MANAGER = "disk_manager";

	/**
	 * 修改用户密码 个人(需要输入原始密码)
	 */
	public static final String USER_UPDATE_PASSWORD_YES = "1";
	/**
	 * 修改用户密码 个人(不需要输入原始密码)
	 */
	public static final String USER_UPDATE_PASSWORD_NO = "0";



    private static SsmPropertiesLoader appconfig = null;
    private static SsmPropertiesLoader config = null;
    public static final String APPCONFIG_FILE_PATH = "appconfig.properties";
    public static final String CONFIG_FILE_PATH = "config.properties";

    /**
     * 获取配置
     */
    public static String getAppConfig(String key) {
        return SsmSysConstants.getAppConfig().getProperty(key);
    }

    /**
     * 获取配置
     */
    public static String getAppConfig(String key,String defaultValue) {
        return SsmSysConstants.getAppConfig().getProperty(key,defaultValue);
    }

    /**
     * 获取管理端根路径
     */
    public static String getAdminPath() {
        return getAppConfig("adminPath","/a");
    }

    /**
     * 获取前端根路径
     */
    public static String getFrontPath() {
        return getAppConfig("frontPath","/f");
    }

    /**
     * 获取URL后缀
     */
    public static String getUrlSuffix() {
        return getAppConfig("urlSuffix",".html");
    }





    /**
     * 配置文件(config.properties)
     */
    public static SsmPropertiesLoader getConfig() {
        if(config == null){
            config = new SsmPropertiesLoader(CONFIG_FILE_PATH);
        }
        return config;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        return getConfig().getProperty(key);
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key,String defaultValue) {
        return getConfig().getProperty(key,defaultValue);
    }


    /**
     * 日志保留时间 天(默认值:30).
     */
    public static int getLogKeepTime(){
        String code = "logKeepTime";
        String value = getConfigValue(code,"30");
        return Integer.valueOf(value);
    }

    /**
     * 应用文件存储目录 放置于webapp下 应用相对路径
     * 自动化部署 不推荐使用
     * 建议使用{@link AppConstants.getDiskBasePath()}
     * @return
     */
    @Deprecated
    public static String getDiskBaseDir() {
        String code = "disk.baseDir";
        return getConfigValue(code);
    }

    /**
     * 云盘存储路径 磁盘绝对路径
     * @return
     */
    public static String getDiskBasePath() {
        String code = "disk.basePath";
        return getConfigValue(code);
    }

    /**
     * 单个文件上传最大 单位：字节
     * @return
     */
    public static Integer getDiskMaxUploadSize() {
        String code = "disk.maxUploadSize";
        return Integer.valueOf(getConfigValue(code));
    }

    /**
     * 用户(员工)默认最大磁盘空间 5G 单位：M
     * @return
     */
    public static Integer getDiskUserLimitSize() {
        String code = "disk.userLimitSize";
        return Integer.valueOf(getConfigValue(code));
    }


    /**
     * 部门默认最大磁盘空间 10G 单位：M
     * @return
     */
    public static Integer getDiskOrganLimitSize() {
        return getConfig().getInteger("disk.organLimitSize");
    }

    /**
     * 启用安全检查
     * @return
     */
    public static boolean getIsSecurityOn() {
        String code = "security.on";
        String value = getConfigValue(code,"false");
        return "true".equals(value) || "1".equals(value);
    }

    /**
     * 系统最大登录用户数
     * @return
     */
    public static int getSessionUserMaxSize() {
        String code = "sessionUser.MaxSize";
        String value = getConfigValue(code);
        return Integer.valueOf(value);
    }


    /**
     * 获取用户可创建会话数量 默认值：0
     * 0 无限制
     * @return
     */
    public static int getUserSessionSize() {
        String code = "sessionUser.UserSessionSize";
        String value = getConfigValue(code);
        return SsmStringUtils.isBlank(value) ? 0:Integer.valueOf(value);
    }

    /**
     * 非法登录次数不超过X次
     * @return
     */
    public static int getLoginAgainSize() {
        String code = "password.loginAgainSize";
        String value = getConfigValue(code);
        return SsmStringUtils.isBlank(value) ? 3:Integer.valueOf(value);
    }

    /**
     * 用户密码更新周期 （天） 默认值：30
     * @return
     */
    public static int getUserPasswordUpdateCycle() {
        String code = "password.updateCycle";
        String value = getConfigValue(code);
        return SsmStringUtils.isBlank(value) ? 30:Integer.valueOf(value);
    }

    /**
     * 用户密码至少多少次内不能重复 默认值：5
     * @return
     */
    public static int getUserPasswordRepeatCount() {
        String code = "password.repeatCount";
        String value = getConfigValue(code);
        return SsmStringUtils.isBlank(value) ? 5:Integer.valueOf(value);
    }



    /**
     * 查找属性对应的属性值
     * @param code 属性名称
     * @return
     */
    public static String getConfigValue(String code) {
        return getConfigValue(code, null);
    }

    /**
     * 查找属性对应的属性值
     * @param code 属性名称
     * @param defaultValue 默认值
     * @return
     */
    public static String getConfigValue(String code, String defaultValue) {
    	SsmConfigManager configManager = SsmSpringContextHolder.getBean(SsmConfigManager.class);
        String configValue = configManager.getConfigValueByCode(code);
        if(SsmStringUtils.isBlank(configValue)){
            return getConfig().getProperty(code, defaultValue);
        }
        return configValue == null ? defaultValue:configValue;
    }
}
