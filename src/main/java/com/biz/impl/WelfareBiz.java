package com.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biz.IWelfareBiz;
import com.po.Welfare;
import com.service.DaoService;
@Service("WelfareBiz")
@Transactional
public class WelfareBiz implements IWelfareBiz {
	@Resource(name="DaoService")
	   private DaoService daoservice;
		
		public DaoService getDaoservice() {
			return daoservice;
		}

		public void setDaoservice(DaoService daoservice) {
			this.daoservice = daoservice;
		}
	@Override
	public List<Welfare> findAll() {
		// TODO Auto-generated method stub
		return daoservice.getWelfaremapper().findAll();
	}

}
