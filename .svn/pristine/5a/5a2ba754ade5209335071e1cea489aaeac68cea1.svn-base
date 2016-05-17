/*@版权所有LIJIANG*/ 
package com.ssm.pay.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSessionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ssm.pay.common.web.listener.SsmSystemListener;

/** 
 * 系统初始化监听器继承默认监听器
* @author Jiang.Li
* @version 2016年1月28日 下午9:27:19
*/
public class SsmSystemInitlListener extends SsmSystemListener{
	private static final Logger logger = LoggerFactory
			.getLogger(SsmSystemInitlListener.class);
	public SsmSystemInitlListener(){
		
	}
	
	@Override
    public void contextInitialized(ServletContextEvent sce) {
		super.contextInitialized(sce);
	}
	
	public void sessionDestroyed(HttpSessionEvent evt) {
		super.sessionDestroyed(evt);
//		String sessionId = evt.getSession().getId();
		logger.debug("销毁sessionId");
//		SecurityUtils.removeUserFromSession(sessionId,true, SecurityType.logout_abnormal);
	}
}
