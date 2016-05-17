/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.excelTools;

import java.util.LinkedList;
import java.util.List;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午9:53:55
*/
public class SsmTableHeaderMetaData {
	private LinkedList<SsmTableColumn> columns;

	private LinkedList<SsmTableColumn> leafs;

	private String common;

	public int maxlevel = 0;

	public SsmTableHeaderMetaData() {
		columns = new LinkedList<SsmTableColumn>();
		leafs = new LinkedList<SsmTableColumn>();
	}

	public void addColumn(SsmTableColumn col) {
		setLevel(col, 1);
		columns.add(col);
		addLeafColumn(col);
	}

	public void refresh() {
		maxlevel = 1;
		for (SsmTableColumn col : columns) {
			if (col.isVisible()) {
				col.level = 1;
				int level = refreshChildren(col);
				if (level > maxlevel)
					maxlevel = level;
			}
		}
	}

	private int refreshChildren(SsmTableColumn parent) {
		if (parent.children.size() != 0) {
			int max = parent.level;
			for (SsmTableColumn col : parent.children) {
				if (col.isVisible()) {
					col.parent = parent;
					col.level = parent.level + 1;
					int level = refreshChildren(col);
					if (level > max)
						max = level;
				}
			}
			return max;
		} else {
			return parent.level;
		}
	}

	private void setLevel(SsmTableColumn col, int level) {
		col.level = level;
		if (col.isVisible() && level > maxlevel)
			maxlevel = level;
	}

	private void addLeafColumn(SsmTableColumn col) {
		if (col.parent != null)
			setLevel(col, col.parent.level + 1);
		if (col.isComplex()) {
			for (SsmTableColumn c : col.getChildren()) {
				addLeafColumn(c);
			}
		} else {
			leafs.add(col);
		}
	}

	public List<SsmTableColumn> getColumns() {
		return leafs;
	}

	public List<SsmTableColumn> getOriginColumns() {
		LinkedList<SsmTableColumn> ret = new LinkedList<SsmTableColumn>();
		for (SsmTableColumn c : columns) {
			if (c.isVisible())
				ret.add(c);
		}
		return ret;
	}

	public SsmTableColumn getColumnAt(int index) {
		return leafs.get(index);
	}

	public int getColumnCount() {
		return leafs.size();
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}
}
