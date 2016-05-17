/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.google.common.collect.Lists;
import com.ssm.pay.common.exception.SsmActionException;
import com.ssm.pay.common.model.SsmCombobox;
import com.ssm.pay.common.model.SsmDatagrid;
import com.ssm.pay.common.model.SsmResult;
import com.ssm.pay.common.model.SsmTreeNode;
import com.ssm.pay.common.orm.SsmPage;
import com.ssm.pay.common.orm.SsmPropertyFilter;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.common.utils.encode.SsmEncrypt;
import com.ssm.pay.common.utils.mapper.SsmJsonMapper;
import com.ssm.pay.common.web.springmvc.SsmBaseController;
import com.ssm.pay.common.web.springmvc.SsmSpringMVCHolder;
import com.ssm.pay.core.excelTools.SsmExcelUtils;
import com.ssm.pay.core.excelTools.SsmJsGridReportBase;
import com.ssm.pay.core.excelTools.SsmTableData;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.core.web.upload.exception.SsmFileNameLengthLimitExceededException;
import com.ssm.pay.core.web.upload.exception.SsmInvalidExtensionException;
import com.ssm.pay.modules.disk.utils.SsmDiskUtils;
import com.ssm.pay.modules.sys._enum.SsmSexType;
import com.ssm.pay.modules.sys.entity.SsmOrgan;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.modules.sys.service.SsmOrganManager;
import com.ssm.pay.modules.sys.service.SsmPostManager;
import com.ssm.pay.modules.sys.service.SsmResourceManager;
import com.ssm.pay.modules.sys.service.SsmRoleManager;
import com.ssm.pay.modules.sys.service.SsmUserManager;
import com.ssm.pay.utils.SsmAppConstants;
import com.ssm.pay.utils.SsmSelectType;

/** 用户管理层
* @author Jiang.Li
* @version 2016年4月12日 下午8:26:19
*/
@SuppressWarnings("serial")
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class SsmUserController extends SsmBaseController<SsmUser,Long> {
	@Autowired
    private SsmUserManager userManager;
    @Autowired
    private SsmOrganManager organManager;
    @Autowired
    private SsmRoleManager roleManager;
    @Autowired
    private SsmResourceManager resourceManager;
    @Autowired
    private SsmPostManager postManager;

    @Override
    public SsmEntityManager<SsmUser, Long> getEntityManager() {
        return userManager;
    }


    @RequestMapping(value = {""})
    public String list() {
        return "modules/sys/user";
    }

    @RequestMapping(value = {"select"})
    public String selectPage(String userIds,Model model,Integer grade) {
        List<SsmUser> users = Lists.newArrayList();
        if (SsmStringUtils.isNotBlank(userIds)) {
            String[] userIdss = userIds.split(",");
            List<Long> userIdLs = Lists.newArrayList();
            for (String userId : userIdss) {
                userIdLs.add(Long.valueOf(userId));
            }
            Criterion inUserCriterion = Restrictions.in("id",userIdLs);
            users = userManager.findByCriteria(inUserCriterion);
        }
        model.addAttribute("users", users);
        model.addAttribute("grade", grade);
//        model.addAttribute("userDatagridData", this.combogridSelectUser(null,null,null,"name",Page.ASC));
        model.addAttribute("userDatagridData", SsmJsonMapper.getInstance().toJson(new SsmDatagrid(users.size(),users),SsmUser.class,new String[]{"id","loginName","name","sexView","defaultOrganName"}));
        return "modules/sys/user-select";
    }

    @RequestMapping(value = {"combogridSelectUser"})
    @ResponseBody
    public String combogridSelectUser(Long organId, Long roleId,String loginNameOrName,String sort, String order) {
        List<SsmUser> users = userManager.getUsersByOrgOrRole(organId, roleId,loginNameOrName, sort, order);
        SsmDatagrid<SsmUser> dg = new SsmDatagrid<SsmUser>(users.size(), users);
        return SsmJsonMapper.getInstance().toJson(dg,SsmUser.class,new String[]{"id","loginName","name","sexView"});
    }

    /**
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"input"})
    public String input(@ModelAttribute("model") SsmUser user) throws Exception {
        return "modules/sys/user-input";
    }


    @RequestMapping(value = {"_remove"})
    @ResponseBody
    @Override
    public SsmResult remove(@RequestParam(value = "ids", required = false) List<Long> ids) {
    	SsmResult result;
        userManager.deleteByIds(ids);
        result = SsmResult.successResult();
        logger.debug(result.toString());
        return result;
    }

    /**
     * 自定义查询
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"userDatagrid"})
    @ResponseBody
    public SsmDatagrid<SsmUser> userDatagrid(Long organId,String organSysCode, String loginNameOrName,Integer userType) {
    	SsmPage<SsmUser> p = new SsmPage<SsmUser>(SsmSpringMVCHolder.getRequest());
        //非管理员用户 添加机构系统编码
        if(SsmStringUtils.isBlank(organSysCode)){
        	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        	SsmUser sueperUser = userManager.getSuperUser();
            if(!sessionInfo.getUserId().equals(sueperUser.getId())){
                organSysCode = sessionInfo.getLoginOrganSysCode();
            }
        }
        p = userManager.getUsersByQuery(organId,organSysCode, loginNameOrName,userType, p);
        SsmDatagrid<SsmUser> dg = new SsmDatagrid<SsmUser>(p.getTotalCount(), p.getResult());
        return dg;
    }

    /**
     * 用户combogrid所有
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"combogridAll"})
    @ResponseBody
    public String combogridAll() {
        List<SsmPropertyFilter> filters = Lists.newArrayList();
        filters.add(new SsmPropertyFilter("EQI_status", SsmStatusState.normal.getValue().toString()));
        List<SsmUser> users = userManager.find(filters, "id", "asc");
        SsmDatagrid<SsmUser> dg = new SsmDatagrid<SsmUser>(users.size(), users);
        return SsmJsonMapper.getInstance().toJson(dg,SsmUser.class,new String[]{"id","loginName","name","sexView","defaultOrganName"});
    }

    /**
     * 获取机构用户
     * @param organId 机构ID
     * @return
     */
    @RequestMapping(value = {"combogridOrganUser"})
    @ResponseBody
    public String combogridOrganUser(@RequestParam(value = "organId", required = true)Long organId) {
        List<SsmUser> users = userManager.getUsersByOrganId(organId);
        SsmDatagrid dg = new SsmDatagrid(users.size(),users);
        return SsmJsonMapper.getInstance().toJson(dg,SsmUser.class,new String[]{"id","loginName","name","sexView","defaultOrganName"});
    }


    /**
     * combogrid
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"combogrid"})
    @ResponseBody
    public SsmDatagrid<SsmUser> combogrid(@RequestParam(value = "ids", required = false)List<Long> ids, String loginNameOrName, Integer rows) throws Exception {
        Criterion statusCriterion = Restrictions.eq("status", SsmStatusState.normal.getValue());
        Criterion[] criterions = new Criterion[0];
        criterions = (Criterion[]) ArrayUtils.add(criterions, 0, statusCriterion);
        Criterion criterion = null;
        if (SsmCollections3.isNotEmpty(ids)) {
            //in条件
            Criterion inCriterion = Restrictions.in("id", ids);

            if (SsmStringUtils.isNotBlank(loginNameOrName)) {
                Criterion loginNameCriterion = Restrictions.like("loginName", loginNameOrName, MatchMode.ANYWHERE);
                Criterion nameCriterion = Restrictions.like("name", loginNameOrName, MatchMode.ANYWHERE);
                Criterion criterion1 = Restrictions.or(loginNameCriterion, nameCriterion);
                criterion = Restrictions.or(inCriterion, criterion1);
            } else {
                criterion = inCriterion;
            }
            //合并查询条件
            criterions = (Criterion[]) ArrayUtils.add(criterions, 0, criterion);
        } else {
            if (SsmStringUtils.isNotBlank(loginNameOrName)) {
                Criterion loginNameCriterion = Restrictions.like("loginName", loginNameOrName, MatchMode.ANYWHERE);
                Criterion nameCriterion = Restrictions.like("name", loginNameOrName, MatchMode.ANYWHERE);
                criterion = Restrictions.or(loginNameCriterion, nameCriterion);
                //合并查询条件
                criterions = (Criterion[]) ArrayUtils.add(criterions, 0, criterion);
            }
        }

        //分页查询
        SsmPage<SsmUser> p = new SsmPage<SsmUser>(rows);//分页对象
        p = userManager.findPageByCriteria(p, criterions);
        SsmDatagrid<SsmUser> dg = new SsmDatagrid<SsmUser>(p.getTotalCount(), p.getResult());
        return dg;
    }


    /**
     * 头像 文件上传
     * @param request
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = {"upload"})
    @ResponseBody
    public SsmResult upload(HttpServletRequest request,
                         @RequestParam(value = "uploadFile", required = false)MultipartFile multipartFile) {
    	SsmResult result = null;
        try {
        	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
//            String basePath = DiskUtils.getUserPhotoRelativePath(sessionInfo.getUserId());
//            String filename = FileUploadUtils.upload(request, AppConstants.getDiskBaseDir() + File.separator + basePath, multipartFile, null, AppConstants.getDiskMaxUploadSize(), true, null);
        	com.ssm.pay.modules.disk.entity.SsmFile file = SsmDiskUtils.saveSystemFile(SsmDiskUtils.FOLDER_USER_PHOTO, null, sessionInfo, multipartFile);
            String filename =  SsmDiskUtils.getVirtualFilePath(file);
            result = SsmResult.successResult().setObj(filename);
        } catch (SsmInvalidExtensionException e) {
            result = SsmResult.errorResult().setMsg(SsmDiskUtils.UPLOAD_FAIL_MSG+e.getMessage());
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            result = SsmResult.errorResult().setMsg(SsmDiskUtils.UPLOAD_FAIL_MSG);
        } catch (SsmFileNameLengthLimitExceededException e) {
            result = SsmResult.errorResult().setMsg(SsmDiskUtils.UPLOAD_FAIL_MSG);
        } catch (IOException e){
            result = SsmResult.errorResult().setMsg(SsmDiskUtils.UPLOAD_FAIL_MSG+e.getMessage());
        }

        return result;
    }
    /**
     * 保存.
     */
    @RequestMapping(value = {"save"})
    @ResponseBody
    public SsmResult save(@ModelAttribute("model") SsmUser user) {
        getEntityManager().evict(user);//如过本方法中有对model。setXX操作 则需执行evict方法 防止Hibernate session自动同步
        SsmResult result = null;
        // 名称重复校验
        SsmUser nameCheckUser = userManager.getUserByLoginName(user.getLoginName());
        if (nameCheckUser != null && !nameCheckUser.getId().equals(user.getId())) {
            result = new SsmResult(SsmResult.WARN, "登录名为[" + user.getLoginName() + "]已存在,请修正!", "loginName");
            logger.debug(result.toString());
            return result;
        }

        if (user.getId() == null) {// 新增
            user.setPassword(SsmEncrypt.e(user.getPassword()));
        } else {// 修改
        	SsmUser superUser = userManager.getSuperUser();
        	SsmUser sessionUser = userManager.getCurrentUser();
            if (nameCheckUser != null && !sessionUser.getId().equals(superUser.getId())) {
                result = new SsmResult(SsmResult.ERROR, "超级用户信息仅允许自己修改!",null);
                logger.debug(result.toString());
                return result;
            }
        }
        userManager.saveEntity(user);
        result = SsmResult.successResult();
        logger.debug(result.toString());
        return result;
    }

    /**
     * 修改用户信息.
     */
    @RequestMapping("userInfoInput")
    public ModelAndView userInfoInput() {
        ModelAndView modelAndView = new ModelAndView("layout/north-userInfoInput");
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        SsmUser user = userManager.loadById(sessionInfo.getUserId());
        SsmJsonMapper jsonMapper = SsmJsonMapper.getInstance();
//        解决hibernate延时加载设置
        jsonMapper.registerHibernate4Module();
        modelAndView.addObject("userJson",jsonMapper.toJson(user));
        return modelAndView;
    }

    /**
     * 保存用户信息.
     */
    @RequestMapping("saveUserinfo")
    @ResponseBody
    public SsmResult saveUserinfo(@ModelAttribute("model")SsmUser model) throws Exception {
    	SsmResult result = null;
        userManager.saveEntity(model);
        result = SsmResult.successResult();
        return result;
    }

    /**
     * 修改用户密码页面.
     */
    @RequestMapping(value = {"password"})
    public String password() throws Exception {
        return "modules/sys/user-password";

    }

    /**
     * 修改用户密码.
     * @param id 用户ID
     * @param upateOperate 需要密码"1" 不需要密码"0".
     * @param password 原始密码
     * @param newPassword 新密码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"updateUserPassword"})
    @ResponseBody
    public SsmResult updateUserPassword(@RequestParam(value = "id", required = true)Long id, @RequestParam(value = "upateOperate", required = true)String upateOperate, String password, @RequestParam(value = "newPassword", required = true)String newPassword) throws Exception {
    	SsmResult result;
    	SsmUser u = userManager.loadById(id);
        if (u != null) {
            boolean isCheck = true;
            //需要输入原始密码
            if (SsmAppConstants.USER_UPDATE_PASSWORD_YES.equals(upateOperate)) {
                String originalPassword = u.getPassword(); //数据库存储的原始密码
                String pagePassword = password; //页面输入的原始密码（未加密）
                if (!originalPassword.equals(SsmEncrypt.e(pagePassword))) {
                    isCheck = false;
                }
            }
            //不需要输入原始密码
            if (SsmAppConstants.USER_UPDATE_PASSWORD_NO.equals(upateOperate)) {
                isCheck = true;
            }
            if (isCheck) {
                u.setPassword(SsmEncrypt.e(newPassword));
                userManager.saveEntity(u);
                result = SsmResult.successResult();
            } else {
                result = new SsmResult(SsmResult.WARN, "原始密码输入错误.", "password");
            }
        } else {
            throw new SsmActionException("用户【"+id+"】不存在或已被删除.");
        }
        logger.debug(result.toString());
        return result;
    }

    /**
     * 修改用户密码 批量、无需输入原密码.
     * @param userIds 用户ID集合
     * @param newPassword 新密码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"_updateUserPassword"})
    @ResponseBody
    public SsmResult updateUserPassword(@RequestParam(value = "userIds", required = false) List<Long> userIds,
                                     @RequestParam(value = "newPassword", required = true)String newPassword) throws Exception {
        userManager.updateUserPassword(userIds,SsmEncrypt.e(newPassword));
        return SsmResult.successResult();
    }


    /**
     * 修改用户角色页面.
     */
    @RequestMapping(value = {"role"})
    public String role() throws Exception {
        return "modules/sys/user-role";
    }

    /**
     * 修改用户角色.
     */
    @RequestMapping(value = {"updateUserRole"})
    @ResponseBody
    public SsmResult updateUserRole(@RequestParam(value = "userIds", required = false) List<Long> userIds,
                                 @RequestParam(value = "roleIds", required = false) List<Long> roleIds) throws Exception {
    	SsmResult result = null;
        userManager.updateUserRole(userIds,roleIds);
        result = SsmResult.successResult();
        return result;
    }

    /**
     * 设置组织机构页面.
     */
    @RequestMapping(value = {"organ"})
    public String organ(@ModelAttribute("model") SsmUser user, Model model) throws Exception {
        //设置默认组织机构初始值
        List<SsmCombobox> defaultOrganCombobox = Lists.newArrayList();
        if (user.getId() != null) {
            List<SsmOrgan> organs = user.getOrgans();
            SsmCombobox combobox;
            if (!SsmCollections3.isEmpty(organs)) {
                for (SsmOrgan organ : organs) {
                    combobox = new SsmCombobox(organ.getId().toString(), organ.getName());
                    defaultOrganCombobox.add(combobox);
                }
            }
        }
        String defaultOrganComboboxData = SsmJsonMapper.nonDefaultMapper().toJson(defaultOrganCombobox);
        logger.debug(defaultOrganComboboxData);
        model.addAttribute("defaultOrganComboboxData", defaultOrganComboboxData);
        return "modules/sys/user-organ";
    }

    /**
     * 设置用户机构 批量更新用户 机构信息
     * @param userIds 用户Id集合
     * @param organIds 所所机构ID集合
     * @param defaultOrganId 默认机构
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"updateUserOrgan"})
    @ResponseBody
    public SsmResult updateUserOrgan(@RequestParam(value = "userIds", required = false) List<Long> userIds,
                                  @RequestParam(value = "organIds", required = false) List<Long> organIds, Long defaultOrganId) throws Exception {
    	SsmResult result = null;
        userManager.updateUserOrgan(userIds, organIds, defaultOrganId);
        result = SsmResult.successResult();
        return result;

    }

    /**
     * 设置用户岗位页面.
     * @param organId 机构ID
     * @param uiModel
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"post"})
    public String post(Long organId,Model uiModel) throws Exception {
        uiModel.addAttribute("organId",organId);
        return "modules/sys/user-post";
    }

    /**
     * 修改用户岗位.
     * @param userIds 用户Id集合
     * @param postIds 岗位ID集合
     */
    @RequestMapping(value = {"updateUserPost"})
    @ResponseBody
    public SsmResult updateUserPost(@RequestParam(value = "userIds", required = false)List<Long> userIds,
                                 @RequestParam(value = "postIds", required = false) List<Long> postIds) {
    	SsmResult result = null;
        userManager.updateUserPost(userIds,postIds);
        result = SsmResult.successResult();
        return result;
    }

    /**
     * 修改用户资源页面.
     */
    @RequestMapping(value = {"resource"})
    public String resource(@ModelAttribute("model") SsmUser model,Model uiModel) throws Exception {
        List<SsmTreeNode> treeNodes = resourceManager.getResourceTree(null, true);
        String resourceComboboxData = SsmJsonMapper.nonDefaultMapper().toJson(treeNodes);
        logger.debug(resourceComboboxData);
        uiModel.addAttribute("resourceComboboxData", resourceComboboxData);
        return "modules/sys/user-resource";
    }

    /**
     * 修改用户资源.
     * @param userIds 用户ID集合
     * @param resourceIds 资源ID集合
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"updateUserResource"})
    @ResponseBody
    public SsmResult updateUserResource(@RequestParam(value = "userIds", required = false) List<Long> userIds,
                                     @RequestParam(value = "resourceIds", required = false)List<Long> resourceIds) throws Exception {
    	SsmResult result = null;
        userManager.updateUserResource(userIds,resourceIds);
        result = SsmResult.successResult();
        return result;

    }

    /**
     * 性别下拉框
     *
     * @throws Exception
     */
    @RequestMapping(value = {"sexTypeCombobox"})
    @ResponseBody
    public List<SsmCombobox> sexTypeCombobox(String selectType) throws Exception {
        List<SsmCombobox> cList = Lists.newArrayList();

        //为combobox添加  "---全部---"、"---请选择---"
        if (!SsmStringUtils.isBlank(selectType)) {
        	SsmSelectType s = SsmSelectType.getSelectTypeValue(selectType);
            if (s != null) {
            	SsmCombobox selectCombobox = new SsmCombobox("", s.getDescription());
                cList.add(selectCombobox);
            }
        }
        SsmSexType[] _enums =  SsmSexType.values();
        for (int i = 0; i < _enums.length; i++) {
        	SsmCombobox combobox = new SsmCombobox(_enums[i].getValue().toString(), _enums[i].getDescription());
            cList.add(combobox);
        }
        return cList;
    }


    /**
     *
     * @param q 查询关键字
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"autoComplete"})
    @ResponseBody
    public List<String> autoComplete(String q) throws Exception {
        List<String> cList = Lists.newArrayList();
        List<SsmPropertyFilter> filters = Lists.newArrayList();
        System.out.println(q);
        SsmPropertyFilter propertyFilter = new SsmPropertyFilter("LIKES_name",q);
        filters.add(propertyFilter);

        SsmPage<SsmUser> p = new SsmPage<SsmUser>(SsmSpringMVCHolder.getRequest());
        p  = userManager.findPage(p,filters);
        for (SsmUser user:p .getResult()) {
            cList.add(user.getName());
        }
        return cList;
    }

    /**
     * 排序最大值.
     */
    @RequestMapping(value = {"maxSort"})
    @ResponseBody
    public SsmResult maxSort(){
    	SsmResult result;
        Integer maxSort = userManager.getMaxSort();
        result = new SsmResult(SsmResult.SUCCESS, null, maxSort);
        return result;
    }

    /**
     * 排序调整
     * @param upUserId 需要上位的用户ID
     * @param downUserId 需要下位的用户ID
     * @param moveUp 是否上移动 否则下移
     * @return
     */
    @RequestMapping(value = {"changeOrderNo"})
    @ResponseBody
    public SsmResult changeOrderNo(@RequestParam(required = true) Long upUserId,
                                @RequestParam(required = true)Long downUserId,boolean moveUp){
        userManager.changeOrderNo(upUserId,downUserId,moveUp);
        return SsmResult.successResult();
    }

    /**
     * 锁定用户 批量
     * @param userIds 用户ID集合
     * @param status {@link com.eryansky.common.orm.entity.StatusState}
     * @return
     */
    @RequestMapping(value = {"lock"})
    @ResponseBody
    public SsmResult lock(@RequestParam(value = "userIds", required = false) List<Long> userIds,
                       @RequestParam(required = true,defaultValue = "1")Integer status){
        userManager.lockUsers(userIds,status);
        return SsmResult.successResult();
    }

    /**
     * 多Sheet Excel导出，获取的数据格式是List<Object[]>
     * @return
     * @throws Exception
     */
    @RequestMapping("export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setContentType("application/msexcel;charset=UTF-8");
        List<SsmUser> users = userManager.getAllNormal();

        List<Object[]> list = new ArrayList<Object[]>();
        Iterator<SsmUser> iterator = users.iterator();
        while (iterator.hasNext()){
        	SsmUser user = iterator.next();
            list.add(new Object[]{user.getLoginName(),user.getName(),user.getSexView(),user.getAddress(),user.getDefaultOrganName()});
        }

        List<SsmTableData> tds = new ArrayList<SsmTableData>();

        //Sheet1
        String[] parents = new String[] {"", "基本信息", "",""};//父表头数组
        String[][] children = new String[][] {
                new String[]{"登录名"},
                new String[]{"姓名", "性别"},
                new String[]{"地址"},
                new String[]{"部门"}};//子表头数组
        String[] fields = new String[] {"loginName", "name", "sexView", "address","defaultOrganName"};//People对象属性数组
        SsmTableData td = SsmExcelUtils.createTableData(users, SsmExcelUtils.createTableHeader(parents, children), fields);
        td.setSheetTitle("合并表头示例");
        tds.add(td);

        //Sheet2
        String[] hearders = new String[] {"登录名", "姓名", "性别", "地址","部门"};//表头数组
        td = SsmExcelUtils.createTableData(users, SsmExcelUtils.createTableHeader(hearders),fields);
        td.setSheetTitle("普通表头示例");
        tds.add(td);

        String title = "用户信息导出示例";
        SsmJsGridReportBase report = new SsmJsGridReportBase(request, response);
        report.exportToExcel(title, SsmSecurityUtils.getCurrentSessionInfo().getLoginName(), tds);

    }
}
