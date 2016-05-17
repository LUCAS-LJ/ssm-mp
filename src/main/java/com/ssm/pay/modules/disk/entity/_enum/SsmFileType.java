/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.entity._enum;

import com.ssm.pay.common.utils.SsmStringUtils;

/** 
* @author Jiang.Li
* @version 2016年4月13日 下午8:13:06
*/
public enum SsmFileType {
	 /**
     * 文档(0)
     */
    Document(0, "文档"),
    /**
     * 图片(1)
     */
    Image(1, "图片"),
    /**
     * 媒体(2)
     */
    Music(2, "音乐"),
    /**
     * 媒体(2)
     */
    Video(2, "视频"),
    /**
     * 软件(3)
     */
    Soft(3, "软件"),
    /**
     * 其它(4)
     */
    Other(4, "其它");

    /**
     * 值 Integer型
     */
    private final Integer value;
    /**
     * 描述 String型
     */
    private final String description;

    SsmFileType(Integer value, String description) {
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

    public static SsmFileType getFileType(Integer value) {
        if (null == value)
            return null;
        for (SsmFileType _enum : SsmFileType.values()) {
            if (value.equals(_enum.getValue()))
                return _enum;
        }
        return null;
    }

    public static SsmFileType getFileType(String description) {
        if (null == description)
            return null;
        for (SsmFileType _enum : SsmFileType.values()) {
            if (description.equals(_enum.getDescription()))
                return _enum;
        }
        return null;
    }

    /**
     * 根据文件后缀名返回文件分类
     * @param fileSuffix
     * @return
     */
    public static SsmFileType getFileTypeByFileSuffix(String fileSuffix) {
    	SsmFileType fileType = SsmFileType.Other;
        if (SsmStringUtils.isNotBlank(fileSuffix)){
            //TODO
        }
        return fileType;
    }
}
