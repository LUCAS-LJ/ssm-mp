/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.web.listener;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @author Jiang.Li
* @version 2016年1月28日 下午9:01:29
* SMM系统初始化监听器
*/
public class SsmSystemListener implements ServletContextListener,HttpSessionListener,HttpSessionAttributeListener{

	private static final Logger logger = LoggerFactory
			.getLogger(SsmSystemListener.class);
	/**
	 * 默认构造函数
	 */
	public SsmSystemListener(){
		
	}
	public void attributeAdded(HttpSessionBindingEvent se) {
		logger.debug("SSM-MP attribute Added");
	}

	public void attributeRemoved(HttpSessionBindingEvent se) {
		logger.debug("SSM-MP attribute Removed");
	}

	public void attributeReplaced(HttpSessionBindingEvent se) {
		logger.debug("SSM-MP attribute Replaced");
	}

	/**
	 * Session创建
	 */
	public void sessionCreated(HttpSessionEvent se) {
		logger.debug("SSM-MP session has bean created");
	}

	/**
	 * Session销毁
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		logger.debug("SSM-MP session has bean destroyed");
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		Properties props = System.getProperties();
		logger.info(props.toString());  
        logger.info("====SSM-MP system has bean started.======");
		
	}

	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("====SSM-MP system has bean stoped.======");
	}

}
