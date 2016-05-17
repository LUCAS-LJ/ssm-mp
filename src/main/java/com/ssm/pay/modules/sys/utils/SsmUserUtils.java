/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.utils;

import java.util.List;
import com.ssm.pay.common.spring.SsmSpringContextHolder;
import com.ssm.pay.common.utils.SsmConvertUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.modules.sys.service.SsmUserManager;

/** 用户工具类
* @author Jiang.Li
* @version 2016年4月13日 下午8:03:37
*/
public class SsmUserUtils {
	 private static SsmUserManager userManager = SsmSpringContextHolder.getBean(SsmUserManager.class);

	    /**
	     * 根据userId查找用户姓名
	     * @param userId 用户ID
	     * @return
	     */
	    public static String getUserName(Long userId){
	        if(userId != null){
	        	SsmUser user = userManager.loadById(userId);
	            if(user != null){
	                return user.getName();
	            }
	        }
	        return null;
	    }

	    /**
	     * 根据userId查找用户姓名
	     * @param userIds 用户ID集合
	     * @return
	     */
	    public static String getUserNames(List<Long> userIds){
	        if(SsmCollections3.isNotEmpty(userIds)){
	            List<SsmUser> list = userManager.findUsersByIds(userIds);
	            return SsmConvertUtils.convertElementPropertyToString(list, "name", ", ");
	        }
	        return null;
	    }
}
