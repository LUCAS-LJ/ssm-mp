/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.notice.entity.common;

import java.util.Date;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssm.pay.common.orm.entity.SsmAutoEntity;
import com.ssm.pay.modules.notice._enum.SsmNoticeReadMode;
import com.ssm.pay.modules.notice.entity.SsmNotice;
import com.ssm.pay.modules.notice.utils.SsmNoticeUtils;

/** 通知实体基类
* @author Jiang.Li
* @version 2016年4月15日 上午10:53:37
*/
@MappedSuperclass
public class SsmBaseReferenceNotice extends SsmAutoEntity implements SsmINotice {

    private Long noticeId;

    private SsmNotice notice;

    /**
     * 带缓存查询
     * @return
     */
    @JsonIgnore
    @Transient
    public SsmNotice getNotice() {
        if(this.notice == null){
            this.notice = SsmNoticeUtils.getNotice(noticeId);
        }
        return notice;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    @Transient
    @Override
    public String getTitle() {
        return getNotice().getTitle();
    }

    @Transient
    @Override
    public String getType() {
        return getNotice().getType();
    }

    @Transient
    @Override
    public String getTypeView() {
        return getNotice().getTypeView();
    }

    @Transient
    @Override
    public String getContent() {
        return getNotice().getContent();
    }

    @Transient
    @Override
    public String getPublishUserName() {
        return getNotice().getPublishUserName();
    }

    @Transient
    @Override
    public String getPublishOrganName() {
        return getNotice().getPublishOrganName();
    }

    @Transient
    @Override
    public Date getPublishTime() {
        return getNotice().getPublishTime();
    }

    @Transient
    @Override
    public Integer getIsTop() {
        return getNotice().getIsTop();
    }

    @Transient
    @Override
    public String getIsTopView() {
        return getNotice().getIsTopView();
    }

    @Transient
    @Override
    public String getNoticeModeView() {
        return getNotice().getNoticeModeView();
    }



    /**
     * 判断当前登录用户是否读取
     * @return
     */
    @Transient
    public boolean isRead(){
        return SsmNoticeUtils.isRead(this.noticeId);
    }

    /**
     * 判断当前登录用户是否读取
     * @return
     */
    @Transient
    public String isReadView(){
        return this.isRead() == true ? SsmNoticeReadMode.readed.getDescription():SsmNoticeReadMode.unreaded.getDescription();
    }
}
