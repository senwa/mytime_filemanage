package com.zhs.mytime.filemanage.dao;

import java.util.List;
import java.util.Map;

import com.zhs.mytime.filemanage.model.ResourceMetadata;

public interface ResourceMetadataMapper {
    
	    
	 List<ResourceMetadata> getListByParam(Map<String,Object> param);
	 
	 List<String> getYearMonthDayByParam(Map<String,Object> param);
	
	/**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_meta_data
     *
     * @mbggenerated Sat Dec 23 23:42:08 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_meta_data
     *
     * @mbggenerated Sat Dec 23 23:42:08 CST 2017
     */
    int insert(ResourceMetadata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_meta_data
     *
     * @mbggenerated Sat Dec 23 23:42:08 CST 2017
     */
    int insertSelective(ResourceMetadata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_meta_data
     *
     * @mbggenerated Sat Dec 23 23:42:08 CST 2017
     */
    ResourceMetadata selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_meta_data
     *
     * @mbggenerated Sat Dec 23 23:42:08 CST 2017
     */
    int updateByPrimaryKeySelective(ResourceMetadata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_meta_data
     *
     * @mbggenerated Sat Dec 23 23:42:08 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(ResourceMetadata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_meta_data
     *
     * @mbggenerated Sat Dec 23 23:42:08 CST 2017
     */
    int updateByPrimaryKey(ResourceMetadata record);
}