/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssm.pay.common.orm.entity.SsmAutoEntity;
import com.ssm.pay.utils.SsmAppConstants;

/** 用户云盘存储空间配置
* @author Jiang.Li
* @version 2016年4月14日 下午5:44:02
*/
@Entity
@Table(name = "T_DISK_USER_STORAGE")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class SsmUserStorage extends SsmAutoEntity {
	/**
     * 用户ID
     */
    private Long userId;
    /**
     * 最大限制大小 单位：M {@link com.eryansky.utils.AppConstants}
     */
    private Integer limitSize = SsmAppConstants.getDiskUserLimitSize();

    public SsmUserStorage(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(Integer limitSize) {
        this.limitSize = limitSize;
    }
}
