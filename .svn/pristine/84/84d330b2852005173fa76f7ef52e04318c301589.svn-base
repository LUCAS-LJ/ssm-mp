/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.security.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.ssm.pay.core.security._name.SsmLogical;
import static com.ssm.pay.core.security._name.SsmLogical.AND;

/** 需要的权限
* @author Jiang.Li
* @version 2016年2月27日 下午11:10:01
*/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SsmRequiresPermissions {
	String[] value();

    SsmLogical logical() default AND;
}
