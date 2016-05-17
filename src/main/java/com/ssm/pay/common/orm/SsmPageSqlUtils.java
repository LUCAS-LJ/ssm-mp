/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm;

import java.text.MessageFormat;
import com.ssm.pay.common.utils.SsmSysConstants;

/** 
 * 分页SQL工具类.
 * <br>支持MySQL、Oracle、Postgresql分页查询.
* @author Jiang.Li
* @version 2016年3月25日 下午2:47:54
*/
public class SsmPageSqlUtils {
	/**
     * 数据库类型
     */
    public static final String DATABSE_TYPE_MYSQL ="mysql";
    public static final String DATABSE_TYPE_POSTGRE ="postgresql";
    public static final String DATABSE_TYPE_ORACLE ="oracle";
    public static final String DATABSE_TYPE_SQLSERVER ="sqlserver";
    /**
     * 分页SQL
     */
    public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}";         //mysql
    public static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";//postgresql
    public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}"; //oracle
    public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; //sqlserver

    /**
	 * 按照数据库类型，封装SQL（根据配置文件自动识别）.
	 * <br>支持MySQL、Oracle、Postgresql分页查询.
	 * @param sql 查询语句 例如:"select * from user "
	 * @param page 第几页
	 * @param rows 页大小
	 * @return
	 */
	public static String createPageSql(String sql, int page, int rows) {
		int beginNum = (page - 1) * rows;
		String[] sqlParam = new String[3];
		sqlParam[0] = sql;
		sqlParam[1] = beginNum + "";
		sqlParam[2] = rows + "";
        String jdbcUrl = SsmSysConstants.getJdbcUrl();
		if (jdbcUrl.indexOf(SsmPageSqlUtils.DATABSE_TYPE_MYSQL) != -1) {
			sql = MessageFormat.format(SsmPageSqlUtils.MYSQL_SQL, new Object[]{sqlParam});
		} else if (jdbcUrl.indexOf(
				SsmPageSqlUtils.DATABSE_TYPE_POSTGRE) != -1) {
			sql = MessageFormat.format(SsmPageSqlUtils.POSTGRE_SQL, new Object[]{sqlParam});
		} else {
            int beginIndex = (page-1)*rows;
            int endIndex = beginIndex+rows;
            sqlParam[2] = Integer.toString(beginIndex);
            sqlParam[1] = Integer.toString(endIndex);
            if(jdbcUrl.indexOf(DATABSE_TYPE_ORACLE)!=-1) {
                sql = MessageFormat.format(ORACLE_SQL, new Object[]{sqlParam});
            } else if(jdbcUrl.indexOf(DATABSE_TYPE_SQLSERVER)!=-1) {
                sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
                sql = MessageFormat.format(SQLSERVER_SQL, new Object[]{sqlParam});
            }
		}
		return sql;
	}

    private static int getAfterSelectInsertPoint(String sql) {
        int selectIndex = sql.toLowerCase().indexOf("select");
        int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
        return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }

    public static void main(String[] args) {

    }
}
