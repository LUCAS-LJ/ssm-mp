/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssm.pay.common.model.SsmDatagrid;
import com.ssm.pay.common.model.SsmResult;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.utils.SsmIpUtils;
import com.ssm.pay.common.utils.encode.SsmEncrypt;
import com.ssm.pay.common.web.springmvc.SsmSimpleController;
import com.ssm.pay.core.security.SsmSecurityConstants;
import com.ssm.pay.core.security.SsmSecurityType;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.core.security.annotation.SsmRequiresUser;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.modules.sys.service.SsmUserManager;
import com.ssm.pay.utils.SsmAppConstants;

/** 
* 用户登录/注销等前端交互入口
* @author Jiang.Li
* @version 2016年1月29日 下午12:51:08
*/
@Controller
@RequestMapping(value = "${adminPath}/login")
public class SsmLoginController extends SsmSimpleController{
	@Autowired
    private SsmUserManager userManager;


    /**
     * 登录验证
     *
     * @param loginName 用户名
     * @param password  密码
     * @param theme     主题
     * @param request
     * @return
     */
	@SsmRequiresUser(required = false)
    @ResponseBody
    @RequestMapping(value = {"login"})
    public SsmResult login(@RequestParam(required = true) String loginName, @RequestParam(required = true) String password,
                        String theme, HttpServletRequest request) {
        SsmResult result = null;
        String msg = null;
        // 获取用户信息
        SsmUser user = userManager.getUserByLNP(loginName, SsmEncrypt.e(password));
        if (user == null) {
            msg = "用户名或密码不正确!";
        } else if (user.getStatus().intValue() == SsmStatusState.lock.getValue()) {
            msg = "该用户已被锁定，暂不允许登陆!";
        }
        if (msg != null) {
            result = new SsmResult(SsmResult.ERROR, msg, null);
        } else {
            if(SsmAppConstants.getIsSecurityOn()){
                List<SsmSessionInfo> userSessionInfos = SsmSecurityUtils.getSessionUser(loginName);
                if(SsmAppConstants.getUserSessionSize() > 0 &&  userSessionInfos.size() >= SsmAppConstants.getUserSessionSize() ){
                    result = new SsmResult(SsmResult.ERROR, "已达到用户最大会话登录限制["+SsmAppConstants.getUserSessionSize()+"，请注销其它登录信息后再试！]", SsmAppConstants.getUserSessionSize());
                    return result;
                }
            }

            //将用户信息放入session中
            SsmSecurityUtils.putUserToSession(request, user);
            logger.info("用户{}登录系统,IP:{}.", user.getLoginName(), SsmIpUtils.getIpAddr(request));

            //设置调整URL 如果session中包含未被授权的URL 则跳转到该页面
            String resultUrl = request.getContextPath() +SsmAppConstants.getAdminPath()+ "/index?theme=" + theme;
            Object unAuthorityUrl = request.getSession().getAttribute(SsmSecurityConstants.SESSION_UNAUTHORITY_URL);
            if (unAuthorityUrl != null) {
                resultUrl = unAuthorityUrl.toString();
                //清空未被授权的URL
                request.getSession().setAttribute(SsmSecurityConstants.SESSION_UNAUTHORITY_URL, null);
            }
            //返回
            result = new SsmResult(SsmResult.SUCCESS, "用户验证通过!", resultUrl);
        }

        return result;
    }


    /**
     * 用户注销
     * @param request
     * @return
     */
    @RequestMapping(value = {"logout"})
    public String logout(HttpServletRequest request) {
    	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        if (sessionInfo != null) {
            // 退出时清空session中的内容
            String sessionId = request.getSession().getId();
            //由监听器更新在线用户列表
            SsmSecurityUtils.removeUserFromSession(sessionId, false, SsmSecurityType.logout);
            logger.info("用户{}退出系统.", sessionInfo.getLoginName());
        }
        return "redirect:/";
    }



    /**
     * 当前在线用户
     *
     * @throws Exception
     */
    @RequestMapping(value = {"onlineDatagrid"})
    @ResponseBody
    public SsmDatagrid<SsmSessionInfo> onlineDatagrid() throws Exception {
        return SsmSecurityUtils.getSessionUser();
    }

    /**
     * 异步方式返回session信息
     */
    @RequestMapping(value = {"sessionInfo"})
    @ResponseBody
    public SsmResult sessionInfo() {
    	SsmResult result = SsmResult.successResult();
    	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        result.setObj(sessionInfo);
        if (logger.isDebugEnabled()) {
            logger.debug(result.toString());
        }
        return result;
    }
}
