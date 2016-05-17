/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.security;

import java.util.Map;
import javax.servlet.http.HttpSession;
import com.google.common.collect.Maps;

/**应用session上下文 
* @author Jiang.Li
* @version 2016年2月27日 下午8:44:45
*/
public class SsmApplicationSessionContext {
	private static SsmApplicationSessionContext instance;
	
	/**
	 * 用于存储HttpSession对象
	 */
	private Map<String,HttpSession> sessionData;
	
	public SsmApplicationSessionContext(){
		sessionData = Maps.newHashMap();
	}
	/**
	 * 单利模式实现Session类型实例化
	 * @return
	 */
	public static SsmApplicationSessionContext getInstance() {
		if (instance == null) {
			instance = new SsmApplicationSessionContext();
		}
		return instance;
	}

	public synchronized void addSession(HttpSession session) {
		if (session != null) {
			sessionData.put(session.getId(), session);
		}
	}

	public synchronized void removeSession(HttpSession session) {
		if (session != null) {
			sessionData.remove(session.getId());
		}
	}

	public synchronized HttpSession getSession(String sessionId) {
		if (sessionId == null) return null;
		return (HttpSession) sessionData.get(sessionId);
	}

	public Map<String,HttpSession> getSessionData() {
		return sessionData;
	}
}
