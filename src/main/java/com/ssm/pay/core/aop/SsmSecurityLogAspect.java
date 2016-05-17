/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.aop;

import java.util.Date;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.ssm.pay.common.orm.hibernate.SsmDefaultEntityManager;
import com.ssm.pay.common.utils.browser.SsmBrowserType;
import com.ssm.pay.common.utils.browser.SsmBrowserUtils;
import com.ssm.pay.common.web.springmvc.SsmSpringMVCHolder;
import com.ssm.pay.core.security.SsmSecurityType;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.modules.sys._enum.SsmLogType;
import com.ssm.pay.modules.sys.entity.SsmLog;
import com.ssm.pay.modules.sys.web.SsmLoginController;

/** 使用AspectJ实现登录登出日志AOP
* @author Jiang.Li
* @version 2016年1月29日 上午10:13:29
*/
@Aspect
public class SsmSecurityLogAspect {
	
	private static Logger logger = LoggerFactory.getLogger(SsmSecurityLogAspect.class);

	@Autowired
    private SsmDefaultEntityManager defaultEntityManager;

    /**
     * 登录增强
     * @param joinPoint 切入点
     */
    @After("execution(* com.ssm.pay.modules.sys.web.SsmLoginController.login(..))")
    public void afterLoginLog(JoinPoint joinPoint) throws Throwable{
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        if(sessionInfo != null){
            saveLog(sessionInfo,joinPoint,SsmSecurityType.login); //保存日志
        }
    }

    /**
     * 登出增强
     * @param joinPoint 切入点
     */
    @Before("execution(* com.ssm.pay.modules.sys.web.SsmLoginController.logout(..))")
    public void beforeLogoutLog(JoinPoint joinPoint) throws Throwable{
    	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        if(sessionInfo != null){
            saveLog(sessionInfo,joinPoint,SsmSecurityType.logout); //保存日志
        }
    }

    /**
     * 保存日志
     * @param sessionInfo 登录用户session信息
     * @param joinPoint 切入点
     * @param securityType 是否是登录操作
     *                     @see SecurityType
     */
    public void saveLog(SsmSessionInfo sessionInfo, JoinPoint joinPoint,SsmSecurityType securityType){
        Object result = null;
        // 执行方法名

        String methodName = null;
        String className = null;
        if(joinPoint != null){
            methodName = joinPoint.getSignature().getName();
            className = joinPoint.getTarget().getClass().getSimpleName();
        }else{
            className = SsmLoginController.class.getSimpleName();
            methodName = "logout";
        }
        String user = null;
        Long start = 0L;
        Long end = 0L;
        // 执行方法所消耗的时间
        try {
            start = System.currentTimeMillis();
            end = System.currentTimeMillis();
            Long opTime = end - start;
            SsmLog log = new SsmLog();
            log.setType(SsmLogType.security.getValue());
            log.setLoginName(sessionInfo.getLoginName());
            log.setModule(className + "-" + methodName);
            log.setActionTime(opTime.toString());
            log.setIp(sessionInfo.getIp());
            log.setAction("["+sessionInfo.getLoginName()+"]"+securityType.getDescription());
            SsmBrowserType browserType = SsmBrowserUtils.getBrowserType(SsmSpringMVCHolder.getRequest());
            log.setBrowserType(browserType == null ? null : browserType.toString());
            if(logger.isDebugEnabled()){
                logger.debug("用户:{},操作类：{},操作方法：{},耗时：{}ms.",new Object[]{user,className,methodName,end - start});
            }
            log.setOperTime(new Date());
            defaultEntityManager.save(log);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
