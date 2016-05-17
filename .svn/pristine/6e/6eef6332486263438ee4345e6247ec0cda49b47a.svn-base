/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.notice.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssm.pay.modules.notice._enum.SsmNoticeReadMode;
import com.ssm.pay.modules.notice.entity.common.SsmBaseReferenceNotice;
import com.ssm.pay.modules.sys.utils.SsmOrganUtils;
import com.ssm.pay.modules.sys.utils.SsmUserUtils;

/** 
* @author Jiang.Li
* @version 2016年4月15日 上午10:52:39
*/
@SuppressWarnings("serial")
@Entity
@Table(name = "t_notice_notice_scope")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler"})
public class SsmNoticeScope extends SsmBaseReferenceNotice {
	/**
	 * 用户
	 */
	private Long userId;
	/**
	 * 部门
	 */
	private Long organId;
	/**
	 * 是否已读 默认值：否 {@link NoticeReadMode}
	 */
	private Integer isRead = SsmNoticeReadMode.unreaded.getValue();
    /**
     * 读取时间
     */
    private Date readTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganId() {
        return organId;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }


    /**
     * 接收人姓名
     * @return
     */
    @Transient
    public String getUserName(){
        return SsmUserUtils.getUserName(this.userId);
    }

    /**
     * 接收部门名称
     * @return
     */
    @Transient
    public String getOrganName(){
        return SsmOrganUtils.getOrganName(this.organId);
    }

    /**
     * 是否读取
     * @return
     */
    @Transient
    public String getIsReadView(){
    	SsmNoticeReadMode s = SsmNoticeReadMode.getNoticeReadMode(isRead);
        String str = "";
        if(s != null){
            str =  s.getDescription();
        }
        return str;
    }
}
