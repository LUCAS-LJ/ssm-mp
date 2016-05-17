/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.ssm.pay.core.security.SsmSecurityUtils;

/** 判断是否具有某个角色编码权限
* @author Jiang.Li
* @version 2016年4月11日 下午8:17:48
*/
@SuppressWarnings("serial")
public class SsmHasRoleTag extends TagSupport {
	/**
     * 角色编码访问权限字符串 多个以";"分割
     */
    private String name = "";

    @Override
    public int doStartTag() throws JspException {
        if (!"".equals(this.name)) {
            String[] permissonNames = name.split(";");
            for (int i = 0; i < permissonNames.length; i++) {
                if (SsmSecurityUtils.isPermittedRole(permissonNames[i])) {
                    return TagSupport.EVAL_BODY_INCLUDE;
                }
            }

        }
        return SKIP_BODY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
