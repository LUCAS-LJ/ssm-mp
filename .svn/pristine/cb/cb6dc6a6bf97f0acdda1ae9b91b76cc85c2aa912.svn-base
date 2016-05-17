/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.excelTools;

import java.util.LinkedList;
import java.util.List;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午9:52:46
*/
public class SsmTableData {
	/**
	 * 字符串型
	 */
	public static final int STYLE_TYPE_STRING = 0;

	/**
	 * 浮点型，保留2位小数
	 */
	public static final int STYLE_TYPE_FLOAT_2 = 1;

	/**
	 * 浮点型，保留3位小数
	 */
	public static final int STYLE_TYPE_FLOAT_3 = 2;

	/**
	 * 整形
	 */
	public static final int STYLE_TYPE_INTEGER = 3;

	/**
	 * 红色背景
	 */
	public static final int STYLE_TYPE_RED_BG = 10;

	/**
	 * 黄色背景
	 */
	public static final int STYLE_TYPE_YELLOW_BG = 11;

	/**
	 * 绿色背景
	 */
	public static final int STYLE_TYPE_GREEN_BG = 12;

	private String sheetTitle;
	
	private SsmTableHeaderMetaData header;

	private LinkedList<SsmTableDataRow> rows;

	private int totalRows;
	
	public SsmTableData(){}

	public SsmTableData(SsmTableHeaderMetaData header) {
		this.header = header;
		rows = new LinkedList<SsmTableDataRow>();
	}

	public SsmTableHeaderMetaData getTableHeader() {
		return header;
	}

	public void addRow(SsmTableDataRow row) {
		rows.add(row);
	}

	public List<SsmTableDataRow> getRows() {
		return rows;
	}

	public SsmTableDataRow getRowAt(int index) {
		return rows.get(index);
	}

	public int getRowCount() {
		return rows.size();
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public void setHeader(SsmTableHeaderMetaData header) {
		this.header = header;
	}

	public void setRows(LinkedList<SsmTableDataRow> rows) {
		this.rows = rows;
	}

	public String getSheetTitle() {
		return sheetTitle;
	}

	public void setSheetTitle(String sheetTitle) {
		this.sheetTitle = sheetTitle;
	}

}
