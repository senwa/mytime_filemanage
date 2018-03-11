package com.zhs.mytime.filemanage.dao;

import org.springframework.stereotype.Repository;

import com.zhs.mytime.filemanage.model.User;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    User getByAccountOrEmailOrPhoneOrUnionId(String account);
    
    User getByPhone(String phone);
    
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}