/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.utils;

import java.util.List;
import org.apache.commons.lang3.StringUtils;

import com.ssm.pay.common.spring.SsmSpringContextHolder;
import com.ssm.pay.modules.sys.entity.SsmDictionary;
import com.ssm.pay.modules.sys.service.SsmDictionaryManager;

/** 
* 数据字典工具类 
* @author Jiang.Li
* @version 2016年4月11日 下午7:34:00
*/
public class SsmDictionaryUtils {
	private static SsmDictionaryManager dictionaryManager = SsmSpringContextHolder.getBean(SsmDictionaryManager.class);

    public static final String DIC_BUG = "bug";

    /**
     * 根据字典逼编码得到字典名称
     * @param dictionaryCode 字典编码
     * @return
     */
    @Deprecated
    public static String getDictionaryNameByCode(String dictionaryCode){
    	SsmDictionary dictionary = dictionaryManager.getByCode(dictionaryCode);
        if(dictionary != null){
            return dictionary.getName();
        }
        return null;
    }

    /**
     *
     * @param dictionaryTypeCode 字典类型编码
     * @param dictionaryCode 字典项编码
     * @param defaultValue 默认数据项值
     * @return
     */
    public static String getDictionaryNameByDC(String dictionaryTypeCode,String dictionaryCode, String defaultValue){
    	SsmDictionary dictionary = dictionaryManager.getDictionaryByDC(dictionaryTypeCode,dictionaryCode);
        if(dictionary != null){
            return dictionary.getName();
        }
        return defaultValue;
    }

    /**
     *
     * @param dictionaryTypeCode 字典类型编码
     * @param dictionaryValue 字典项值
     * @param defaultValue 默认数据项值
     * @return
     */
    public static String getDictionaryNameByDV(String dictionaryTypeCode,String dictionaryValue, String defaultValue){
    	SsmDictionary dictionary = dictionaryManager.getDictionaryByDV(dictionaryTypeCode,dictionaryValue);
        if(dictionary != null){
            defaultValue = dictionary.getName();
        }
        return defaultValue;
    }

    /**
     * 根据字典类型编码获取字典项列表
     * @param dictionTypeCode 类型编码
     * @return
     */
    public static List<SsmDictionary> getDictList(String dictionTypeCode){
        return dictionaryManager.getDictionarysByDictionaryTypeCode(dictionTypeCode);
    }


    /**
     * 获取字典对应的值
     * @param dictionName 字典项显示名称
     * @param dictionTypeCode 类型编码
     * @param defaultName 默认显示名称
     * @return
     */
    public static String getDictionaryValue(String dictionName, String dictionTypeCode, String defaultName){
        if (StringUtils.isNotBlank(dictionTypeCode) && StringUtils.isNotBlank(dictionName)){
            for (SsmDictionary dict : getDictList(dictionTypeCode)){
                if (dictionName.equals(dict.getName())){
                    return dict.getValue();
                }
            }
        }
        return defaultName;
    }
}
