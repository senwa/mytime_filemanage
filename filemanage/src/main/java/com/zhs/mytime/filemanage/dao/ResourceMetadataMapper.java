package com.zhs.mytime.filemanage.dao;

import org.springframework.stereotype.Repository;

import com.zhs.mytime.filemanage.model.ResourceMetadata;

@Repository
public interface ResourceMetadataMapper {
    int deleteByPrimaryKey(String id);

    int insert(ResourceMetadata record);

    int insertSelective(ResourceMetadata record);

    ResourceMetadata selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ResourceMetadata record);

    int updateByPrimaryKeyWithBLOBs(ResourceMetadata record);

    int updateByPrimaryKey(ResourceMetadata record);
}