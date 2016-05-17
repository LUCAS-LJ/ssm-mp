/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.entity._enum;

/** 文件夹授权
* @author Jiang.Li
* @version 2016年4月13日 下午8:01:14
*/
public enum SsmFolderAuthorize {
	/**
     * 个人(0)
     */
    User(0, "我的云盘"),
    /**
     * 系统(1)
     */
    SysTem(1, "系统云盘"),
    /**
     * 部门(2)
     */
    Organ(2, "部门云盘"),
    /**
     * 角色(3)
     */
    Role(3, "角色云盘"),
    /**
     * 公开(4)
     */
    Pulic(4, "公共云盘");

    /**
     * 值 Integer型
     */
    private final Integer value;
    /**
     * 描述 String型
     */
    private final String description;

    SsmFolderAuthorize(Integer value, String description) {
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

    public static SsmFolderAuthorize getFolderAuthorize(Integer value) {
        if (null == value)
            return null;
        for (SsmFolderAuthorize _enum : SsmFolderAuthorize.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static SsmFolderAuthorize getFolderAuthorize(String description) {
        if (null == description)
            return null;
        for (SsmFolderAuthorize _enum : SsmFolderAuthorize.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }
}
