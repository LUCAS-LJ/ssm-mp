/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.ssm.pay.common.excel.annotation.SsmExcel;

/**
* 统一定义entity基类. <br>
* 基类统一定义id的属性名称、数据类型、列名映射及生成策略. <br>
* 子类可重载getId()函数重定义id的列名映射和生成策略. <br>
* 2012-12-15 wencp:新加并发控制(乐观锁,用于并发控制)、数据更新时间、操作用户ID.  
* @author Jiang.Li
* @version 2016年1月28日 下午11:08:46
*/
@MappedSuperclass
public class SsmAutoEntity extends SsmAbstractEntity<Long>{

	/**
	 * 主键ID
	 */
	@SsmExcel(exportName="编号", exportFieldWidth = 10)
	protected Long id;

	public SsmAutoEntity() {
	}

	/**
	 * 主键ID 根据数据库主键自增长策略 依赖于数据库(SQL Serveer、MySQL数据库使用)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * 设置 主键ID
	 * 
	 * @param id
	 *            主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
