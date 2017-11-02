package com.zhs.mytime.filemanage.dao;

import com.zhs.mytime.filemanage.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    User getByAccountOrEmailOrPhoneOrUnionId(String accountOrOtherInfo);
    
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}