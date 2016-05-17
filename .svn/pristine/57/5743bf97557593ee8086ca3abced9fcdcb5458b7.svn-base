/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.security;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Lists;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.model.SsmDatagrid;
import com.ssm.pay.common.spring.SsmSpringContextHolder;
import com.ssm.pay.common.utils.SsmIpUtils;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.web.springmvc.SsmSpringMVCHolder;
import com.ssm.pay.core.aop.SsmSecurityLogAspect;
import com.ssm.pay.modules.sys.entity.SsmRole;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.modules.sys.service.SsmResourceManager;
import com.ssm.pay.modules.sys.service.SsmUserManager;
import com.ssm.pay.utils.SsmAppConstants;

/**系统使用的特殊工具类 
* @author Jiang.Li
* @version 2016年1月28日 下午11:34:04
*/
public class SsmSecurityUtils extends EmptyInterceptor{
	private static final Logger logger = LoggerFactory.getLogger(SsmSecurityUtils.class);
    private static SsmResourceManager resourceManager = SsmSpringContextHolder.getBean(SsmResourceManager.class);
    private static SsmUserManager userManager = SsmSpringContextHolder.getBean(SsmUserManager.class);
    private static SsmSecurityLogAspect securityLogAspect = SsmSpringContextHolder.getBean(SsmSecurityConstants.SERVICE_SECURITY_LOGINASPECT);
    private static SsmApplicationSessionContext applicationSessionContext = SsmApplicationSessionContext.getInstance();

    /**
     * 是否授权某个资源
     *
     * @param resourceCode 资源编码
     * @return
     */
    public static Boolean isPermitted(String resourceCode) {
        Boolean flag = false;
        try {
        	SsmUser superUser = userManager.getSuperUser();
            SsmSessionInfo sessionInfo = getCurrentSessionInfo();
            if (sessionInfo != null && superUser != null
                    && sessionInfo.getUserId().equals(superUser.getId())) {// 超级用户
                flag = true;
            } else {
                flag = resourceManager.isUserPermittedResourceCode(sessionInfo.getUserId(), resourceCode);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 是否授权某个角色
     *
     * @param roleCode 角色编码
     * @return
     */
    public static Boolean isPermittedRole(String roleCode) {
    	SsmSessionInfo sessionInfo = getCurrentSessionInfo();
        return isPermittedRole(sessionInfo.getUserId(), roleCode);
    }

    /**
     * 判断某个用户是否授权某个角色
     *
     * @param userId   用户ID
     * @param roleCode 角色编码
     * @return
     */
    public static Boolean isPermittedRole(Long userId, String roleCode) {
        boolean flag = false;
        try {
            if (userId == null) {
            	SsmSessionInfo sessionInfo = getCurrentSessionInfo();
                if (sessionInfo != null) {
                    userId = sessionInfo.getUserId();
                }
            }
            if (userId == null) {
                throw new SsmSystemException("用户[" + userId + "]不存在.");
            }

            SsmUserManager userManager = SsmSpringContextHolder.getBean(SsmUserManager.class);
            SsmUser superUser = userManager.getSuperUser();
            if (userId != null && superUser != null
                    && userId.equals(superUser.getId())) {// 超级用户
                flag = true;
            } else {
            	SsmUser user = userManager.loadById(userId);
                List<SsmRole> userRoles = user.getRoles();
                for (SsmRole role : userRoles) {
                    if (roleCode.equalsIgnoreCase(role.getCode())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return flag;
    }

    /**
     * User转SessionInfo.
     *
     * @param user
     * @return
     */
    public static SsmSessionInfo userToSessionInfo(SsmUser user) {
    	SsmSessionInfo sessionInfo = new SsmSessionInfo();
        sessionInfo.setUserId(user.getId());
        sessionInfo.setName(user.getName());
        sessionInfo.setLoginName(user.getLoginName());
        sessionInfo.setRoleIds(user.getRoleIds());
        sessionInfo.setRoleNames(user.getRoleNames());
        sessionInfo.setLoginOrganId(user.getDefaultOrganId());
        sessionInfo.setLoginOrganSysCode(user.getDefaultOrganSysCode());
        sessionInfo.setLoginOrganName(user.getDefaultOrganName());
        sessionInfo.setOrganNames(user.getOrganNames());
        sessionInfo.setName(user.getName());
        return sessionInfo;
    }

    /**
     * 将用户放入session中.
     *
     * @param user
     */
    public static synchronized void putUserToSession(HttpServletRequest request, SsmUser user) {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        if(logger.isDebugEnabled()){
            logger.debug("putUserToSession:{}", sessionId);
        }
        SsmSessionInfo sessionInfo = userToSessionInfo(user);
        sessionInfo.setIp(SsmIpUtils.getIpAddr(request));
        sessionInfo.setId(sessionId);
        request.getSession().setAttribute(SsmSecurityConstants.SESSION_SESSIONINFO, sessionInfo);
        SsmSecurityConstants.sessionInfoMap.put(sessionId, sessionInfo);
    }

    /**
     * 获取当前用户session信息.
     */
    public static SsmSessionInfo getCurrentSessionInfo() {
    	SsmSessionInfo sessionInfo = null;
        try {
            sessionInfo = SsmSpringMVCHolder.getSessionAttribute(SsmSecurityConstants.SESSION_SESSIONINFO);
        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
        }
        return sessionInfo;
    }

    /**
     * 获取当前登录用户信息.
     */
    public static SsmUser getCurrentUser() {
    	SsmSessionInfo sessionInfo = getCurrentSessionInfo();
    	SsmUser user = null;
        if(sessionInfo != null){
            user = userManager.loadById(sessionInfo.getUserId());
        }
        return user;
    }

    /**
     * 根据用户ID获取用户对象
     * @param userId
     * @return
     */
    public static SsmUser getUserById(String userId) {
        Long uId = Long.valueOf(userId);
        SsmUser user = null;
        if(uId != null){
            user = userManager.loadById(uId);
        }
        return user;
    }

    /**
     * 将用户信息从session中移除
     *
     * @param sessionId session ID
     * @param saveLog   是否 保存切面日志
     */
    public static synchronized void removeUserFromSession(String sessionId, boolean saveLog,SsmSecurityType securityType) {
        if (SsmStringUtils.isNotBlank(sessionId)) {
            Set<String> keySet = SsmSecurityConstants.sessionInfoMap.keySet();
            for (String key : keySet) {
                if (key.equals(sessionId)) {
                    if(logger.isDebugEnabled()){
                        logger.debug("removeUserFromSession:{}", sessionId);
                    }
                    if (saveLog) {
                    	SsmSessionInfo sessionInfo = SsmSecurityConstants.sessionInfoMap.get(key);
                        securityLogAspect.saveLog(sessionInfo, null, securityType);
                    }
                    SsmSecurityConstants.sessionInfoMap.remove(key);
                }
            }
            HttpSession session = applicationSessionContext.getSession(sessionId);
            if(session != null){
                session.removeAttribute(SsmSecurityConstants.SESSION_SESSIONINFO);
                applicationSessionContext.removeSession(session);
            }
        }
    }

    /**
     * 查看当前登录用户信息
     * @return
     */
    public static SsmDatagrid<SsmSessionInfo> getSessionUser() {
        List<SsmSessionInfo> list = Lists.newArrayList();
        Set<String> keySet = SsmSecurityConstants.sessionInfoMap.keySet();
        for (String key : keySet) {
        	SsmSessionInfo sessionInfo = SsmSecurityConstants.sessionInfoMap.get(key);
            list.add(sessionInfo);
        }
        //排序
        Collections.sort(list, new Comparator<SsmSessionInfo>() {
            public int compare(SsmSessionInfo o1, SsmSessionInfo o2) {
                return o2.getLoginTime().compareTo(o1.getLoginTime());
            }
        });

        SsmDatagrid<SsmSessionInfo> dg = new SsmDatagrid<SsmSessionInfo>(SsmSecurityConstants.sessionInfoMap.size(), list);
        return dg;
    }


    /**
     * 查看某个用户登录信息
     * @param loginName 登录帐号
     * @return
     */
    public static List<SsmSessionInfo> getSessionUser(String loginName) {
        SsmDatagrid<SsmSessionInfo> datagrid = getSessionUser();
        List<SsmSessionInfo> sessionInfos = Lists.newArrayList();
        for(SsmSessionInfo sessionInfo: datagrid.getRows()){
            if(sessionInfo.getLoginName().equals(loginName)){
                sessionInfos.add(sessionInfo);
            }
        }
        return sessionInfos;
    }

    /**
     * 根据SessionId查找对应的SessionInfo信息
     * @param sessionId
     * @return
     */
    public static SsmSessionInfo getSessionInfo(String sessionId) {
        SsmDatagrid<SsmSessionInfo> datagrid = getSessionUser();
        for(SsmSessionInfo sessionInfo: datagrid.getRows()){
            if(sessionInfo.getId().equals(sessionId)){
                return sessionInfo;
            }
        }
        return null;
    }


    /**
     * 云盘管理员 超级管理 + 系统管理员 + 网盘管理员
     * @param userId 用户ID 如果为null,则为当前登录用户ID
     * @return
     */
    public static boolean isDiskAdmin(Long userId){
        Long _userId = userId;
        if(_userId == null){
        	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
            _userId = sessionInfo.getUserId();
        }

        boolean isAdmin = false;
        if (userManager.isSuperUser(_userId) || SsmSecurityUtils.isPermittedRole(SsmAppConstants.ROLE_SYSTEM_MANAGER) || SsmSecurityUtils.isPermittedRole(SsmAppConstants.ROLE_DISK_MANAGER)) {//系统管理员 + 网盘管理员
            isAdmin = true;
        }
        return isAdmin;
    }
}
