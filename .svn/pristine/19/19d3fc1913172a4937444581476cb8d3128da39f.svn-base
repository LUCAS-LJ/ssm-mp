/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.service;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.Validate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.pay.common.exception.SsmDaoException;
import com.ssm.pay.common.exception.SsmServiceException;
import com.ssm.pay.common.exception.SsmSystemException;
import com.ssm.pay.common.orm.SsmPage;
import com.ssm.pay.common.orm.entity.SsmStatusState;
import com.ssm.pay.common.orm.hibernate.SsmEntityManager;
import com.ssm.pay.common.orm.hibernate.SsmHibernateDao;
import com.ssm.pay.common.orm.hibernate.SsmParameter;
import com.ssm.pay.common.utils.SsmStringUtils;
import com.ssm.pay.common.utils.collections.SsmCollections3;
import com.ssm.pay.core.web.upload.SsmFileUploadUtils;
import com.ssm.pay.modules.disk.entity.SsmFile;
import com.ssm.pay.modules.disk.entity._enum.SsmFolderAuthorize;

/**文件管理 
* @author Jiang.Li
* @version 2016年4月13日 下午8:09:38
*/
@Service
public class SsmFileManager extends SsmEntityManager<SsmFile, Long> {
	@Autowired
    private SsmFolderManager folderManager;

    private SsmHibernateDao<SsmFile, Long> fileDao;


    /**
     * 通过注入的sessionFactory初始化默认的泛型DAO成员变量.
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        fileDao = new SsmHibernateDao<SsmFile, Long>(sessionFactory, SsmFile.class);
    }

    @Override
    protected SsmHibernateDao<SsmFile, Long> getEntityDao() {
        return fileDao;
    }

    /**
     * 根据文件标识获取文件
     * @param code 文件标识
     * @param excludeFileId 排除的文件ID  可为null
     * @return
     */
    private List<SsmFile> getFileByCode(String code,Long excludeFileId){
        StringBuffer hql = new StringBuffer();
        SsmParameter parameter = new SsmParameter(SsmStatusState.delete.getValue(),code);
        hql.append("from File f where f.status <> :p1  and f.code = :p2");
        if(excludeFileId != null){
            hql.append(" and f.id <> :excludeFileId");
            parameter.put("excludeFileId",excludeFileId);
        }
        List<SsmFile> list = getEntityDao().find(hql.toString(),parameter);
        return list;
    }

    /**
     * 查找文件夹下所有文件
     * @param folderId 文件夹ID
     * @return
     */
    public List<SsmFile> getFolderFiles(Long folderId){
        Validate.notNull(folderId, "参数[folderId]不能为null.");
        SsmParameter parameter = new SsmParameter(SsmStatusState.normal.getValue(),folderId);
        return getEntityDao().find("from File f where f.status = :p1 and f.folder.id = :p2",parameter);
    }

    public void deleteFile(HttpServletRequest request,Long fileId){
    	SsmFile file = getEntityDao().load(fileId);
        try {
            //检查文件是否被引用
            List<SsmFile> files = this.getFileByCode(file.getCode(),fileId);
            if(SsmCollections3.isEmpty(files)){
            	SsmFileUploadUtils.delete(request, file.getFilePath());//磁盘删除文件
                logger.debug("磁盘上删除文件：{}", new Object[]{file.getFilePath()});
            }
//            file.setStatus(StatusState.lock.getValue());
//            this.update(file);
            getEntityDao().delete(file);
        } catch (IOException e) {
            logger.error("删除文件[{}]失败,{}",new Object[]{file.getFilePath(),e.getMessage()});
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    /**
     *
     * 文件删除
     * @param request
     * @param fileIds 文件集合
     * @throws com.eryansky.common.exception.DaoException
     * @throws com.eryansky.common.exception.SystemException
     * @throws com.eryansky.common.exception.ServiceException
     */
    public void deleteFolderFiles(HttpServletRequest request,List<Long> fileIds) throws SsmDaoException, SsmSystemException, SsmServiceException {
        if (SsmCollections3.isNotEmpty(fileIds)) {
            for (Long fileId : fileIds) {
                deleteFile(request,fileId);
            }
        } else {
            logger.warn("参数[ids]为空.");
        }
    }

    public SsmPage<SsmFile> findPage(SsmPage<SsmFile> page,Long folderId,String fileName){
//        Validate.notNull(userId, "参数[userId]不能为null.");
        StringBuffer hql = new StringBuffer();
        SsmParameter parameter = new SsmParameter(SsmStatusState.delete.getValue());
        hql.append("from File f where f.status <> :p1");

        if(folderId != null){
            hql.append(" and f.folder.id = :folderId");
            parameter.put("folderId",folderId);
        }

        if(SsmStringUtils.isNotBlank(fileName)){
            hql.append(" and f.name like :fileName");
            parameter.put("fileName","%"+fileName+"%");
        }
        logger.debug(hql.toString());
        return getEntityDao().findPage(page,hql.toString(),parameter);
    }

    /**
     * 查找用户已用个人存储空间 单位：字节
     * @param userId 用户ID
     * @return
     */
    public long getUserUsedStorage(Long userId){
        Validate.notNull(userId, "参数[userId]不能为null.");
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(f.fileSize) from File f where f.status <> :p1 and f.folder.folderAuthorize = :p2 and f.userId = :p3");
        SsmParameter parameter = new SsmParameter(SsmStatusState.delete.getValue(), SsmFolderAuthorize.User.getValue(),userId);
        List<Object> list = getEntityDao().find(hql.toString(),parameter);

        long count = 0L;
        if (list.size() > 0) {
            count = list.get(0) == null ? 0:(Long)list.get(0);
        }
        return count;
    }


    /**
     * 查找部门已用存储空间 单位：字节
     * @param organId 部门ID
     * @return
     */
    public long getOrganUsedStorage(Long organId){
        Validate.notNull(organId, "参数[organId]不能为null.");
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(f.fileSize) from File f where f.status <> :p1 and f.folder.folderAuthorize = :p2 and f.folder.organId = :p3");
        SsmParameter parameter = new SsmParameter(SsmStatusState.delete.getValue(), SsmFolderAuthorize.Organ.getValue(),organId);
        List<Object> list = getEntityDao().find(hql.toString(),parameter);

        long count = 0L;
        if (list.size() > 0) {
            count = list.get(0) == null ? 0:(Long)list.get(0);
        }
        return count;
    }


    /**
     * 根据ID查找
     * @param fileIds
     * @return
     */
    public List<SsmFile> findFilesByIds(List<Long> fileIds){
    	SsmParameter parameter = new SsmParameter(fileIds);
        return getEntityDao().find("from File f where f.id in (:p1)",parameter);
    }
}
