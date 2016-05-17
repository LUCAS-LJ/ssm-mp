/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.modules.disk.entity.SsmFile;
import com.ssm.pay.modules.disk.entity.SsmFolder;
import com.ssm.pay.modules.disk.entity._enum.SsmFolderAuthorize;

/** 
* @author Jiang.Li
* @version 2016年4月13日 下午7:56:50
*/
@Service
public class SsmDiskManager {
	@Autowired
    private SsmFolderManager folderManager;
    @Autowired
    private SsmFileManager fileManager;


    /**
     * 根据编码获取 获取系统文件夹
     * <br/>如果不存在则自动创建
     * @param code 系统文件夹编码
     * @return
     */
    public SsmFolder checkSystemFolderByCode(String code){
        Validate.notBlank(code, "参数[code]不能为null.");
        SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue(), SsmFolderAuthorize.SysTem.getValue(),code);
        StringBuffer hql = new StringBuffer();
        hql.append("from Folder f where f.status = :p1 and f.folderAuthorize = :p2 and f.code = :p3");
        List<SsmFolder> list =  folderManager.getEntityDao().find(hql.toString(), parameter);
        SsmFolder folder =  list.isEmpty() ? null:list.get(0);
        if(folder == null){
            folder = new SsmFolder();
            folder.setFolderAuthorize(SsmFolderAuthorize.SysTem.getValue());
            folder.setCode(code);
            folderManager.saveOrUpdate(folder);
        }
        return folder;
    }


    /**
     * 根据编码获取 获取用户的系统文件夹
     * <br/>如果不存在则自动创建
     * @param code 系统文件夹编码
     * @param userId 用户ID
     * @return
     */
    public SsmFolder checkSystemFolderByCode(String code,Long userId){
        Validate.notBlank(code, "参数[code]不能为null.");
        Validate.notNull(userId, "参数[userId]不能为null.");
        SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue(), SsmFolderAuthorize.SysTem.getValue(),code,userId);
        StringBuffer hql = new StringBuffer();
        hql.append("from Folder f where f.status = :p1 and f.folderAuthorize = :p2 and f.code = :p3 and f.userId = :p4");
        List<SsmFolder> list =  folderManager.getEntityDao().find(hql.toString(), parameter);
        SsmFolder folder =  list.isEmpty() ? null:list.get(0);
        if(folder == null){
            folder = new SsmFolder();
            folder.setFolderAuthorize(SsmFolderAuthorize.SysTem.getValue());
            folder.setCode(code);
            folder.setUserId(userId);
            folderManager.saveOrUpdate(folder);
        }
        return folder;
    }

    /**
     * 保存系统文件
     * @param folderCode
     * @param file
     * @return
     */
    public SsmFile saveSystemFile(String folderCode,SsmFile file){
        Validate.notBlank(folderCode, "参数[folderCode]不能为null.");
        Validate.notNull(file, "参数[file]不能为null.");
        SsmFolder folder = checkSystemFolderByCode(folderCode);
        file.setFolder(folder);
        fileManager.save(file);
        return file;
    }

    /**
     * 保存文件
     * @param fileId 文件ID
     * @return
     */
    public SsmFile getFileById(Long fileId){
        Validate.notNull(fileId, "参数[fileId]不能为null.");
        return fileManager.loadById(fileId);
    }


    /**
     * 保存文件
     * @param file
     * @return
     */
    public SsmFile saveFile(SsmFile file){
        Validate.notNull(file, "参数[file]不能为null.");
        fileManager.save(file);
        return file;
    }

    /**
     * 修改文件
     * @param file
     * @return
     */
    public SsmFile updateFile(SsmFile file){
        Validate.notNull(file, "参数[file]不能为null.");
        fileManager.update(file);
        return file;
    }

    /**
     * 删除文件
     * @param request
     * @param file
     * @return
     */
    public void deleteFile(HttpServletRequest request,SsmFile file){
        Validate.notNull(file, "参数[file]不能为null.");
        fileManager.deleteFile(request,file.getId());
    }

    /**
     * 删除文件
     * @param request
     * @param fileId
     */
    public void deleteFile(HttpServletRequest request,Long fileId){
        Validate.notNull(fileId, "参数[fileId]不能为null.");
        fileManager.deleteFile(request,fileId);
    }




    /**
     * 根据ID查找
     * @param fileIds 文件ID集合
     * @return
     */
    public List<SsmFile> findFilesByIds(List<Long> fileIds){
        Validate.notEmpty(fileIds, "参数[fileIds]不能为null.");
        if(SsmCollections3.isNotEmpty(fileIds)){
            return fileManager.findFilesByIds(fileIds);
        }else{
        	return null;
        }
    }
}
