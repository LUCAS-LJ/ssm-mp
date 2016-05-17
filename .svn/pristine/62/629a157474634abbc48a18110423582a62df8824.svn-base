/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.jdbc;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ssm.pay.common.orm.SsmPageSqlUtils;

/** 
* @author Jiang.Li
* @version 2016年3月25日 下午2:43:23
*/
@Repository
@Transactional
public class SsmJdbcDao extends SsmSimpleJdbcDao{
	public SsmJdbcDao(){

    }

    @Autowired
    public SsmJdbcDao(@Qualifier("dataSource") DataSource dataSource) {
        super(dataSource);
    }


    /**
     * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式.<br/>
     * 支持MySQL、Oracle、postgresql、SQL Server分页查询. <br/>
     * 例如：
     * @param sql SQL语句
     * @param page 第几页
     * @param rows 页大小
     * @return
     */
    @Transactional(readOnly = true)
    public  List<Map<String, Object>>  queryForList(String  sql,  int page, int rows,Object... param){
        Assert.hasText(sql,"sql语句不正确!");
        //封装分页SQL
        sql = SsmPageSqlUtils.createPageSql(sql,page,rows);
        if(param == null){
            return jdbcTemplate.queryForList(sql);
        }
        return jdbcTemplate.queryForList(sql,param);
    }

}
