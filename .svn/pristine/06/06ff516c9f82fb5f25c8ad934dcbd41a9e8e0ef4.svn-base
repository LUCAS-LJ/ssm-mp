/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.notice._enum;

/** 邮件重要性
* @author Jiang.Li
* @version 2016年4月15日 上午11:02:02
*/
public enum SsmIsTop {
	/**
     * 不置顶(0)
     */
    No(0, "不置顶"),
    /**
     * 置顶(1)
     */
    Yes(1, "置顶");

    /**
     * 值 Integer型
     */
    private final Integer value;
    /**
     * 描述 String型
     */
    private final String description;

    SsmIsTop(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 获取值
     *
     * @return value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * 获取描述信息
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    public static SsmIsTop getIsTop(Integer value) {
        if (null == value)
            return null;
        for (SsmIsTop _enum : SsmIsTop.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static SsmIsTop getIsTop(String description) {
        if (null == description)
            return null;
        for (SsmIsTop _enum : SsmIsTop.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
