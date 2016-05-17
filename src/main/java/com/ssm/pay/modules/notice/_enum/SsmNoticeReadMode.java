/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.notice._enum;

/** 通知公告
* @author Jiang.Li
* @version 2016年4月15日 上午11:18:28
*/
public enum SsmNoticeReadMode {
	/**
     * 未读(0)
     */
    unreaded(0, "未读"),
    /**
     * 已读(1)
     */
    readed(1, "已读"),
    /**
     * 未知(2)
     */
    unknow(2, "未知");


    /**
     * 值 Integer型
     */
    private final Integer value;
    /**
     * 描述 String型
     */
    private final String description;

    SsmNoticeReadMode(Integer value, String description) {
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

    public static SsmNoticeReadMode getNoticeReadMode(Integer value) {
        if (null == value)
            return null;
        for (SsmNoticeReadMode _enum : SsmNoticeReadMode.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static SsmNoticeReadMode getNoticeReadMode(String description) {
        if (null == description)
            return null;
        for (SsmNoticeReadMode _enum : SsmNoticeReadMode.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
