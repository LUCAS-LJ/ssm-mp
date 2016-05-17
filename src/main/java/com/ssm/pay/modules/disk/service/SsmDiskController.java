/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ssm.pay.common.exception.SsmActionException;
import com.ssm.pay.common.model.SsmCombobox;
import com.ssm.pay.common.model.SsmDatagrid;
import com.ssm.pay.common.model.SsmResult;
import com.ssm.pay.common.model.SsmTreeNode;
import com.ssm.pay.common.orm.SsmPage;
import com.ssm.pay.common.utils.SsmPrettyMemoryUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.common.utils.mapper.SsmJsonMapper;
import com.ssm.pay.common.web.springmvc.SsmSimpleController;
import com.ssm.pay.common.web.springmvc.SsmSpringMVCHolder;
import com.ssm.pay.common.web.utils.SsmDownloadUtils;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.core.web.upload.SsmFileUploadUtils;
import com.ssm.pay.core.web.upload.exception.SsmFileNameLengthLimitExceededException;
import com.ssm.pay.core.web.upload.exception.SsmInvalidExtensionException;
import com.ssm.pay.modules.disk.entity.SsmFile;
import com.ssm.pay.modules.disk.entity.SsmFolder;
import com.ssm.pay.modules.disk.entity._enum.SsmFolderAuthorize;
import com.ssm.pay.modules.disk.utils.SsmDiskUtils;
import com.ssm.pay.modules.sys._enum.SsmOrganType;
import com.ssm.pay.modules.sys.service.SsmOrganManager;
import com.ssm.pay.modules.sys.service.SsmUserManager;
import com.ssm.pay.utils.SsmAppConstants;
import com.ssm.pay.utils.SsmSelectType;

/** 云盘管理
* @author Jiang.Li
* @version 2016年4月13日 下午8:40:31
*/
@Controller
@RequestMapping(value = "${adminPath}/disk")
public class SsmDiskController extends SsmSimpleController {
	@Autowired
    private SsmFolderManager folderManager;
    @Autowired
    private SsmFileManager fileManager;
    @Autowired
    private SsmUserManager userManager;
    @Autowired
    private SsmOrganManager organManager;
    @Autowired
    private SsmUserStorageManager userStorageManager;
    @Autowired
    private SsmOrganStorageManager organStorageManager;

    @RequestMapping(value = {""})
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("modules/disk/disk");
        return modelAndView;
    }


    /**
     * 文件夹编辑页面
     * @param folderId
     * @param folderAuthorize {@link com.eryansky.modules.disk.entity._enum.FolderAuthorize}
     * @param parentFolderId
     * @param organId
     * @return
     */
    @RequestMapping(value = {"folderInput"})
    public ModelAndView folderInputPage(Long folderId,Integer folderAuthorize,Long parentFolderId,Long organId,Long roleId){
        ModelAndView modelAndView = new ModelAndView("modules/disk/disk-folderInput");
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        SsmFolder model = new SsmFolder();
        if(folderId !=null){
            model = folderManager.loadById(folderId);
        }

        modelAndView.addObject("model",model);
        modelAndView.addObject("folderAuthorize",folderAuthorize);
        if(parentFolderId != null){//不允许在别人的文件夹下创建文件夹
        	SsmFolder parentFolder = folderManager.loadById(parentFolderId);
            if(!parentFolder.getUserId().equals(sessionInfo.getUserId())){
                parentFolderId = null;
            }
        }
        modelAndView.addObject("parentFolderId",parentFolderId);
        modelAndView.addObject("organId",organId);
        return modelAndView;
    }

    /**
     * 文件夹树
     * @param folderAuthorize {@link com.eryansky.modules.disk.entity._enum.FolderAuthorize}
     * @param organId
     * @param excludeFolderId
     * @param selectType
     * @return
     */
    @RequestMapping(value = {"folderTree"})
    @ResponseBody
    public List<SsmTreeNode> folderTree(Integer folderAuthorize,Long organId,Long excludeFolderId,String selectType){
        List<SsmTreeNode> treeNodes = Lists.newArrayList();

        SsmTreeNode selectTreeNode = SsmSelectType.treeNode(selectType);
        if(selectTreeNode != null){
            treeNodes.add(selectTreeNode);
        }
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        List<SsmTreeNode> folderTreeNodes = null;
        if(SsmFolderAuthorize.Organ.getValue().equals(folderAuthorize) && organId == null){//部门网盘 没有传递organId返回null
            folderTreeNodes = Lists.newArrayList();
        }else{
            folderTreeNodes = folderManager.getFolders(folderAuthorize,sessionInfo.getUserId(),organId,excludeFolderId,true);
        }
        treeNodes.addAll(folderTreeNodes);
        return treeNodes;
    }

    /**
     * 保存文件夹
     * @return
     */
    @RequestMapping(value = {"folderAuthorizeCombobox"})
    @ResponseBody
    public List<SsmCombobox> folderAuthorizeCombobox(String selectType){
        List<SsmCombobox> cList = Lists.newArrayList();
        SsmCombobox selectCombobox = SsmSelectType.combobox(selectType);
        if(selectCombobox != null){
            cList.add(selectCombobox);
        }
//        if(SecurityUtils.isDiskAdmin(null)){
//            FolderAuthorize[] _enums = FolderAuthorize.values();
//            for(int i=0;i<_enums.length;i++){
//                Combobox combobox = new Combobox(_enums[i].getValue().toString(), _enums[i].getDescription());
//                cList.add(combobox);
//            }
//        }else{
//            Combobox combobox = new Combobox(FolderAuthorize.User.getValue().toString(), FolderAuthorize.User.getDescription());
//            cList.add(combobox);
//            combobox = new Combobox(FolderAuthorize.Organ.getValue().toString(),FolderAuthorize.Organ.getDescription());
//            cList.add(combobox);
//            combobox = new Combobox(FolderAuthorize.Pulic.getValue().toString(), FolderAuthorize.Pulic.getDescription());
//            cList.add(combobox);
//        }

        SsmCombobox combobox = new SsmCombobox(SsmFolderAuthorize.User.getValue().toString(), SsmFolderAuthorize.User.getDescription());
        cList.add(combobox);
        combobox = new SsmCombobox(SsmFolderAuthorize.Organ.getValue().toString(), SsmFolderAuthorize.Organ.getDescription());
        cList.add(combobox);
        combobox = new SsmCombobox(SsmFolderAuthorize.Pulic.getValue().toString(), SsmFolderAuthorize.Pulic.getDescription());
        cList.add(combobox);

        return cList;
    }

    public enum ModelType{
        Folder,File;
    }

    @ModelAttribute
    public void getModel(ModelType modelType,Long id, Model uiModel){
        if(modelType != null && id != null){
            if(modelType.equals(ModelType.Folder)){
                uiModel.addAttribute("model",folderManager.loadById(id));
            }else if(modelType.equals(ModelType.File)){
                uiModel.addAttribute("model",fileManager.loadById(id));
            }
        }

    }


    /**
     * 保存文件夹
     * @return
     */
    @RequestMapping(value = {"saveFolder"})
    @ResponseBody
    public SsmResult saveFolder(@ModelAttribute("model")SsmFolder folder){
    	SsmResult result = null;
        if(folder.getUserId() == null){
            folder.setUserId(SsmSecurityUtils.getCurrentSessionInfo().getUserId());
        }
        folderManager.saveOrUpdate(folder);
        result = SsmResult.successResult();
        return result;
    }

    /**
     * 删除文件夹
     * @param folderId 文件夹ID
     * @return
     */
    @RequestMapping(value = {"folderRemove/{folderId}"})
    @ResponseBody
    public SsmResult folderRemove(@PathVariable Long folderId){
    	SsmResult result = null;
        folderManager.deleteFolderAndFiles(folderId);
        result = SsmResult.successResult();
        return result;
    }


    /**
     * 磁盘树 节点类型
     */
    public enum NType{
        FolderAuthorize,Folder,Organ;
    }
    public static final String NODE_TYPE = "nType";
    public static final String NODE_OPERATE = "operate";
    public static final String NODE_USERNAME = "userName";


    /**
     * 是否允操作文件夹
     * @param folderId 文件夹ID
     * @param isAdmin 是否是管理员
     * @return
     */
    private boolean isOperateFolder(Long folderId,boolean isAdmin){
    	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
    	SsmFolder folder = folderManager.loadById(folderId);
        boolean operateAble = isAdmin;
        if(!operateAble && folder != null){
            if(sessionInfo.getUserId().equals(folder.getUserId())){
                operateAble = true;
            }
        }
        return operateAble;
    }

    /**
     * 递归用户文件夹树
     * @param userTreeNodes
     * @param folder
     * @param isCascade
     */
    public void recursiveUserFolderTreeNode(List<SsmTreeNode> userTreeNodes, SsmFolder folder, boolean isCascade){
    	SsmTreeNode treeNode = new SsmTreeNode(folder.getId().toString(),folder.getName());
        treeNode.getAttributes().put(SsmDiskController.NODE_TYPE, SsmDiskController.NType.Folder.toString());
        treeNode.getAttributes().put(SsmDiskController.NODE_OPERATE,true);
        treeNode.setIconCls("eu-icon-folder");
        userTreeNodes.add(treeNode);
        if(isCascade){
            List<SsmFolder> childFolders = folderManager.getChildFoldersByByParentFolderId(folder.getId());
            List<SsmTreeNode> childTreeNodes = Lists.newArrayList();
            for(SsmFolder childFolder:childFolders){
                this.recursiveUserFolderTreeNode(childTreeNodes, childFolder, isCascade);
            }
            for(SsmTreeNode childTreeNode:childTreeNodes){
                treeNode.addChild(childTreeNode);
            }
        }

    }

    /**
     * 递归部门文件夹树
     * @param organTreeNode
     * @param isAdmin
     */
    private void recursiveOrganTreeNode(SsmTreeNode organTreeNode,boolean isAdmin){
        organTreeNode.getAttributes().put(NODE_TYPE, NType.Organ.toString());
        organTreeNode.setState(SsmTreeNode.STATE_OPEN);
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        long limitStorage = organStorageManager.getOrganAvaiableStorage(sessionInfo.getLoginOrganId());//部门可用空间
        long usedStorage = fileManager.getOrganUsedStorage(Long.valueOf(organTreeNode.getId()));//部门已用空间
        String organNodeName = organTreeNode.getText();
        if(SsmOrganType.department.getValue().equals(organTreeNode.getAttributes().get("type"))){
            organNodeName = organTreeNode.getText()+"("+ SsmPrettyMemoryUtils.prettyByteSize(usedStorage)+"/"+ SsmPrettyMemoryUtils.prettyByteSize(limitStorage)+")";
        }
        organTreeNode.setText(organNodeName);


        //用户在部门下的文件夹
        List<SsmFolder> organUserFolders = folderManager.getOrganFolders(Long.valueOf(organTreeNode.getId()), sessionInfo.getUserId(), false, null);
        //排除用户在部门以外的所有文件夹
        List<SsmFolder> excludeUserOrganFolders = folderManager.getOrganFolders(Long.valueOf(organTreeNode.getId()), sessionInfo.getUserId(), true, null);
        //合并
        List<SsmFolder> organFolders = SsmCollections3.aggregate(organUserFolders, excludeUserOrganFolders);

        List<SsmTreeNode> treeNodes = Lists.newArrayList();
        Iterator<SsmFolder> iterator = organFolders.iterator();
        while (iterator.hasNext()){
        	SsmFolder folder = iterator.next();
            folderManager.recursiveFolderTreeNode(treeNodes,folder,null,true);
        }
        for(int i=0;i<treeNodes.size();i++){
        	SsmTreeNode t = treeNodes.get(i);
        	SsmFolder folder = organFolders.get(i);
            t.getAttributes().put(SsmDiskController.NODE_OPERATE,this.isOperateFolder(folder.getId(),isAdmin));
            t.setText(t.getText()+"（<span style='color:blue;'>"+organFolders.get(i).getUserName()+"</span>）");
            organTreeNode.addChild(t);
        }



        for(SsmTreeNode childTreeNode:organTreeNode.getChildren()){
            if(!NType.Folder.toString().equals((String) childTreeNode.getAttributes().get(NODE_TYPE))){
                recursiveOrganTreeNode(childTreeNode,isAdmin);
            }
        }
    }

    /**
     * 递归用户文件夹树
     * @param publicTreeNodes
     * @param folder
     * @param isAdmin
     */
    public void recursivePublicFolderTreeNode(List<SsmTreeNode> publicTreeNodes, SsmFolder folder,boolean isAdmin){
    	SsmTreeNode treeNode = new SsmTreeNode(folder.getId().toString(),folder.getName());
        treeNode.getAttributes().put(SsmDiskController.NODE_TYPE, SsmDiskController.NType.Folder.toString());
        treeNode.getAttributes().put(SsmDiskController.NODE_OPERATE,this.isOperateFolder(folder.getId(),isAdmin));
        treeNode.setIconCls("eu-icon-folder");
        publicTreeNodes.add(treeNode);
        List<SsmFolder> childFolders = folderManager.getChildFoldersByByParentFolderId(folder.getId());
        List<SsmTreeNode> childTreeNodes = Lists.newArrayList();
        for(SsmFolder childFolder:childFolders){
            this.recursivePublicFolderTreeNode(childTreeNodes, childFolder, isAdmin);
        }
        for(SsmTreeNode childTreeNode:childTreeNodes){
            treeNode.addChild(childTreeNode);
        }

    }
    /**
     * 个人 磁盘树
     *
     * @return
     */
    @RequestMapping(value = {"diskTree"})
    @ResponseBody
    public List<SsmTreeNode> diskTree(){
        List<SsmTreeNode> treeNodes = Lists.newArrayList();
        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
        List<SsmFolder> userFolders = folderManager.getAuthorizeFoldersByUserId(sessionInfo.getUserId());
        List<SsmFolder> userOwnerFolders = Lists.newArrayList();
        List<SsmFolder> publicFolders = Lists.newArrayList();
        List<SsmFolder> userAuthorizeOrganFolders = Lists.newArrayList();
        List<SsmFolder> roleFolders = Lists.newArrayList();
        //文件夹数据分类
        for(SsmFolder folder:userFolders){
            if(SsmFolderAuthorize.User.getValue().equals(folder.getFolderAuthorize()) && sessionInfo.getUserId().equals(folder.getUserId())){
                userOwnerFolders.add(folder);
            }else if(SsmFolderAuthorize.Organ.getValue().equals(folder.getFolderAuthorize())){
                userAuthorizeOrganFolders.add(folder);
            }else if(SsmFolderAuthorize.Pulic.getValue().equals(folder.getFolderAuthorize())){
                publicFolders.add(folder);
            }else if(SsmFolderAuthorize.Role.getValue().equals(folder.getFolderAuthorize())){
                roleFolders.add(folder);
            }
        }

        //树构造
        //个人
        long limitStorage = userStorageManager.getUserAvaiableStorage(sessionInfo.getUserId());//用户可用个人空间
        long usedStorage = fileManager.getUserUsedStorage(sessionInfo.getUserId());//用户已用空间
        SsmTreeNode userOwnerTreeNode = new SsmTreeNode(SsmFolderAuthorize.User.getValue().toString(),
        		SsmFolderAuthorize.User.getDescription()+"("+ SsmPrettyMemoryUtils.prettyByteSize(usedStorage)+"/"+ SsmPrettyMemoryUtils.prettyByteSize(limitStorage)+")");
        userOwnerTreeNode.getAttributes().put(NODE_TYPE, NType.FolderAuthorize.toString());
        userOwnerTreeNode.setIconCls("eu-icon-user");

        List<SsmFolder> folders = folderManager.getFoldersByFolderAuthorize(SsmFolderAuthorize.User.getValue(),sessionInfo.getUserId(),null,null,null);
        List<SsmTreeNode> userFolderTreeNodes = Lists.newArrayList();
        for(SsmFolder folder:folders){
            this.recursiveUserFolderTreeNode(userFolderTreeNodes, folder, true);
        }

        for(SsmTreeNode userFolderTreeNode:userFolderTreeNodes){
            userOwnerTreeNode.addChild(userFolderTreeNode);
        }
        treeNodes.add(userOwnerTreeNode);

        //部门
        SsmTreeNode organTreeNode = new SsmTreeNode(SsmFolderAuthorize.Organ.getValue().toString(), SsmFolderAuthorize.Organ.getDescription());
        organTreeNode.getAttributes().put(NODE_TYPE, NType.FolderAuthorize.toString());
        organTreeNode.setIconCls("eu-icon-disk_organ");

        Long organId = sessionInfo.getLoginOrganId();
        boolean isAdmin = SsmSecurityUtils.isDiskAdmin(null);
        if(isAdmin){
            organId = null;
        }
        List<SsmTreeNode> organTreeNodes = organManager.getOrganTree(organId, null, true, false,null);
        for(SsmTreeNode organNode:organTreeNodes){
            organNode.setState(SsmTreeNode.STATE_CLOASED);
            this.recursiveOrganTreeNode(organNode,isAdmin);
            organTreeNode.addChild(organNode);
        }
        treeNodes.add(organTreeNode);

        //公共
        SsmTreeNode publicTreeNode = new SsmTreeNode(SsmFolderAuthorize.Pulic.getValue().toString(), SsmFolderAuthorize.Pulic.getDescription());
        publicTreeNode.getAttributes().put(NODE_TYPE, NType.FolderAuthorize.toString());
        publicTreeNode.setIconCls("eu-icon-disk_public");
        List<SsmTreeNode> publicTreeNodes = folderManager.getFolders(SsmFolderAuthorize.Pulic.getValue(),null,null,null,true);
//        for(Folder folder:publicFolders){
//            recursivePublicFolderTreeNode(publicTreeNodes,folder,isAdmin);
//        }
        for(SsmTreeNode treeNode:publicTreeNodes){
            treeNode.getAttributes().put(SsmDiskController.NODE_OPERATE,this.isOperateFolder(Long.valueOf(treeNode.getId()) ,isAdmin));
            treeNode.setText(treeNode.getText()+"（<span style='color:blue;'>"+treeNode.getAttributes().get(NODE_USERNAME)+"</span>）");
            publicTreeNode.addChild(treeNode);
        }
        treeNodes.add(publicTreeNode);

        //角色 TODO

        return treeNodes;
    }


    /**
     * 文件列表
     * @param folderId 文件夹Id
     * @return
     */
    @RequestMapping(value = {"folderFileDatagrid"})
    @ResponseBody
    public String folderFileDatagrid(Long folderId,String fileName){
    	SsmPage<SsmFile> page = new SsmPage<SsmFile>(SsmSpringMVCHolder.getRequest());
        if(folderId == null){
            return SsmJsonMapper.getInstance().toJson(new SsmDatagrid());
        }
        page = fileManager.findPage(page,folderId,fileName);
        SsmDatagrid<SsmFile> dg = new SsmDatagrid<SsmFile>(page.getTotalCount(),page.getResult());
        //fotter
        List<Map<String,Object>> footer = Lists.newArrayList();
        long totalSize = 0L;//分页总大小
        if(SsmCollections3.isNotEmpty(page.getResult())){
            for(SsmFile file:page.getResult()){
                totalSize += file.getFileSize();
            }
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put("name","总大小");
        map.put("prettyFileSize", SsmPrettyMemoryUtils.prettyByteSize(totalSize));
        footer.add(map);
        dg.setFooter(footer);

        String json = SsmJsonMapper.getInstance().toJson(dg,SsmFile.class,
                new String[]{"id","name","prettyFileSize","createTime","userName"});
        return json;
    }


    /**
     * 文件上传页面
     * @param folderId 文件夹ID
     * @return
     */
    @RequestMapping(value = {"fileInput"})
    public ModelAndView fileInputPage(Long folderId){
        ModelAndView modelAndView = new ModelAndView("modules/disk/disk-fileInput");
        SsmFolder model = new SsmFolder();
        if(folderId !=null){
            model = folderManager.loadById(folderId);
            if(model != null){
                modelAndView.addObject("folderName",model.getName());
            }
        }
        modelAndView.addObject("folderId",folderId);
        return modelAndView;
    }


    /**
     * 文件信息修改
     * @return
     */
    @RequestMapping(value = {"fileSave"})
    @ResponseBody
    public SsmResult fileSave(@ModelAttribute("model")SsmFile file){
        fileManager.saveEntity(file);
        return SsmResult.successResult();
    }


    /**
     * 上传容量校验
     * @param sessionInfo
     * @param folder
     * @param uploadFileSize
     * @return
     * @throws com.eryansky.common.exception.ActionException
     */
    private boolean checkStorage(SsmSessionInfo sessionInfo,SsmFolder folder,long uploadFileSize) throws SsmActionException {
        boolean flag = false;
        if(SsmFolderAuthorize.User.getValue().equals(folder.getFolderAuthorize())){
            long limitStorage = userStorageManager.getUserAvaiableStorage(sessionInfo.getUserId());//用户可用个人空间
            long usedStorage = fileManager.getUserUsedStorage(sessionInfo.getUserId());//用户已用空间
            long avaiableStorage = limitStorage - usedStorage;
            if(avaiableStorage < uploadFileSize){
                throw new SsmActionException("用户个人云盘空间不够！可用大小："+ SsmPrettyMemoryUtils.prettyByteSize(avaiableStorage));
            }
        }else if(SsmFolderAuthorize.Organ.getValue().equals(folder.getFolderAuthorize())){
            long limitStorage = organStorageManager.getOrganAvaiableStorage(sessionInfo.getLoginOrganId());//部门可用个人空间
            long usedStorage = fileManager.getOrganUsedStorage(folder.getOrganId());//部门已用空间
            long avaiableStorage = limitStorage - usedStorage;
            if(avaiableStorage < uploadFileSize){
                throw new SsmActionException("部门云盘空间不够！可用大小："+ SsmPrettyMemoryUtils.prettyByteSize(avaiableStorage));
            }
        }
        return  flag;
    }

    /**
     * 文件上传容量校验
     * @param folderId 文件夹ID
     * @param uploadFileSize 上传文件的大小 单位：字节
     * @return
     */
    @RequestMapping(value = {"fileLimitCheck/{folderId}"})
    @ResponseBody
    public SsmResult fileLimitCheck(@PathVariable Long folderId,Long uploadFileSize,String filename) {
    	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
    	SsmResult result = null;
        try {
        	SsmFolder folder = folderManager.loadById(folderId);
            checkStorage(sessionInfo,folder,uploadFileSize);
            result = SsmResult.successResult();
        } catch (SsmActionException e){
            result = SsmResult.errorResult().setMsg("文件【"+filename+"】上传失败，"+e.getMessage());
        }
        return result;
    }

    /**
     * 文件上传
     * @param folderId 文件夹
     * @param uploadFile 上传文件
     * @return
     */
    @RequestMapping(value = {"fileUpload"})
    @ResponseBody
    public SsmResult fileUpload( @RequestParam(value = "folderId", required = false)Long folderId,
                             @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile) throws IOException {
    	SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
    	SsmResult result = null;
        String url = null;

        SsmFolder folder = folderManager.loadById(folderId);
        String relativeDir = SsmDiskUtils.getRelativePath(folder,sessionInfo.getUserId());
        SsmFile file = null;
        Exception exception = null;
        try {
            checkStorage(sessionInfo,folder,uploadFile.getSize());
            String code = SsmFileUploadUtils.encodingFilenamePrefix(sessionInfo.getUserId().toString(), uploadFile.getOriginalFilename());
            url = SsmFileUploadUtils.upload(null, relativeDir, uploadFile, null, SsmAppConstants.getDiskMaxUploadSize(), true, code);
            file = new SsmFile();
            file.setFolder(folder);
            file.setCode(code);
            file.setUserId(sessionInfo.getUserId());
            file.setName(uploadFile.getOriginalFilename());
            file.setFilePath(url);
            file.setFileSize(uploadFile.getSize());
            file.setFileSuffix(FilenameUtils.getExtension(uploadFile.getOriginalFilename()));
            fileManager.save(file);

            Map<String,String> obj = Maps.newHashMap();
            obj.put("fileId",file.getId().toString());
            obj.put("name", file.getName());
            obj.put("filePath", file.getFilePath());
            result = SsmResult.successResult().setObj(obj).setMsg("文件上传成功！");
        } catch (SsmInvalidExtensionException e) {
//            e.printStackTrace();
            exception = e;
            result = SsmResult.errorResult().setMsg(SsmDiskUtils.UPLOAD_FAIL_MSG+e.getMessage());
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
//            e.printStackTrace();
            exception = e;
            result = SsmResult.errorResult().setMsg(SsmDiskUtils.UPLOAD_FAIL_MSG);
        } catch (SsmFileNameLengthLimitExceededException e) {
//            e.printStackTrace();
            exception = e;
            result = SsmResult.errorResult().setMsg(SsmDiskUtils.UPLOAD_FAIL_MSG);
        } catch (SsmActionException e){
//            e.printStackTrace();
            exception = e;
            result = SsmResult.errorResult().setMsg(SsmDiskUtils.UPLOAD_FAIL_MSG+e.getMessage());
        }finally {
            if (exception != null) {
                if(file != null){
                    fileManager.delete(file);
                }
            }
        }
        return  result;

    }

    /**
     * 文件转发 根据文件ID查找到服务器上的硬盘文件 virtual
     * @param response
     * @param request
     * @param fileId
     */
    @RequestMapping(value = { "file/{fileId}" })
    public void file(HttpServletResponse response,
                     HttpServletRequest request, @PathVariable Long fileId) {
    	SsmFile file = fileManager.loadById(fileId);
        try {
            java.io.File diskFile = null;
            if(file != null){
                diskFile = file.getDiskFile();
            }
            FileCopyUtils.copy(new FileInputStream(diskFile), response.getOutputStream());
            response.setHeader("Content-Type", "application/octet-stream");
            return;
        } catch (FileNotFoundException e) {
            if(logger.isWarnEnabled()) {
                logger.warn(String.format("请求的文件%s不存在", fileId), e.getMessage());
            }
            return;
        } catch (IOException e) {
            logger.warn(String.format("请求的文件%s不存在", fileId), e.getMessage());
        }

    }


    @RequestMapping(value = {"fileDownload/{fileId}"})
    public void fileDownload(HttpServletResponse response,
                             HttpServletRequest request,
                             @PathVariable Long fileId) {
    	SsmFile file = fileManager.loadById(fileId);

        //TODO 文件下载权限校验
        SsmActionException fileNotFoldException = new SsmActionException("文件不存在，已被删除或移除。");
        if(file == null){
            logger.error("文件[{}]不存在",new Object[]{fileId});
            throw fileNotFoldException;
        }

        try {
            String fileBasePath = SsmFileUploadUtils.getBasePath(file.getFilePath());
            file.getDiskFile();
            java.io.File diskFile  = SsmFileUploadUtils.getAbsoluteFile(fileBasePath);
            if(!diskFile.exists() || !diskFile.canRead()){
                throw fileNotFoldException;
            }
            String displayName = file.getName();
            SsmDownloadUtils.download(request, response, new FileInputStream(diskFile), displayName);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw fileNotFoldException;
        }

    }


    /**
     * 文件删除
     * @param fileIds 文件集合
     * @return
     */
	@RequestMapping(value = { "delFolderFile" })
	@ResponseBody
	public SsmResult delFolderFile(
			@RequestParam(value = "fileIds", required = false) List<Long> fileIds) {
		fileManager.deleteFolderFiles(null, fileIds);
		return SsmResult.successResult();
	}
}
