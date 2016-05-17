/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.service;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.ssm.pay.common.exception.SsmDaoException;
import com.ssm.pay.common.exception.SsmServiceException;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.model.SsmTreeNode;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.common.utils.mapper.SsmJsonMapper;
import com.ssm.pay.common.web.springmvc.SsmSpringMVCHolder;
import com.ssm.pay.core.security.SsmSecurityUtils;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.modules.disk.entity.SsmFile;
import com.ssm.pay.modules.disk.entity.SsmFolder;
import com.ssm.pay.modules.disk.entity._enum.SsmFolderAuthorize;
import com.ssm.pay.modules.sys.entity.SsmUser;
import com.ssm.pay.modules.sys.service.SsmRoleManager;
import com.ssm.pay.modules.sys.service.SsmUserManager;

/** 文件夹管理
* @author Jiang.Li
* @version 2016年4月13日 下午7:57:43
*/
@Service
public class SsmFolderManager extends SsmEntityManager<SsmFolder, Long> {
	 @Autowired
	    private SsmUserManager userManager;
	    @Autowired
	    private SsmRoleManager roleManager;
	    @Autowired
	    private SsmFileManager fileManager;

	    private SsmHibernateDao<SsmFolder, Long> folderDao;


	    /**
	     * 通过注入的sessionFactory初始化默认的泛型DAO成员变量.
	     */
	    @Autowired
	    public void setSessionFactory(final SessionFactory sessionFactory) {
	        folderDao = new SsmHibernateDao<SsmFolder, Long>(sessionFactory, SsmFolder.class);
	    }

	    @Override
	    protected SsmHibernateDao<SsmFolder, Long> getEntityDao() {
	        return folderDao;
	    }


	    /**
	     * 删除文件夹 包含子级文件夹以及文件
	     * @param folderId
	     * @param folderId
	     * @throws com.eryansky.common.exception.DaoException
	     * @throws com.eryansky.common.exception.SystemException
	     * @throws com.eryansky.common.exception.ServiceException
	     */
	    public void deleteFolderAndFiles(Long folderId) throws SsmDaoException, SsmSystemException, SsmServiceException {
	        Validate.notNull(folderId, "参数[folderId]不能为null.");
	        //TODO
	        List<Long> fileIds = Lists.newArrayList();
	        List<Long> folderIds = Lists.newArrayList();
	        recursiveFolderAndFile(folderIds, fileIds, folderId);

	        System.out.println(SsmJsonMapper.getInstance().toJson(folderIds));
	        System.out.println(SsmJsonMapper.getInstance().toJson(fileIds));
	        fileManager.deleteFolderFiles(SsmSpringMVCHolder.getRequest(), fileIds);
	        this.deleteByIds(folderIds);
//	        for (int i = folderIds.size() - 1; i >= 0; i--) {
//	            Long fId = folderIds.get(i);
//	            this.deleteById(fId);
//	        }

	    }

	    /**
	     * 递归 查找文件夹下的文件夹以及文件
	     * @param folderIds
	     * @param fileIds
	     * @param folderId
	     */
	    private void recursiveFolderAndFile(List<Long> folderIds, List<Long> fileIds, Long folderId){
	        folderIds.add(folderId);
	        List<SsmFile> folderFiles = fileManager.getFolderFiles(folderId);
	        for(SsmFile folderFile:folderFiles){
	            fileIds.add(folderFile.getId());
	        }
	        List<SsmFolder> childFolders = this.getChildFoldersByByParentFolderId(folderId);
	        if (SsmCollections3.isNotEmpty(childFolders)){
	            for(SsmFolder childFolder:childFolders){
	                recursiveFolderAndFile(folderIds, fileIds, childFolder.getId());
	            }
	        }
	    }




	    /**
	     * 获取用户文创建的文件夹
	     * @param userId 用户ID
	     * @return
	     */
	    public List<SsmFolder> getFoldersByUserId(Long userId){
	        Validate.notNull(userId, "参数[userId]不能为null.");
	        SsmParameter parameter = new SsmParameter(SsmStatusState.delete.getValue(),userId);
	        return getEntityDao().find("from Folder f where f.status <> :p1 and f.userId = :p2",parameter);
	    }

	    /**
	     * 某个用户是否可以操作文件夹
	     * @param userId 用户ID
	     * @param folder 文件夹
	     * @return
	     */
	    public boolean isOperateFolder(Long userId,SsmFolder folder){
	        Long _userId = userId;
	        SsmSessionInfo sessionInfo = SsmSecurityUtils.getCurrentSessionInfo();
	        if(_userId == null){
	            _userId = sessionInfo.getUserId();
	        }

	        boolean operateAble =  SsmSecurityUtils.isDiskAdmin(_userId);
	        if(!operateAble){
	            if(sessionInfo.getUserId().equals(folder.getUserId())){
	                operateAble = true;
	            }
	        }
	        return operateAble;
	    }

	    /**
	     * 获取用户授权使用的文件夹
	     * 注:包含用户自己创建、公开、所属部门、所属角色
	     * @param userId 用户ID
	     * @return
	     */
	    public List<SsmFolder> getAuthorizeFoldersByUserId(Long userId){
	        Validate.notNull(userId, "参数[userId]不能为null.");
	        SsmParameter parameter = new SsmParameter(SsmStatusState.delete.getValue(),userId, SsmFolderAuthorize.User.getValue(), SsmFolderAuthorize.Pulic.getValue());
	        StringBuffer hql = new StringBuffer();
	        hql.append("from Folder f where f.status <> :p1")
	           .append(" and ((f.userId = :p2 and f.folderAuthorize = :p3) or f.folderAuthorize = :p4");
	        SsmUser user = userManager.loadById(userId);
	        if(user != null){
	            List<Long> userRoleIds = user.getRoleIds();
	            if(SsmCollections3.isNotEmpty(userRoleIds)){
	                hql.append(" or f.roleId in (:userRoleIds)");
	                parameter.put("userRoleIds",userRoleIds);
	            }
	            List<Long> userOrganIds = user.getOrganIds();
	            if(SsmCollections3.isNotEmpty(userRoleIds)){
	                hql.append(" or f.organId in (:userOrganIds)");
	                parameter.put("userOrganIds",userOrganIds);
	            }

	        }
	        hql.append(")");
	        hql.append(" order by f.folderAuthorize asc,f.createTime desc");
	        logger.debug(hql.toString());
	        return getEntityDao().find(hql.toString(),parameter);
	    }

	    /**
	     * 获取部门下的文件夹
	     * @param organId 机构ID
	     * @param userId 用户ID
	     * @param excludeUserOrganFolder 是否排除用户在部门的文件夹
	     * @param parentFolderId
	     * @return
	     */
	    public List<SsmFolder> getOrganFolders(Long organId,Long userId,boolean excludeUserOrganFolder,Long parentFolderId){
	        Validate.notNull(organId, "参数[organId]不能为null.");
	        SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue(), SsmFolderAuthorize.Organ.getValue(),organId);
	        StringBuffer hql = new StringBuffer();
	        hql.append("from Folder f where f.status = :p1 and f.folderAuthorize = :p2 and f.organId = :p3");
	        if(userId != null){
	            hql.append(" and f.userId ");
	            if(excludeUserOrganFolder){
	                hql.append(" <> ");
	            }else{
	                hql.append(" = ");
	            }
	            hql.append(" :userId ");
	            parameter.put("userId",userId);
	        }
	        if(parentFolderId != null){
	            hql.append(" and f.parentId = :parentFolderId");
	            parameter.put("parentFolderId",parentFolderId);
	        }else{
	            hql.append(" and f.parentId is null");
	        }
	        hql.append(" order by f.createTime desc");
	        logger.debug(hql.toString());
	        return getEntityDao().find(hql.toString(),parameter);
	    }

	    /**
	     *
	     * @param folderAuthorize {@link com.eryansky.modules.disk.entity._enum.FolderAuthorize}
	     * @param userId 用户ID
	     * @param organId 机构ID
	     * @param excludeFolderId 排除的文件夹ID
	     * @param isCascade 是否级联
	     * @return
	     */
	    public List<SsmTreeNode> getFolders(Integer folderAuthorize,Long userId,Long organId,Long excludeFolderId,boolean isCascade){
	        Validate.notNull(folderAuthorize, "参数[folderAuthorize]不能为null.");
	        List<SsmFolder> folders = this.getFoldersByFolderAuthorize(folderAuthorize,userId,organId,null,null);
	        List<SsmTreeNode> treeNodes = Lists.newArrayList();
	        for(SsmFolder folder:folders){
	            if(!folder.getId().equals(excludeFolderId)){
	                this.recursiveFolderTreeNode(treeNodes,folder,excludeFolderId,isCascade);
	            }
	        }
	        return treeNodes;
	    }

	    /**
	     * 递归文件夹树
	     * @param treeNodes
	     * @param folder
	     */
	    public void recursiveFolderTreeNode(List<SsmTreeNode> treeNodes,SsmFolder folder,Long excludeFolderId,boolean isCascade){
	    	SsmTreeNode treeNode = new SsmTreeNode(folder.getId().toString(),folder.getName());
	        treeNode.getAttributes().put(SsmDiskController.NODE_TYPE, SsmDiskController.NType.Folder.toString());
	        treeNode.getAttributes().put(SsmDiskController.NODE_USERNAME, folder.getUserName());
	        treeNode.setIconCls("eu-icon-folder");
	        treeNodes.add(treeNode);
	        if(isCascade){
	            List<SsmFolder> childFolders = this.getChildFoldersByByParentFolderId(folder.getId());
	            List<SsmTreeNode> childTreeNodes = Lists.newArrayList();
	            for(SsmFolder childFolder:childFolders){
	                if(!folder.getId().equals(excludeFolderId)){
	                    this.recursiveFolderTreeNode(childTreeNodes,childFolder,excludeFolderId,isCascade);
	                }
	            }
	            for(SsmTreeNode childTreeNode:childTreeNodes){
	                treeNode.addChild(childTreeNode);
	            }
	        }

	    }

	    /**
	     * 查询某个授权类型下的文件夹
	     * 0个人：个人文件夹 部门：部门下的文件夹（包含自己在部门下建立的文件夹） 角色：角色下的文件夹
	     * @param folderAuthorize
	     * @param userId
	     * @param organId
	     * @param roleId
	     * @param parentFolderId 上级文件夹 null:查询顶级文件夹 不为null:查询该级下一级文件夹
	     * @return
	     */
	    public List<SsmFolder> getFoldersByFolderAuthorize(Integer folderAuthorize,Long userId,Long organId,Long roleId,Long parentFolderId){
	        Validate.notNull(folderAuthorize, "参数[folderAuthorize]不能为null.");
	        SsmParameter parameter = new SsmParameter(SsmStatusState.delete.getValue(), folderAuthorize);
	        StringBuffer hql = new StringBuffer();
	        hql.append("from Folder f where f.status <> :p1 and f.folderAuthorize = :p2 ");
	        if(SsmFolderAuthorize.User.getValue().equals(folderAuthorize)){
	            Validate.notNull(userId, "参数[userId]不能为null.");
	            hql.append(" and f.userId = :userId");
	            parameter.put("userId",userId);
	        }else if(SsmFolderAuthorize.Organ.getValue().equals(folderAuthorize)){
	            Validate.notNull(organId, "参数[organId]不能为null.");
	            if(userId != null){
	                hql.append(" and f.userId = :userId");
	                parameter.put("userId",userId);
	            }
	            hql.append(" and f.organId = :organId");
	            parameter.put("organId",organId);
	        }else if(SsmFolderAuthorize.Role.getValue().equals(folderAuthorize)){
	            Validate.notNull(roleId, "参数[roleId]不能为null.");
	            hql.append(" and f.roleId = :roleId");
	            parameter.put("roleId",roleId);
	        }else if(SsmFolderAuthorize.Pulic.getValue().equals(folderAuthorize)){
	            if(userId != null){
	                hql.append(" and f.userId = :userId");
	                parameter.put("userId",userId);
	            }
	        }else{
	           throw new SsmServiceException("无法识别参数[folderAuthorize]："+folderAuthorize);
	        }

	        if(parentFolderId != null){
	            hql.append(" and f.parentId = :parentFolderId");
	            parameter.put("parentFolderId",parentFolderId);
	        }else{
	            hql.append(" and f.parentId is null");
	        }
	        hql.append(" order by f.createTime desc");
	        logger.debug(hql.toString());
	        return getEntityDao().find(hql.toString(),parameter);
	    }

	    /**
	     * 根据父级ID查找子级文件夹
	     * @param parentFolderId 父级文件夹ID null:查询顶级文件夹 不为null:查询该级下一级文件夹
	     * @return
	     */
	    public List<SsmFolder> getChildFoldersByByParentFolderId(Long parentFolderId){
	    	SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue());
	        StringBuffer hql = new StringBuffer();
	        hql.append("from Folder f where f.status = :p1 ");
	        if(parentFolderId != null){
	            hql.append(" and f.parentId = :parentFolderId");
	            parameter.put("parentFolderId",parentFolderId);
	        }else{
	            hql.append(" and f.parentId is null");
	        }
	        hql.append(" order by f.createTime desc");
	        logger.debug(hql.toString());
	        return getEntityDao().find(hql.toString(),parameter);
	    }
}
