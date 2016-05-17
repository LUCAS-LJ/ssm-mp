/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/** 
 * easyui分页组件datagrid、combogrid数据模型.
* @author Jiang.Li
* @version 2016年1月28日 下午11:40:13
*/
@SuppressWarnings("serial")
public class SsmDatagrid<T> implements Serializable{
	 /**
     * 总记录数
     */
    private long total;
    /**
     * 动态列
     */
    private List<SsmColumn> columns = new ArrayList<SsmColumn>(0);
    /**
     * 列表行
     */
    private List<T> rows = new ArrayList<T>(0);
    /**
     * 脚列表
     */
    private List<Map<String, Object>> footer = new ArrayList<Map<String, Object>>(0);

    public SsmDatagrid() {
        super();
    }

    /**
     * @param total 总记录数
     * @param rows  列表行
     */
    public SsmDatagrid(long total, List<T> rows) {
        this(total, rows, null, null);
    }

    /**
     * @param total   总记录数
     * @param rows    列表行
     * @param footer  脚列表
     * @param columns 动态列
     */
    public SsmDatagrid(long total, List<T> rows, List<Map<String, Object>> footer,
                    List<SsmColumn> columns) {
        super();
        this.total = total;
        this.rows = rows;
        this.footer = footer;
        this.columns = columns;
    }

    /**
     * 总记录数
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置总记录数
     *
     * @param total 总记录数
     */
    public SsmDatagrid<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    /**
     * 列表行
     */
    public List<T> getRows() {
        return rows;
    }

    /**
     * 设置列表行
     *
     * @param rows 列表行
     */
    public SsmDatagrid<T> setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    /**
     * 脚列表
     */
    public List<Map<String, Object>> getFooter() {
        return footer;
    }

    /**
     * 设置脚列表
     *
     * @param footer 脚列表
     */
    public SsmDatagrid<T> setFooter(List<Map<String, Object>> footer) {
        this.footer = footer;
        return this;
    }

    /**
     * 动态列
     */
    public List<SsmColumn> getColumns() {
        return columns;
    }

    /**
     * 设置动态列
     *
     * @param columns 动态列
     */
    public SsmDatagrid<T> setColumns(List<SsmColumn> columns) {
        this.columns = columns;
        return this;
    }

    /**
     * 添加动态列表
     *
     * @param column 动态列
     * @return
     */
    public SsmDatagrid<T> addColumn(SsmColumn column) {
        if (this.columns == null) {
            this.columns = Lists.newArrayList();
        }
        this.columns.add(column);
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
