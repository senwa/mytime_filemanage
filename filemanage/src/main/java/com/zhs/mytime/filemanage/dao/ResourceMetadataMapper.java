package com.zhs.mytime.filemanage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.zhs.mytime.filemanage.model.ResourceMetadata;


public interface ResourceMetadataMapper {
	

	int deleteByPrimaryKey(String id);

	int insert(ResourceMetadata record);

	int insertSelective(ResourceMetadata record);

	ResourceMetadata selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(ResourceMetadata record);

	int updateByPrimaryKey(ResourceMetadata record);

	void delete(Long id);
}
