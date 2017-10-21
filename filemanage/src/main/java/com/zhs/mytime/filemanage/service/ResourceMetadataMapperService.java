package com.zhs.mytime.filemanage.service;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhs.mytime.filemanage.dao.ResourceMetadataMapper;
import com.zhs.mytime.filemanage.model.ResourceMetadata;


public class ResourceMetadataMapperService {
	

	@Autowired  ResourceMetadataMapper dao;

	
	public int deleteByPrimaryKey(String id){
		return dao.deleteByPrimaryKey(id);
	}

	public int insert(ResourceMetadata record){
		return dao.insert(record);
	}

	public int insertSelective(ResourceMetadata record){
		return dao.insertSelective(record);
	}

	public ResourceMetadata selectByPrimaryKey(String id){
		return dao.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(ResourceMetadata record){
		return dao.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(ResourceMetadata record){
		return dao.updateByPrimaryKey(record);
	}

	public void delete(Long id){
		dao.delete(id);
	}
}
