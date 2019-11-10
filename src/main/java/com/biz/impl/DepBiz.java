package com.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biz.IDepBiz;
import com.po.Dep;
import com.service.DaoService;
@Service("DepBiz")
@Transactional
public class DepBiz implements IDepBiz {
	@Resource(name="DaoService")
   private DaoService daoservice;
	
	public DaoService getDaoservice() {
		return daoservice;
	}

	public void setDaoservice(DaoService daoservice) {
		this.daoservice = daoservice;
	}

	@Override
	public List<Dep> findAll() {
		// TODO Auto-generated method stub
		return daoservice.getDepmapper().findAll();
	}

}
