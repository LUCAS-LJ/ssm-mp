/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.web.upload.exception;

import java.util.Arrays;
import org.apache.commons.fileupload.FileUploadException;

/** 
* @author Jiang.Li
* @version 2016年4月13日 下午8:23:02
*/
public class SsmInvalidExtensionException extends FileUploadException {
	private String[] allowedExtension;
    private String extension;
    private String filename;

    public SsmInvalidExtensionException(String[] allowedExtension, String extension, String filename) {
        super("filename : [" + filename + "], extension : [" + extension + "], allowed extension : [" + Arrays.toString(allowedExtension) + "]");
        this.allowedExtension = allowedExtension;
        this.extension = extension;
        this.filename = filename;
    }

    public String[] getAllowedExtension() {
        return allowedExtension;
    }

    public String getExtension() {
        return extension;
    }

    public String getFilename() {
        return filename;
    }

    public static class SsmInvalidImageExtensionException extends SsmInvalidExtensionException {
        public SsmInvalidImageExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    public static class SsmInvalidFlashExtensionException extends SsmInvalidExtensionException {
        public SsmInvalidFlashExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    public static class SsmInvalidMediaExtensionException extends SsmInvalidExtensionException {
        public SsmInvalidMediaExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }
}
