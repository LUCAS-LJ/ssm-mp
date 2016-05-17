/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.notice.entity.common;

import java.util.Date;

/** 通知服务定义
* @author Jiang.Li
* @version 2016年4月15日 上午10:54:28
*/
public interface SsmINotice {
    String getTitle();
    String getType();
    String getTypeView();
    String getContent();
    String getPublishUserName();
    String getPublishOrganName();
    Date getPublishTime();
    Integer getIsTop();
    String getIsTopView();
    String getNoticeModeView();
}
