/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.notice.utils;

import com.ssm.pay.common.spring.SsmSpringContextHolder;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.modules.notice.entity.SsmNotice;
import com.ssm.pay.modules.notice.service.SsmNoticeManager;
import com.ssm.pay.modules.notice.service.SsmNoticeScopeManager;
import com.ssm.pay.modules.sys.service.SsmUserManager;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午11:03:29
*/
public class SsmNoticeUtils {
	public static final String DIC_NOTICE = "notice";
    public static final String ROLE_SYSTEM_MANAGER = "system_manager";
    public static final String ROLE_NOTICE_MANAGER = "notice_manager";

    public static final String MSG_REPEAT = "转发：";

    private SsmNoticeUtils(){

    }

    private static SsmNoticeManager noticeManager = SsmSpringContextHolder.getBean(SsmNoticeManager.class);
    private static SsmNoticeScopeManager noticeScopeManager = SsmSpringContextHolder.getBean(SsmNoticeScopeManager.class);
    private static SsmUserManager userManager = SsmSpringContextHolder.getBean(SsmUserManager.class);


    /**
     * 根据ID查找
     * @param noticeId
     * @return
     */
    public static SsmNotice getNotice(Long noticeId) {
        return noticeManager.loadById(noticeId);
    }

    /**
     * 判断当前登录用户是否读取通知
     * @param noticeId 通知ID
     * @return
     */
    public static boolean isRead(Long noticeId) {
        return noticeScopeManager.isRead(SsmSecurityUtils.getCurrentSessionInfo().getUserId(), noticeId);
    }

    /**
     * 通知管理员 超级管理 + 系统管理员 + 通知管理员
     * @param userId 用户ID 如果为null,则为当前登录用户ID
     * @return
     */
    public static boolean isNoticeAdmin(Long userId){
        Long _userId = userId;
        if(_userId == null){
        	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
            _userId = sessionInfo.getUserId();
        }

        boolean isAdmin = false;
        if (userManager.isSuperUser(_userId) || SsmSecurityUtils.isPermittedRole(ROLE_SYSTEM_MANAGER)
                || SsmSecurityUtils.isPermittedRole(ROLE_NOTICE_MANAGER)) {//系统管理员 + 通知管理员
            isAdmin = true;
        }
        return isAdmin;
    }
}
