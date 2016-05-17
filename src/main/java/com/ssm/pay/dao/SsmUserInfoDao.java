package com.ssm.pay.dao;

import com.ssm.pay.common.model.SsmUserInfo;

public interface SsmUserInfoDao {
	
	int deleteByPrimaryKey(Integer id);

    int insert(SsmUserInfo record);

    int insertSelective(SsmUserInfo record);

    SsmUserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SsmUserInfo record);

    int updateByPrimaryKey(SsmUserInfo record);
    
}
