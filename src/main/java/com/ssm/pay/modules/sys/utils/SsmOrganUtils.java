/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.utils;

import java.util.List;
import com.ssm.pay.common.spring.SsmSpringContextHolder;
import com.ssm.pay.common.utils.SsmConvertUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.modules.sys.entity.SsmOrgan;
import com.ssm.pay.modules.sys.service.SsmOrganManager;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午11:23:14
*/
public class SsmOrganUtils {
	private static SsmOrganManager organManager = SsmSpringContextHolder.getBean(SsmOrganManager.class);

    /**
     * 根据机构ID查找机构名称
     * @param organId 机构ID
     * @return
     */
    public static String getOrganName(Long organId){
        if(organId != null){
        	SsmOrgan organ = organManager.loadById(organId);
            if(organ != null){
                return organ.getName();
            }
        }
        return null;
    }

    /**
     * 根据机构ID集合转换成机构名称
     * @param organIds 机构ID集合
     * @return
     */
    public static String getOrganNames(List<Long> organIds){
        if(SsmCollections3.isNotEmpty(organIds)){
            List<SsmOrgan> list = organManager.findOrgansByIds(organIds);
            return SsmConvertUtils.convertElementPropertyToString(list, "name", ", ");
        }
        return null;
    }
}
