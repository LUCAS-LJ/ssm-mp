/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.excelTools;

import java.text.DecimalFormat;
/** 
* @author Jiang.Li
* @version 2016年4月15日 上午9:57:46
*/
public class SsmTableDataCell {
	private String value;

	private int intValue;

	private double doubleValue;

	private SsmTableDataRow row;

	private int columnIndex;

	private static DecimalFormat format2 = new DecimalFormat("0.##");

	private static DecimalFormat format3 = new DecimalFormat("0.###");

	private int cellStyle = SsmTableData.STYLE_TYPE_STRING;

	public SsmTableDataCell(SsmTableDataRow row) {
		this(-1, row);
	}

	public SsmTableDataCell(int index, SsmTableDataRow row) {
		this.row = row;
		if (index == -1) {
			index = row.getCells().size();
		} else {
			this.columnIndex = index;
		}
		value = "";
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setValue(int value) {
		this.value = String.valueOf(value);
		this.intValue = value;
	}

	public void setValue(double value) {
		int type = row.getTable().getTableHeader().getColumnAt(columnIndex)
				.getColumnType();
		if (type == SsmTableColumn.COLUMN_TYPE_FLOAT_2) {
			this.value = format2.format(value);
		} else if (type == SsmTableColumn.COLUMN_TYPE_FLOAT_3) {
			this.value = format3.format(value);
		}
		this.doubleValue = value;
	}

	public String getValue() {
		return value;
	}

	public SsmTableDataRow getRow() {
		return row;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public int getIntValue() {
		return intValue;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setCellStyle(int cellStyle) {
		this.cellStyle = cellStyle;
	}

	public int getCellStyle() {
		return cellStyle;
	}
}
