/*@版权所有LIJIANG*/ 
package com.ssm.pay.core.web.upload.exception;

import org.apache.commons.fileupload.FileUploadException;

/** 
* @author Jiang.Li
* @version 2016年4月13日 下午8:33:42
*/
public class SsmFileNameLengthLimitExceededException extends FileUploadException {
	private int length;
    private int maxLength;
    private String filename;

    public SsmFileNameLengthLimitExceededException(String filename, int length, int maxLength) {
        super("file name : [" + filename + "], length : [" + length + "], max length : [" + maxLength + "]");
        this.length = length;
        this.maxLength = maxLength;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public int getLength() {
        return length;
    }


    public int getMaxLength() {
        return maxLength;
    }
}
