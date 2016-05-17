/*@版权所有LIJIANG*/ 
package com.ssm.pay.utils;

import org.apache.commons.lang3.StringUtils;

import com.ssm.pay.common.model.SsmCombobox;
import com.ssm.pay.common.model.SsmTreeNode;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午9:49:11
*/
public enum SsmSelectType {
	/** 全部("all") */
	all("all", "全部"),
	/** 请选择("select") */
	select("select", "请选择...");

	/**
	 * 值 String型
	 */
	private final String value;
	/**
	 * 描述 String型
	 */
	private final String description;

	SsmSelectType(String value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * 获取值
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
     * 获取描述信息
     * @return description
     */
	public String getDescription() {
		return description;
	}

	public static SsmSelectType getSelectTypeValue(String value) {
		if (null == value)
			return null;
		for (SsmSelectType _enum : SsmSelectType.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}
	
	public static SsmSelectType getSelectTypeDescription(String description) {
		if (null == description)
			return null;
		for (SsmSelectType _enum : SsmSelectType.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}


    /**
     * 快速构造一个树形节点
     * @param selectType
     * @return
     */
    public static SsmTreeNode treeNode(String selectType){
    	SsmTreeNode selectTreeNode = null;
        //为combobox添加  "---全部---"、"---请选择---"
        if(StringUtils.isNotBlank(selectType)){
        	SsmSelectType s = SsmSelectType.getSelectTypeValue(selectType);
            String title = selectType;
            if(s != null){
                title = s.getDescription();
            }
            selectTreeNode = new SsmTreeNode("", title);
        }
        return selectTreeNode;
    }

    /**
     * 快速构造一个选项节点
     * @param selectType
     * @return
     */
    public static SsmCombobox combobox(String selectType){
    	SsmCombobox selectCombobox = null;
        //为combobox添加  "---全部---"、"---请选择---"
        if(StringUtils.isNotBlank(selectType)){
        	SsmSelectType s = SsmSelectType.getSelectTypeValue(selectType);
            String title = selectType;
            if(s != null){
                title = s.getDescription();
            }
            selectCombobox = new SsmCombobox("", title);
        }
        return selectCombobox;
    }
}
