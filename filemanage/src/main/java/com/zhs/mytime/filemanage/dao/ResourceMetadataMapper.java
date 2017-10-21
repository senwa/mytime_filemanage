package com.zhs.mytime.filemanage.dao;

import com.zhs.mytime.filemanage.model.ResourceMetadata;

public interface ResourceMetadataMapper {
    int deleteByPrimaryKey(String id);

    int insert(ResourceMetadata record);

    int insertSelective(ResourceMetadata record);

    ResourceMetadata selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ResourceMetadata record);

    int updateByPrimaryKeyWithBLOBs(ResourceMetadata record);

    int updateByPrimaryKey(ResourceMetadata record);
}