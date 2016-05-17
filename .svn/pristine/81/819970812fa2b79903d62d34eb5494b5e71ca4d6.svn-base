/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.google.common.collect.Maps;

/** easyui树形节点Menu模型.
* @author Jiang.Li
* @version 2016年1月29日 下午1:59:10
*/
@SuppressWarnings("serial")
public class SsmMenu implements Serializable{
	/**
	 * 节点id
	 */
	private String id;
	/**
	 * 树节点名称
	 */
	private String text;
	/**
	 * 前面的小图标样式
	 */
	private String iconCls;
	/**
	 * URL
	 */
	private String href;
	/**
	 * 自定义属性
	 */
	private Map<String, Object> attributes = Maps.newHashMap();
	/**
	 * 子节点
	 */
	private List<SsmMenu> children = new ArrayList<SsmMenu>(0);


	/**
	 * 添加子节点.
	 * 
	 * @param childMenu
	 *            子节点
	 */
	public SsmMenu addChild(SsmMenu childMenu) {
		this.children.add(childMenu);
        return this;
	}

	/**
	 * 节点id
	 */
	public String getId() {
		return id;
	}

	public SsmMenu setId(String id) {
		this.id = id;
        return this;
	}

	/**
	 * 树节点名称
	 */
	public String getText() {
		return text;
	}

	public SsmMenu setText(String text) {
		this.text = text;
        return this;
	}

    public String getHref() {
        return href;
    }

    public SsmMenu setHref(String href) {
        this.href = href;
        return this;
    }

    /**
	 * 自定义属性
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public SsmMenu setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
        return this;
	}

	/**
	 * 子节点
	 */
	public List<SsmMenu> getChildren() {
		return children;
	}

	public SsmMenu setChildren(List<SsmMenu> children) {
		this.children = children;
        return this;
	}


	/**
	 * 图标样式
	 */
	public String getIconCls() {
		return iconCls;
	}

	public SsmMenu setIconCls(String iconCls) {
		this.iconCls = iconCls;
        return this;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
