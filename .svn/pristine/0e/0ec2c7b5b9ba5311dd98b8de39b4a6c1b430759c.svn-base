/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.aop;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssm.pay.common.orm.hibernate.SsmDefaultEntityManager;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.browser.SsmBrowserType;
import com.ssm.pay.common.utils.browser.SsmBrowserUtils;
import com.ssm.pay.common.web.springmvc.SsmSpringMVCHolder;
import com.ssm.pay.core.aop.annotation.SsmLogging;
import com.ssm.pay.core.security.SsmSecurityConstants;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.modules.sys._enum.SsmLogType;
import com.ssm.pay.modules.sys.entity.SsmLog;

/** 日志拦截
* @author Jiang.Li
* @version 2016年2月27日 下午9:23:21
*/
@Aspect
@Component(value = SsmSecurityConstants.SERVICE_SECURITY_LOGINASPECT)
public class SsmLogAspect {
	private static Logger logger = LoggerFactory.getLogger(SsmLogAspect.class);

	@Autowired
    private SsmDefaultEntityManager defaultEntityManager;

    /**
     * @param point 切入点
     */

	@Around("execution(* com.ssm.pay.modules.*.service..*Manager.*(..))")
    public Object logAll(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        // 执行方法名
        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();
        String userName = null;
        Long start = 0L;
        Long end = 0L;
        String ip = null;
        // 当前用户
        try {
            // 执行方法所消耗的时间
            start = System.currentTimeMillis();
            result = point.proceed();
            end = System.currentTimeMillis();

            // 登录名
            SsmSessionInfo sessionInfo = null;
            try {
                sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            if (sessionInfo != null) {
                userName = SsmStringUtils.isBlank(sessionInfo.getName()) ? sessionInfo.getLoginName() : sessionInfo.getName();
                ip = sessionInfo.getIp();
            } else {
                userName = "系统";
                ip = "127.0.0.1";
//                logger.warn("sessionInfo为空.");
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(),e);
            throw e;
        }
        String name = className;
        // 操作类型
        String opertype = methodName;

        //注解式日志
        MethodSignature joinPointObject = (MethodSignature) point.getSignature();
        Method method = joinPointObject.getMethod();
        SsmLogging logging = method.getAnnotation(SsmLogging.class);//注解式日志
        boolean loglog = false;
        String remark = null;
        String newLogValue = null;
        if(logging != null && logging.logging() == true){
            loglog = true;
            String logValue = logging.value();
            newLogValue = logValue;
            if(SsmStringUtils.isNotBlank(logValue)){
                Object[] args = point.getArgs();
                newLogValue = MessageFormat.format(logValue,args);
            }

            remark = newLogValue;
        }

        if (loglog == true ||
                ((opertype.indexOf("save") > -1 || opertype.indexOf("update") > -1 ||
                        opertype.indexOf("delete") > -1 || opertype.indexOf("remove") > -1 || opertype.indexOf("merge") > -1) && (logging != null && logging.logging() != false))) {
            Long time = end - start;
            SsmLog log = new SsmLog();
            log.setType(SsmLogType.operate.getValue());
            log.setLoginName(userName);
            log.setModule(name);
            log.setAction(opertype);
            log.setOperTime(new Date(start));
            log.setActionTime(time.toString());
            log.setIp(ip);
            log.setRemark(remark);
            SsmBrowserType browserType = SsmBrowserUtils.getBrowserType(SsmSpringMVCHolder.getRequest());
            log.setBrowserType(browserType == null ? null : browserType.toString());
            defaultEntityManager.save(log);
        }
        if(logger.isDebugEnabled()){
            logger.debug("用户:{},操作类：{},操作方法：{},耗时：{}ms.",new Object[]{userName,className,methodName,end - start});
        }
        return result;
    }
}
