/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/** easyui可装载组合框combobox模型.
* @author Jiang.Li
* @version 2016年4月11日 下午7:51:44
*/
public class SsmCombobox implements Serializable{
	/**
	 * 值域
	 */
	private String value;
	/**
	 * 文本域
	 */
	private String text;
    /**
     * 分组
     */
    private String group;
	/**
	 * 是否选中
	 */
	private boolean selected;

	public SsmCombobox() {
		super();
	}

    /**
     *
     * @param value
     *            值域
     * @param text
     *            文本域
     */
    public SsmCombobox(String value, String text) {
        super();
        this.value = value;
        this.text = text;
    }

	/**
	 * 
	 * @param value
	 *            值域
	 * @param text
	 *            文本域
     * @param group
     *            分组
	 */
	public SsmCombobox(String value, String text, String group) {
		super();
		this.value = value;
		this.text = text;
        this.group = group;
	}

	/**
	 * 
	 * @param value
	 *            值域
	 * @param text
	 *            文本域
	 * @param group
	 *            分组
     * @param selected
     *            是否选中
	 */
	public SsmCombobox(String value, String text, String group, boolean selected) {
		super();
		this.value = value;
		this.text = text;
        this.group = group;
		this.selected = selected;
	}

	/**
	 * 值域
	 */
	public String getValue() {
		return value;
	}
    /**
     * 设置值域
     * @param value   文本域
     */
	public SsmCombobox setValue(String value) {
		this.value = value;
        return this;
	}

	/**
	 * 文本域
	 */
	public String getText() {
		return text;
	}

    /**
     * 设置文本域
     * @param text   文本域
     */
	public SsmCombobox setText(String text) {
		this.text = text;
        return this;
	}


    /**
     * 分组
     * @return
     */
    public String getGroup() {
        return group;
    }

    /**
     * 设置分组
     * @param group
     */
    public SsmCombobox setGroup(String group) {
        this.group = group;
        return this;
    }

    /**
	 * 是否选中
	 */
	public boolean isSelected() {
		return selected;
	}

	public SsmCombobox setSelected(boolean selected) {
		this.selected = selected;
        return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
