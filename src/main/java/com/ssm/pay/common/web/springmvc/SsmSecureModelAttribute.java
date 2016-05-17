/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.web.springmvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
* @author Jiang.Li
* @version 2016年2月27日 下午10:19:50
*/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SsmSecureModelAttribute {
	String value() default "";

    String[] allowedField() default "*";

    String[] deniedField() default "";

    String[] clearFiled() default "";
}
