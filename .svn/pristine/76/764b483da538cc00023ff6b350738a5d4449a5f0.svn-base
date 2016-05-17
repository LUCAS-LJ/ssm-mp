/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.web.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.ssm.pay.common.utils.SsmStringUtils;

/** 文件下载工具
* @author Jiang.Li
* @version 2016年4月15日 上午10:10:52
*/
public class SsmDownloadUtils {
	 /**
     * 下载文件
     * @param request
     * @param response
     * @param filePath 文件路径
     * @throws IOException
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath) throws IOException {
        download(request, response, filePath, "");
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param filePath 文件路径
     * @param displayName 下载显示的文件名
     * @throws IOException
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath, String displayName) throws IOException {
        File file = new File(filePath);
        if(SsmStringUtils.isEmpty(displayName)) {
            displayName = file.getName();
        }
        if (!file.exists() || !file.canRead()) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("您下载的文件不存在！");
            return;
        }

        download(request,response, new FileInputStream(file),displayName);
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param displayName 下载显示的文件名
     * @param bytes 文件字节
     * @throws IOException
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String displayName) throws IOException {
        if (ArrayUtils.isEmpty(bytes)) {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("您下载的文件不存在！");
            return;
        }
        download(request,response,new ByteArrayInputStream(bytes),displayName);
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param inputStream 输入流
     * @param displayName 下载显示的文件名
     * @throws IOException
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, String displayName) throws IOException {
        response.reset();
        SsmWebUtils.setNoCacheHeader(response);

        response.setContentType("application/x-download");
        response.setContentLength((int) inputStream.available());

//        String displayFilename = displayName.substring(displayName.lastIndexOf("_") + 1);
//        displayFilename = displayFilename.replace(" ", "_");
        SsmWebUtils.setDownloadableHeader(request,response,displayName);
        BufferedInputStream is = null;
        OutputStream os = null;
        try {

            os = response.getOutputStream();
            is = new BufferedInputStream(inputStream);
            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
