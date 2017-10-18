package com.zhs.mytime.filemanage.dao;

import org.springframework.dao.support.DaoSupport;

import com.zhs.mytime.filemanage.model.ResourceMetadata;

public class ResourceMetadataMapperDaoImpl extends DaoSupport implements ResourceMetadataMapper {

	@Override
	public int deleteByPrimaryKey(String id) {
		
		return 0;
	}

	@Override
	public int insert(ResourceMetadata record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(ResourceMetadata record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResourceMetadata selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(ResourceMetadata record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(ResourceMetadata record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void checkDaoConfig() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

}
