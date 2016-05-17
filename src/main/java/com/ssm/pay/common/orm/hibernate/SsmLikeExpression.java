/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.hibernate;

import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.MatchMode;

/** QBC like查询表达式.
* @author Jiang.Li
* @version 2016年1月29日 上午11:30:38
*/
@SuppressWarnings("serial")
public class SsmLikeExpression extends LikeExpression{
	
	protected SsmLikeExpression(String propertyName, String value,
			MatchMode matchMode, Character escapeChar, boolean ignoreCase) {
		super(propertyName, value, matchMode, escapeChar, ignoreCase);
	}
}
