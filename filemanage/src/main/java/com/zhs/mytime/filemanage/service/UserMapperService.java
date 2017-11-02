package com.zhs.mytime.filemanage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhs.mytime.filemanage.dao.ResourceMetadataMapper;
import com.zhs.mytime.filemanage.dao.UserMapper;
import com.zhs.mytime.filemanage.model.ResourceMetadata;
import com.zhs.mytime.filemanage.model.User;

@Service
public class UserMapperService {
	

	@Autowired  UserMapper dao;

	@Transactional
	public int deleteByPrimaryKey(String id){
		return dao.deleteByPrimaryKey(id);
	}
	
	@Transactional
	public int insert(User record){
		return dao.insert(record);
	}

	@Transactional
	public int insertSelective(User record){
		return dao.insertSelective(record);
	}
	
	@Transactional
	public User selectByPrimaryKey(String id){
		return dao.selectByPrimaryKey(id);
	}
	
	@Transactional
	public User getByAccountOrEmailOrPhoneOrUnionId(String accountOrOtherInfo){
		return dao.getByAccountOrEmailOrPhoneOrUnionId(accountOrOtherInfo);
	}
	
	@Transactional
	public int updateByPrimaryKeySelective(User record){
		return dao.updateByPrimaryKeySelective(record);
	}
	

	@Transactional
	public int updateByPrimaryKey(User record){
		return dao.updateByPrimaryKey(record);
	}
}
