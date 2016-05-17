/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
* @author Jiang.Li
* @version 2016年2月27日 下午9:29:01
*/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SsmLogging {
	 /**
     * 记录日志 默认值：true
     * @return
     */
    boolean logging() default true;

    /**
     * 日志详细信息
     * <br/>支持占位符 表示第几个参数 例如："{0},{1},..."
     * @return
     */
    String value() default "";
}
