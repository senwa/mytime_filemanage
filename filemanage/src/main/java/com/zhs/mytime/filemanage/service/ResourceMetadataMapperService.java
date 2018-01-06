package com.zhs.mytime.filemanage.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhs.mytime.filemanage.comm.FastDFSClientWrapper;
import com.zhs.mytime.filemanage.dao.ResourceMetadataMapper;
import com.zhs.mytime.filemanage.model.ResourceMetadata;

@Service
public class ResourceMetadataMapperService {
	

	@Autowired  ResourceMetadataMapper dao;

	@Autowired FastDFSClientWrapper fastDFSClientWrapper;
	
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
	
	public List<ResourceMetadata> getListByParam(Map<String,Object> param){
		
		List<ResourceMetadata> list = dao.getListByParam(param);
		return list;
	}
	
	public List<String> getYearMonthDayByParam(Map<String,Object> param){
		
		List<String> list = dao.getYearMonthDayByParam(param);
		return list;
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
