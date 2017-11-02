package com.zhs.mytime.filemanage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhs.mytime.filemanage.dao.ResourceMetadataMapper;
import com.zhs.mytime.filemanage.model.ResourceMetadata;

@Service
public class ResourceMetadataMapperService {
	

	@Autowired  ResourceMetadataMapper dao;

	@Transactional
	public int deleteByPrimaryKey(String id){
		return dao.deleteByPrimaryKey(id);
	}

	@Transactional
	public int insert(ResourceMetadata record){
		return dao.insert(record);
	}

	@Transactional
	public int insertSelective(ResourceMetadata record){
		return dao.insertSelective(record);
	}

	public ResourceMetadata selectByPrimaryKey(String id){
		return dao.selectByPrimaryKey(id);
	}

	@Transactional
	public int updateByPrimaryKeySelective(ResourceMetadata record){
		return dao.updateByPrimaryKeySelective(record);
	}

	@Transactional
	public int updateByPrimaryKey(ResourceMetadata record){
		return dao.updateByPrimaryKey(record);
	}

}
