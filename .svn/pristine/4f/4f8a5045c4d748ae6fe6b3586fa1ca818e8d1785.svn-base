/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/** easyui树形节点TreeNode模型.
* @author Jiang.Li
* @version 2016年1月29日 下午1:51:19
*/
@SuppressWarnings("serial")
public class SsmTreeNode implements Serializable{
	/**
	 * 静态变量 展开节点
	 */
	public static final String STATE_OPEN = "open";
	/**
	 * 静态变量 关闭节点
	 */
	public static final String STATE_CLOASED = "closed";

	/**
	 * 节点id
	 */
	private String id;
    /**
     * 父级节点id （用于zTree简单数据模型）
     */
    private String pId;
	/**
	 * 树节点名称
	 */
	private String text;
	/**
	 * 前面的小图标样式
	 */
	private String iconCls = "";
	/**
	 * 是否勾选状态（默认：否false）
	 */
	private Boolean checked = false;
	/**
	 * 自定义属性
	 */
	private Map<String, Object> attributes = Maps.newHashMap();
	/**
	 * 子节点
	 */
	private List<SsmTreeNode> children;
	/**
	 * 是否展开 (open,closed)-(默认值:open)
	 */
	private String state = STATE_OPEN;

	public SsmTreeNode() {
		this(null, null, "");
	}

	/**
	 * 
	 * @param id
	 *            节点id
	 * @param text
	 *            树节点名称
	 */
	public SsmTreeNode(String id, String text) {
		this(id, text, "");
	}

	/**
	 * 
	 * @param id
	 *            节点id
	 * @param text
	 *            树节点名称
	 * @param iconCls
	 *            图标样式
	 */
	public SsmTreeNode(String id, String text, String iconCls) {
		this(id, text, STATE_OPEN, iconCls);
	}

	/**
	 * 
	 * @param id
	 *            节点id
	 * @param text
	 *            树节点名称
	 * @param state
	 *            是否展开
	 * @param iconCls
	 *            图标样式
	 */
	public SsmTreeNode(String id, String text, String state, String iconCls) {
		this.id = id;
		this.text = text;
		this.state = state;
		this.iconCls = iconCls;
		this.children = Lists.newArrayList();
	}

	/**
	 * 
	 * @param id
	 *            节点id
	 * @param text
	 *            树节点名称
	 * @param iconCls
	 *            图标样式
	 * @param checked
	 *            是否勾选状态（默认：否）
	 * @param attributes
	 *            自定义属性
	 * @param children
	 *            子节点
	 * @param state
	 *            是否展开
	 */
	public SsmTreeNode(String id, String text, String iconCls, Boolean checked,
			Map<String, Object> attributes, List<SsmTreeNode> children,
			String state) {
		super();
		this.id = id;
		this.text = text;
		this.iconCls = iconCls;
		this.checked = checked;
		this.attributes = attributes;
		this.children = children;
		this.state = state;
	}

	/**
	 * 添加子节点.
	 * 
	 * @param childNode
	 *            子节点
	 */
	public SsmTreeNode addChild(SsmTreeNode childNode) {
		this.children.add(childNode);
        return this;
	}

	/**
	 * 节点id
	 */
	public String getId() {
		return id;
	}

	public SsmTreeNode setId(String id) {
		this.id = id;
        return this;
	}

    /**
     * 父级节点id （用于zTree简单数据模型）
     * @return
     */
    public String getpId() {
        return pId;
    }

    /**
     * @param pId 父级节点id （用于zTree简单数据模型）
     * @return
     */
    public SsmTreeNode setpId(String pId) {
        this.pId = pId;
        return this;
    }

    /**
	 * 树节点名称
	 */
	public String getText() {
		return text;
	}

	public SsmTreeNode setText(String text) {
		this.text = text;
        return this;
	}

	/**
	 * 是否勾选状态（默认：否）
	 */
	public Boolean getChecked() {
		return checked;
	}

	public SsmTreeNode setChecked(Boolean checked) {
		this.checked = checked;
        return this;
	}

	/**
	 * 自定义属性
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public SsmTreeNode setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
        return this;
	}

	/**
	 * 子节点
	 */
	public List<SsmTreeNode> getChildren() {
		return children;
	}

	public SsmTreeNode setChildren(List<SsmTreeNode> children) {
		this.children = children;
        return this;
	}

	/**
	 * 是否展开
	 */
	public String getState() {
		return state;
	}

	public SsmTreeNode setState(String state) {
		this.state = state;
        return this;
	}

	/**
	 * 图标样式
	 */
	public String getIconCls() {
		return iconCls;
	}

	public SsmTreeNode setIconCls(String iconCls) {
		this.iconCls = iconCls;
        return this;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
