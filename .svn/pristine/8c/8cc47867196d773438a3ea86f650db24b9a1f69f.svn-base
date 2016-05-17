/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.excelTools;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午9:56:43
*/
public class SsmTableDataRow {
	private LinkedList<SsmTableDataCell> cells;

	private SsmTableData table;

	private int rowStyle = SsmTableData.STYLE_TYPE_STRING;

	public void addCell(SsmTableDataCell cell) {
		cells.add(cell);
	}

	public void addCell(String value) {
		SsmTableDataCell cell = new SsmTableDataCell(this);
		cell.setValue(value);
		cell.setCellStyle(rowStyle);
		addCell(cell);
	}

	public void addCell(Integer value) {
		SsmTableDataCell cell = new SsmTableDataCell(this);
		cell.setValue(value);
		cell.setCellStyle(rowStyle);
		addCell(cell);
	}

	public void addCell(Double value) {
		SsmTableDataCell cell = new SsmTableDataCell(this);
		cell.setValue(value);
		cell.setCellStyle(rowStyle);
		addCell(cell);
	}

	public void addCell(Object value) {
		if (value instanceof String) {
			addCell((String) value);
		} else if (value instanceof Integer) {
			addCell((Integer) value);
		} else if (value instanceof Double) {
			addCell((Double) value);
		} else if (value instanceof BigDecimal) {
			addCell(value.toString());
		} else if (value instanceof Long) {
			addCell(value.toString());
		} else if(value == null){
			addCell("");
		}
	}

	public SsmTableDataCell getCellAt(int index) {
		return cells.get(index);
	}

	public List<SsmTableDataCell> getCells() {
		return cells;
	}

	public SsmTableData getTable() {
		return table;
	}

	public SsmTableDataRow(SsmTableData table) {
		cells = new LinkedList<SsmTableDataCell>();
		this.table = table;
	}

	public void setRowStyle(int rowStyle) {
		this.rowStyle = rowStyle;
	}

	public int getRowStyle() {
		return rowStyle;
	}
}
