/*@版权所有LIJIANG*/ 
package com.ssm.pay.core;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.ssm.pay.common.exception.SsmServiceException;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.model.SsmResult;
import com.ssm.pay.common.spring.SsmSpringContextHolder;
import com.ssm.pay.common.utils.SsmExceptions;
import com.ssm.pay.common.utils.SsmIpUtils;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.SsmSysConstants;
import com.ssm.pay.common.utils.SsmSysUtils;
import com.ssm.pay.common.utils.browser.SsmBrowserType;
import com.ssm.pay.common.utils.browser.SsmBrowserUtils;
import com.ssm.pay.common.web.utils.SsmWebUtils;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.modules.sys._enum.SsmLogType;
import com.ssm.pay.modules.sys.entity.SsmLog;
import com.ssm.pay.modules.sys.service.SsmLogManager;

/** 自定义异常拦截器
* @author Jiang.Li
* @version 2016年2月27日 下午11:16:51
*/
public class SsmExceptionInterceptor implements HandlerExceptionResolver  {
	protected Logger logger = LoggerFactory.getLogger(getClass());

    private static SsmLogManager logManager = SsmSpringContextHolder.getBean(SsmLogManager.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// TODO Auto-generated method stub
		SsmResult result = null;
        //开发模式下打印堆栈信息
        if(SsmSysConstants.isdevMode()){
            ex.printStackTrace();
        }else{
            logger.error(ex.getMessage());
        }
        //非Ajax请求 将跳转到500错误页面
//        if(!WebUtils.isAjaxRequest(request)){
//            throw ex;
//        }
        //Ajax方式返回错误信息
        String emsg = ex.getMessage();
        StringBuilder sb = new StringBuilder();
        final String MSG_DETAIL = " 详细信息:";
        boolean isWarn = false;//是否是警告级别的异常
        Object obj = null;//其它信息
        sb.append("发生异常:");
        //Hibernate Validator Bo注解校验异常处理
        if(SsmExceptions.isCausedBy(ex, ConstraintViolationException.class)){
            isWarn = true;
            ConstraintViolationException ce = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> set =  ce.getConstraintViolations();
            Iterator<?> iterator = set.iterator();
            int i = -1;
            while(iterator.hasNext()){
                ConstraintViolation<?> c = (ConstraintViolation<?>) iterator.next();
                sb.append(c.getMessage());
                i++;
                if(i==0){
                    obj = c.getPropertyPath().toString();
                }
                if (i < set.size() - 1) {
                    sb.append(",");
                }else{
                    sb.append(".");
                }
            }
            if(SsmSysConstants.isdevMode()){
                sb.append(MSG_DETAIL).append(ex.getMessage());//将":"替换为","
            }
        }
        //Hibernate 外键关联引用操作异常.
        else if(SsmExceptions.isCausedBy(ex, org.hibernate.exception.ConstraintViolationException.class)){
            isWarn = true;
            sb.append("存在数据被引用,无法执行操作！");
            if(SsmSysConstants.isdevMode()){
                sb.append(MSG_DETAIL).append(SsmSysUtils.jsonStrConvert(emsg));//将":"替换为","
            }
        }
        //Hibernate乐观锁 并发异常处理
        else if(SsmExceptions.isCausedBy(ex, StaleObjectStateException.class)){
            isWarn = true;
            sb.append("当前记录已被其它用户修改或删除！");
            if(SsmSysConstants.isdevMode()){
                sb.append(MSG_DETAIL).append(SsmSysUtils.jsonStrConvert(emsg));//将":"替换为","
            }
        }
        else if(SsmExceptions.isCausedBy(ex, ObjectNotFoundException.class)){
            sb.append("当前记录不存在或已被其它用户删除！");
            if(SsmSysConstants.isdevMode()){
                sb.append(MSG_DETAIL).append(SsmSysUtils.jsonStrConvert(emsg));//将":"替换为","
            }
        }
        //参数类异常 Spring Assert、Apache Common Validate抛出该异常
        else if(SsmExceptions.isCausedBy(ex, IllegalArgumentException.class)){
            isWarn = true;
            sb.append(SsmSysUtils.jsonStrConvert(emsg));//将":"替换为","
        }
        //空指针异常
        else if(SsmExceptions.isCausedBy(ex, NullPointerException.class)){
            sb.append("程序没写好,发生空指针异常！");
            if(SsmSysConstants.isdevMode()){
                sb.append(MSG_DETAIL).append(SsmSysUtils.jsonStrConvert(emsg));//将":"替换为","
            }
        }

        //业务异常
        else if(SsmExceptions.isCausedBy(ex, SsmServiceException.class)){
        	SsmServiceException serviceException = (SsmServiceException) ex;
            result = new SsmResult(serviceException.getCode(), serviceException.getMessage(), serviceException.getObj());
        }

        //系统异常
        else if(SsmExceptions.isCausedBy(ex, SsmSystemException.class)){
            sb.append(SsmSysUtils.jsonStrConvert(emsg));//将":"替换为","
        }

        //其它异常
        else{
            if(SsmSysConstants.isdevMode()){
                sb.append(MSG_DETAIL).append(SsmSysUtils.jsonStrConvert(emsg));//将":"替换为","
            }else{
                sb.append("未知异常！");
            }
        }
        if(isWarn){
            result = new SsmResult(SsmResult.WARN,sb.toString(),obj);
            logger.warn(result.toString());
        }else{
            if(result == null){
                result = new SsmResult(SsmResult.ERROR,sb.toString(),obj);
            }
            logger.error(result.toString());
            saveLog(request,ex);
        }
//        Map<String, Object> model = Maps.newHashMap();
//        model.put("ex", ex);
//        return  new ModelAndView("error-business", model);

        //异步方式返回异常信息
        SsmWebUtils.renderText(response,result);
        return null;
	}


    /**
     * 保存异常日志
     * @param request
     * @param ex
     */
    private void saveLog(HttpServletRequest request,Exception ex){
    	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        if (sessionInfo != null) {

            StringBuilder params = new StringBuilder();
            int index = 0;
            for (Object param : request.getParameterMap().keySet()) {
                params.append((index++ == 0 ? "" : "&") + param + "=");
                params.append(SsmStringUtils.abbr(SsmStringUtils.endsWithIgnoreCase((String) param, "password")
                        ? "" : request.getParameter((String) param), 100));
            }

            SsmLog log = new SsmLog();
            log.setLoginName(sessionInfo.getLoginName());
            log.setType(SsmLogType.exception.getValue());
            SsmBrowserType browserType = SsmBrowserUtils.getBrowserType(request);
            log.setBrowserType(browserType == null ? null : browserType.toString());
            log.setIp(SsmIpUtils.getIpAddr(request));
            log.setModule(request.getRequestURI());
            log.setAction(request.getMethod());
            String exceptionInfo = ex != null ? ex.toString() : "";
            StringBuffer exceptionRemark = new StringBuffer();
            exceptionRemark.append("请求参数：").append(params.toString()).append("异常信息：").append(exceptionInfo);
            log.setRemark(exceptionRemark.toString());
            logManager.save(log);
        }
    }
}
