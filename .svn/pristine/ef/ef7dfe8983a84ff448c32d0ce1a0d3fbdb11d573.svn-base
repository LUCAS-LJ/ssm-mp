/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.disk.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssm.pay.common.orm.entity.SsmAutoEntity;
import com.ssm.pay.utils.SsmAppConstants;

/** 部门云盘存储空间配置
* @author Jiang.Li
* @version 2016年4月15日 上午10:06:39
*/
@Entity
@Table(name = "T_DISK_ORGAN_STORAGE")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class SsmOrganStorage extends SsmAutoEntity {
	 /**
     * 部门ID
     */
    private Long organId;
    /**
     * 最大限制大小 单位：M {@link com.eryansky.utils.AppConstants}
     */
    private Integer limitSize = SsmAppConstants.getDiskOrganLimitSize();

    public SsmOrganStorage(Long organId) {
        this.organId = organId;
    }

    public Long getOrganId() {
        return organId;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }

    public Integer getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(Integer limitSize) {
        this.limitSize = limitSize;
    }
}
