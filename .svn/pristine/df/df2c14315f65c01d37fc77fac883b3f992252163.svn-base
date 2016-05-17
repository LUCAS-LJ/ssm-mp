/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.google.common.collect.Lists;
import com.ssm.pay.common.model.SsmMenu;
import com.ssm.pay.common.model.SsmTreeNode;
import com.ssm.pay.common.orm.SsmPage;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmDefaultEntityManager;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.web.springmvc.SsmSpringMVCHolder;
import com.ssm.pay.common.web.utils.SsmWebUtils;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.core.security.annotation.SsmRequiresUser;
import com.ssm.pay.modules.sys._enum.SsmResourceType;
import com.ssm.pay.modules.sys.entity.SsmResource;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.modules.sys.service.SsmResourceManager;
import com.ssm.pay.modules.sys.service.SsmUserManager;
import com.ssm.pay.utils.SsmAppConstants;

/** 主页管理
* @author Jiang.Li
* @version 2016年4月11日 下午7:22:25
*/
@Controller
@RequestMapping(value = "${adminPath}")
public class SsmIndexController {
	@Autowired
    private SsmUserManager userManager;
    @Autowired
    private SsmResourceManager resourceManager;
    @Autowired
    private SsmDefaultEntityManager defaultEntityManager;


    /**
     * 欢迎页面 登录页
     * @return
     * @throws Exception
     */
    @SsmRequiresUser(required = false)
    @RequestMapping(value = {"index/welcome", ""})
    public String welcome() throws Exception {
        return "login";
    }

    @RequestMapping(value = {"index"})
    public String index(String theme) {
        //根据客户端指定的参数跳转至 不同的主题 如果未指定 默认:index
        if (SsmStringUtils.isNotBlank(theme) && (theme.equals("app") || theme.equals("index"))) {
            return "layout/" + theme;
        } else {
            return "layout/index";
        }
    }

    @RequestMapping("index/west")
    public ModelAndView west() {
        ModelAndView modelAnView = new ModelAndView("layout/west");
        SsmUser sessionUser = SsmSecurityUtils.getCurrentUser();
        modelAnView.addObject("user", sessionUser);
        String userPhoto = null;
        if(SsmStringUtils.isNotBlank(sessionUser.getPhoto())){
            userPhoto = SsmSpringMVCHolder.getRequest().getContextPath()+ sessionUser.getPhoto();
        }else{
            userPhoto = SsmSpringMVCHolder.getRequest().getContextPath()+"/static/img/icon_boy.png";
        }
        modelAnView.addObject("userPhoto", userPhoto);
        return modelAnView;
    }



    /**
     * 导航菜单.
     */
    @ResponseBody
    @RequestMapping(value = {"index/navTree"})
    public List<SsmTreeNode> navTree(HttpServletResponse response) {
    	SsmWebUtils.setNoCacheHeader(response);
        List<SsmTreeNode> treeNodes = Lists.newArrayList();
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        if (sessionInfo != null) {
            treeNodes = resourceManager.getNavMenuTreeByUserId(sessionInfo.getUserId());
        }
        return treeNodes;
    }


    /**
     * 桌面版 开始菜单
     */
    @RequestMapping(value = {"index/startMenu"})
    @ResponseBody
    public List<SsmMenu> startMenu() {
        List<SsmMenu> menus = Lists.newArrayList();
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        if (sessionInfo != null) {
            List<SsmResource> rootResources = Lists.newArrayList();
            SsmUser superUser = userManager.getSuperUser();
            if (sessionInfo != null && superUser != null
                    && sessionInfo.getUserId().equals(superUser.getId())) {// 超级用户
                rootResources = resourceManager.getByParentId(null, SsmStatusState.normal.getValue());
            } else if (sessionInfo != null) {
                rootResources = resourceManager.getResourcesByUserId(sessionInfo.getUserId(), null);
                //去除非菜单资源
                Iterator<SsmResource> iterator = rootResources.iterator();
                while (iterator.hasNext()) {
                    if (!SsmResourceType.menu.getValue().equals(iterator.next().getType())) {
                        iterator.remove();
                    }
                }
            }
            for (SsmResource parentResource : rootResources) {
            	SsmMenu menu = this.resourceToMenu(parentResource, true);
                if (menu != null) {
                    menus.add(menu);
                }
            }
        }
        return menus;
    }


    /**
     * 桌面版 桌面应用程序列表
     */
    @RequestMapping(value = {"index/apps"})
    @ResponseBody
    public List<SsmMenu> apps() {
        HttpServletRequest request = SsmSpringMVCHolder.getRequest();
        List<SsmMenu> menus = Lists.newArrayList();
        String head = this.getHeadFromUrl(request.getRequestURL().toString());
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        if (sessionInfo != null) {
            List<SsmResource> resources = Lists.newArrayList();
            SsmUser superUser = userManager.getSuperUser();
            if (sessionInfo != null && superUser != null
                    && sessionInfo.getUserId().equals(superUser.getId())) {// 超级用户
                resources = resourceManager.getAll("orderNo", SsmPage.ASC);
            } else if (sessionInfo != null) {
                resources = resourceManager.getResourcesByUserId(sessionInfo.getUserId());
            }
            for (SsmResource resource : resources) {
                if (resource != null && SsmStringUtils.isNotBlank(resource.getUrl())) {
                    if (SsmResourceType.menu.getValue().equals(resource.getType())) {
                    	SsmMenu menu = new SsmMenu();
                        menu.setId(resource.getId().toString());
                        menu.setText(resource.getName());
                        String url = resource.getUrl();
                        if (url.startsWith("http")) {
                            url = resource.getUrl();
                        } else if (url.startsWith("/")) {
                            url = head + request.getContextPath()  + url;
                        } else {
                            url = head + request.getContextPath() + SsmAppConstants.getAdminPath()+ "/" + url;
                        }
                        menu.setHref(url);
                        menu.setIconCls(resource.getIconCls());
                        menus.add(menu);
                    }
                }

            }
        }
        return menus;
    }

    /**
     * 资源转M
     *
     * @param resource  资源
     * @param isCascade 是否级联
     * @return
     */
    private SsmMenu resourceToMenu(SsmResource resource, boolean isCascade) {
        HttpServletRequest request = SsmSpringMVCHolder.getRequest();
        Assert.notNull(resource, "参数resource不能为空");
        String head = this.getHeadFromUrl(request.getRequestURL().toString());
        if (SsmResourceType.menu.getValue().equals(resource.getType())) {
        	SsmMenu menu = new SsmMenu();
            menu.setId(resource.getId().toString());
            menu.setText(resource.getName());
            String url = resource.getUrl();
            if (url.startsWith("http")) {
                url =  resource.getUrl();
            } else if (url.startsWith("/")) {
                url = head + request.getContextPath()  + url;
            } else {
                url = head + request.getContextPath() + SsmAppConstants.getAdminPath()+ "/" + url;
            }
            menu.setHref(url);
            if (isCascade) {
                List<SsmMenu> childrenMenus = Lists.newArrayList();
                for (SsmResource subResource : resource.getSubResources()) {
                    if (SsmResourceType.menu.getValue().equals(subResource.getType())) {
                        childrenMenus.add(resourceToMenu(subResource, true));
                    }
                }
                menu.setChildren(childrenMenus);
            }
            return menu;
        }
        return null;
    }

    /**
     * 根据URL地址获取请求地址前面部分信息
     *
     * @param url
     * @return
     */
    private String getHeadFromUrl(String url) {
        int firSplit = url.indexOf("//");
        String proto = url.substring(0, firSplit + 2);
        int webSplit = url.indexOf("/", firSplit + 2);
        int portIndex = url.indexOf(":", firSplit);
        String webUrl = url.substring(firSplit + 2, webSplit);
        String port = "";
        if (portIndex >= 0) {
            webUrl = webUrl.substring(0, webUrl.indexOf(":"));
            port = url.substring(portIndex + 1, webSplit);
        } else {
            port = "80";
        }
        return proto + webUrl + ":" + port;
    }
}
