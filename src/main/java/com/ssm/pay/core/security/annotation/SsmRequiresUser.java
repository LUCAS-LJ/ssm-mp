/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 用户登录验证注解
* @author Jiang.Li
* @version 2016年1月29日 下午3:22:02
*/
@Target({ElementType.TYPE, ElementType.METHOD,ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SsmRequiresUser {
    /**
     * 用户是否需要登录 是：true 否：false 默认值：true
     * @return
     */
    boolean required() default true;
}
