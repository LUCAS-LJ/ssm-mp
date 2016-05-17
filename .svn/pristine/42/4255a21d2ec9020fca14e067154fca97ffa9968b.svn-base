/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ssm.pay.common.spring.SsmSpringContextHolder;
import com.ssm.pay.core.security.SsmSessionInfo;
import com.ssm.pay.core.web.upload.SsmFileUploadUtils;
import com.ssm.pay.core.web.upload.exception.SsmFileNameLengthLimitExceededException;
import com.ssm.pay.core.web.upload.exception.SsmInvalidExtensionException;
import com.ssm.pay.modules.disk.entity.SsmFile;
import com.ssm.pay.modules.disk.entity.SsmFolder;
import com.ssm.pay.modules.disk.entity._enum.SsmFolderAuthorize;
import com.ssm.pay.modules.disk.service.SsmDiskManager;
import com.ssm.pay.utils.SsmAppConstants;

/** 云盘公共接口 以及相关工具类
* @author Jiang.Li
* @version 2016年4月13日 下午7:55:40
*/
public class SsmDiskUtils {
	private static SsmDiskManager diskManager = SsmSpringContextHolder.getBean(SsmDiskManager.class);

	/**
	 * 文件虚拟路径 用于文件转发
	 */
	public static final String FILE_VIRTUAL_PATH = "disk/file/";
	/**
	 * 文件夹标识 通知
	 */
	public static String FOLDER_NOTICE = "notice";
    /**
     * kindeditor
     */
    public static String FOLDER_KINDEDITOR = "kindeditor";
    /**
     * 用户头像
     */
    public static String FOLDER_USER_PHOTO = "userphoto";

    /**
     * 文件上传失败提示信息
     */
    public static final String UPLOAD_FAIL_MSG = "文件上传失败！";


    /**
     * KindEditor编辑器文件
     * @param userId
     * @return
     */
    public static String getKindEditorRelativePath(Long userId) {
    	SsmFolder folder = new SsmFolder();
        folder.setFolderAuthorize(SsmFolderAuthorize.SysTem.getValue());
        folder.setCode(FOLDER_KINDEDITOR);
        return getRelativePath(folder,userId);
    }


    /**
     * 得到用户头像出差相对路径
     * @param userId 用户ID
     * @return
     */
    public static String getUserPhotoRelativePath(Long userId) {
    	SsmFolder folder = new SsmFolder();
        folder.setFolderAuthorize(SsmFolderAuthorize.SysTem.getValue());
        folder.setCode(FOLDER_USER_PHOTO);
        return getRelativePath(folder,userId);
    }



    /**
     * 得到用户相对路径
     * @param folderCode 文件夹编码
     * @param userId 用户ID
     * @return
     */
    public static String getRelativePath(String folderCode,Long userId) {
    	SsmFolder folder = new SsmFolder();
        folder.setFolderAuthorize(SsmFolderAuthorize.SysTem.getValue());
        folder.setCode(folderCode);
        return getRelativePath(folder,userId);
    }


	/**
	 *  生成对象保存的相对地址
	 * 
	 * @param folder
	 *            文件夹
	 * @return
	 */
	public static String getRelativePath(SsmFolder folder, Long userId) {
		Date now = Calendar.getInstance().getTime();
		StringBuffer path = new StringBuffer();
		path.append(DateFormatUtils.format(now, "yyyy"))
				.append(java.io.File.separator);
		String folderAuthorize = SsmFolderAuthorize
				.getFolderAuthorize(folder.getFolderAuthorize()).toString()
				.toLowerCase();
		path.append(folderAuthorize).append(java.io.File.separator);
		if (SsmFolderAuthorize.User.getValue().equals(folder.getFolderAuthorize())) {
			path.append(folder.getUserId()).append(java.io.File.separator)
					.append(folder.getId());
		} else if (SsmFolderAuthorize.Organ.getValue().equals(
				folder.getFolderAuthorize())) {
			path.append(folder.getOrganId()).append(java.io.File.separator)
					.append(folder.getId());
		} else if (SsmFolderAuthorize.Role.getValue().equals(
				folder.getFolderAuthorize())) {
			path.append(folder.getRoleId()).append(java.io.File.separator)
					.append(folder.getId());
		} else if (SsmFolderAuthorize.Pulic.getValue().equals(
				folder.getFolderAuthorize())) {
			path.append(folder.getId());
		} else if (SsmFolderAuthorize.SysTem.getValue().equals(
				folder.getFolderAuthorize())) {
			path.append(folder.getCode());
            if (userId != null) {
                path.append(java.io.File.separator).append(userId);
            }
        }
		return path.toString();
	}
	
	/**
	 * 根据文件夹 创建基准目录
	 * 
	 * @param folder
	 *            文件夹
	 * @return
	 */
	public static String getFolderPath(SsmFolder folder, Long userId) {
		StringBuffer path = new StringBuffer();
		String relativePath = getRelativePath(folder, userId);
		path.append(SsmAppConstants.getDiskBasePath())
				.append(java.io.File.separator).append(relativePath);
				
		return path.toString();
	}
	

	/**
	 * 根据编码获取 获取系统文件夹 <br/>
	 * 如果不存在则自动创建
	 * 
	 * @param code
	 *            系统文件夹编码
	 * @return
	 */
	public static SsmFolder getSystemFolderByCode(String code) {
		return diskManager.checkSystemFolderByCode(code);
	}

    /**
     * 根据编码、用户ID获取 获取系统文件夹 <br/>
     * @param code 系统文件夹编码
     * @param userId 用户ID
     * @return
     */
    public static SsmFolder getSystemFolderByCode(String code,Long userId) {
        return diskManager.checkSystemFolderByCode(code,userId);
    }

    /**
     * 获取用户通知文件夹
     * @param userId
     * @return
     */
    public static SsmFolder getUserNoticeFolder(Long userId) {
        return diskManager.checkSystemFolderByCode(FOLDER_NOTICE,userId);
    }

    /**
     * 获取用户头像文件夹
     * @param userId
     * @return
     */
    public static SsmFolder getUserPhotoFolder(Long userId) {
        return diskManager.checkSystemFolderByCode(FOLDER_USER_PHOTO,userId);
    }

	/**
	 * 保存系统文件
	 * 
	 * @param folderCode
	 *            系统文件夹编码
	 * @param request
	 *            请求对象
	 * @param sessionInfo
	 *            session信息 允允许为null
	 * @param multipartFile
	 *            上传文件对象 SpringMVC
	 * @return
	 * @throws com.eryansky.core.web.upload.exception.InvalidExtensionException
	 * @throws org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException
	 * @throws com.eryansky.core.web.upload.exception.FileNameLengthLimitExceededException
	 * @throws java.io.IOException
	 */
	public static SsmFile saveSystemFile(String folderCode,
			HttpServletRequest request, SsmSessionInfo sessionInfo,
			MultipartFile multipartFile) throws SsmInvalidExtensionException,
			FileUploadBase.FileSizeLimitExceededException,
			SsmFileNameLengthLimitExceededException, IOException {
		Long userId = null;
		if (sessionInfo != null && sessionInfo.getUserId() != null) {
			userId = sessionInfo.getUserId();
		}

		String code = SsmFileUploadUtils.encodingFilenamePrefix(userId + "",
                multipartFile.getOriginalFilename());
		SsmFolder folder = getSystemFolderByCode(folderCode, userId);
		String url = null; //附件路径
		if (request == null) {
			String relativePath = getRelativePath(folder, userId); // 除配置路径外的文件夹路径
			url = SsmFileUploadUtils.upload(null, relativePath, multipartFile,
                    null, SsmAppConstants.getDiskMaxUploadSize(), true, code);
		} else {
			String baseDir = getFolderPath(folder, userId);
			url = SsmFileUploadUtils.upload(request, baseDir, multipartFile, null,
					SsmAppConstants.getDiskMaxUploadSize(), true, code);
		}

		SsmFile file = new SsmFile();
		file.setFolder(folder);
		file.setCode(code);
		file.setUserId(userId);
		file.setName(multipartFile.getOriginalFilename());
		file.setFilePath(url);
		file.setFileSize(multipartFile.getSize());
		file.setFileSuffix(FilenameUtils.getExtension(multipartFile
                .getOriginalFilename()));
		diskManager.saveFile(file);
		return file;
	}


	/**
	 * 保存通知文件
	 *
	 * @param request
	 * @param sessionInfo
	 * @param multipartFile
	 * @return
	 * @throws com.eryansky.core.web.upload.exception.InvalidExtensionException
	 * @throws org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException
	 * @throws com.eryansky.core.web.upload.exception.FileNameLengthLimitExceededException
	 * @throws java.io.IOException
	 */
	public static SsmFile saveNoticeFile(HttpServletRequest request,
			SsmSessionInfo sessionInfo, MultipartFile multipartFile)
            throws SsmInvalidExtensionException,
            FileUploadBase.FileSizeLimitExceededException,
            SsmFileNameLengthLimitExceededException, IOException {
        return saveSystemFile(SsmDiskUtils.FOLDER_NOTICE, request, sessionInfo,
                multipartFile);
    }


    /**
     * 保存用户头像文件
     *
     * @param request
     * @param sessionInfo
     * @param multipartFile
     * @return
     * @throws com.eryansky.core.web.upload.exception.InvalidExtensionException
     * @throws org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException
     * @throws com.eryansky.core.web.upload.exception.FileNameLengthLimitExceededException
     * @throws java.io.IOException
     */
    public static SsmFile saveUserPhotoFile(HttpServletRequest request,
    		SsmSessionInfo sessionInfo, MultipartFile multipartFile)
            throws SsmInvalidExtensionException,
            FileUploadBase.FileSizeLimitExceededException,
            SsmFileNameLengthLimitExceededException, IOException {
        return saveSystemFile(SsmDiskUtils.FOLDER_USER_PHOTO, request, sessionInfo,
                multipartFile);
    }

    /**
     * 更新文件
     * @param file 文件
     * @return
     */
    public static void updateFile(SsmFile file){
        diskManager.updateFile(file);
    }


    /**
     * 删除文件
     * @param request
     * @param fileId 文件ID
     * @return
     */
    public static void deleteFile(HttpServletRequest request,Long fileId){
        Validate.notNull(fileId, "参数[fileId]不能为null.");
        diskManager.deleteFile(request,fileId);
    }
    /**
     * 删除文件
     * @param file
     * @return
     */
    public static void deleteFile(HttpServletRequest request,SsmFile file){
        Validate.notNull(file, "参数[file]不能为null.");
        diskManager.deleteFile(request, file);
    }

	/**
	 * 获取文件虚拟路径
	 * @param file
	 * @return
	 */
	public static String getVirtualFilePath(SsmFile file){
		return SsmAppConstants.getAdminPath() + "/" + FILE_VIRTUAL_PATH + file.getId();
	}
}
