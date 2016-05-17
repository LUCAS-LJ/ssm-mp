/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.orm.entity;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** 抽象实体基类--抽象工厂模式
* @author Jiang.Li
* @version 2016年1月28日 下午11:10:55
*/
public abstract class SsmAbstractEntity<ID extends Serializable> {
	public abstract ID getId();

    /**
     * 设置主键ID.
     *
     * @param id 主键ID
     */
    public abstract void setId(final ID id);

    /**
     * 是否是新创建的对象.
     * @return
     */
    @JsonIgnore
    public boolean isNew() {
        return null == getId();
    }

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        SsmAbstractEntity<?> that = (SsmAbstractEntity<?>) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
