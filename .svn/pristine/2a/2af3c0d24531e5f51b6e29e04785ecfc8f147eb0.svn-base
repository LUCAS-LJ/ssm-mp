/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.web;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.google.common.collect.Maps;
import com.ssm.pay.common.model.SsmResult;
import com.ssm.pay.common.web.springmvc.SsmSimpleController;
import com.ssm.pay.common.web.utils.SsmWebUtils;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.modules.notice.entity.SsmNoticeScope;
import com.ssm.pay.modules.notice.service.SsmNoticeScopeManager;

/** 主页面
* @author Jiang.Li
* @version 2016年4月15日 上午10:48:18
*/
@Controller
@RequestMapping(value = "${adminPath}/portal")
public class SsmPortalController extends SsmSimpleController {
	@Autowired
    private SsmNoticeScopeManager noticeScopeManager;

    @RequestMapping("")
    public ModelAndView portal() {
        ModelAndView modelAnView = new ModelAndView("layout/portal");
        return modelAnView;
    }


    /**
     * 我的通知
     *
     * @return
     */
    @RequestMapping("notice")
    public ModelAndView notice() {
        ModelAndView modelAnView = new ModelAndView("layout/portal-notice");
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        if (sessionInfo != null) {
            List<SsmNoticeScope> noticeScopes = noticeScopeManager.getUserNewNotices(sessionInfo.getUserId(), null);
            modelAnView.addObject("noticeScopes", noticeScopes);
        }

        return modelAnView;
    }

    /**
     * 个人消息中心
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("mymessages")
    @ResponseBody
    public SsmResult mymessages(HttpServletResponse response) throws Exception {
    	SsmWebUtils.setNoCacheHeader(response);
    	SsmResult result = null;
        Map<String, Long> map = Maps.newHashMap();
        // 当前登录用户
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        long noticeScopes = noticeScopeManager.getUserUnreadNoticeNum(sessionInfo.getUserId());
        map.put("noticeScopes", noticeScopes);

        result = SsmResult.successResult().setObj(map);
        return result;
    }
}
