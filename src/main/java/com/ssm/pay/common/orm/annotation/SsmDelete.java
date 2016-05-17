/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.ssm.pay.common.orm.SsmPropertyType;

import static com.ssm.pay.common.orm.SsmPropertyType.I;
/** 
 * Hibernate状态删除，如果在orm实体配置该注解，将不会物理删除数据，会根据该配置来进行对orm实体的update操作
 * 
 * <pre>
 * &#064;Entity
 * &#064;Table(name="tb_user")
 * &#064;Delete(propertyName = "state",type = CategoryType.S,value="1")
 * public class User{
 * 	private String username;
 * 	private String state;
 * 
 * 	public User() {
 * 	}
 * 	getter/setter.....
 * }
 * User user = dao.get(1);
 * dao.delete(user);
 * ----------------------------------
 * sql:update tb_user set state = ? where id = ?
 * </pre>
* @author Jiang.Li
* @version 2016年1月29日 上午10:58:55
*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SsmDelete {
	
	/**
	 * 属性名称
	 * 
	 * @return String
	 */
	public String propertyName();

	/**
	 * 要改变的值
	 * <br> 默认值:"1" {@link com.ssm.pay.common.orm.entity.StatusState}
	 * 
	 * @return String
	 */
	public String value() default "1";

	/**
	 * 改变值的类型
	 * <br>默认值:Integer
	 * @return {@link PropertyType}
	 */
	public SsmPropertyType type() default I;

}
