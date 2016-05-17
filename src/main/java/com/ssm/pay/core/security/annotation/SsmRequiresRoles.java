/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.ssm.pay.core.security._name.SsmLogical;

import static com.ssm.pay.core.security._name.SsmLogical.AND;

/** 
* 需要的角色
* @author Jiang.Li
* @version 2016年2月27日 下午11:05:12
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SsmRequiresRoles {
	String[] value() default {};

    public SsmLogical logical() default AND;
}
