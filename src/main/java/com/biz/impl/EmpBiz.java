package com.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biz.IEmpBiz;
import com.po.Emp;
import com.po.EmpWelfare;
import com.po.PageBean;
import com.po.Salary;
import com.po.Welfare;
import com.service.DaoService;
@Service("EmpBiz")
@Transactional
public class EmpBiz implements IEmpBiz {
	@Resource(name="DaoService")
	   private DaoService daoservice;
		
		public DaoService getDaoservice() {
			return daoservice;
		}

		public void setDaoservice(DaoService daoservice) {
			this.daoservice = daoservice;
		}
	@Override
	public boolean save(Emp emp) {
		int code=daoservice.getEmpmapper().save(emp);
		//获取添加emp的eid(薪资和福利)
		if(code>0){
			Integer eid=daoservice.getEmpmapper().findMaxEid();
			/*******保存薪资*******/
			Salary sa=new Salary(eid,emp.getEmoney());
			daoservice.getSalarymapper().save(sa);
			/*******保存薪资end*******/
			/*******保存选择的福利数组，从复选框*******/
			String[] wids=emp.getWids();
			if(wids!=null&&wids.length>0){
				for (int i = 0; i < wids.length; i++) {
					EmpWelfare ewf=new EmpWelfare(eid,new Integer(wids[i]));
					daoservice.getEmpwelfaremapper().save(ewf);
				}
			}
			/*******保存选择的福利数组，从复选框end*******/
			return true;
		}
		return false;
	}

	@Override
	public boolean update(Emp emp) {
		int code=daoservice.getEmpmapper().update(emp);
		if(code>0){
			/*********更新薪资*************/
			   //获取原来的员工薪资
			Salary oldSalary=daoservice.getSalarymapper().findByEid(emp.getEid());
			if(oldSalary!=null&&oldSalary.getEmoney()!=null){
				oldSalary.setEmoney(emp.getEmoney());
				daoservice.getSalarymapper().updateByEid(oldSalary);
			}else{
				Salary sa=new Salary(emp.getEid(),emp.getEmoney());
				daoservice.getSalarymapper().save(sa);
			}
			/*********更新薪资end*************/
			/*********更新福利(员工福利表)*************/
			  //获取原来的福利
			List<Welfare> lswf=daoservice.getEmpwelfaremapper().findByEid(emp.getEid());
			if(lswf!=null&&lswf.size()>0){
				//删除原有福利
				daoservice.getEmpwelfaremapper().delByEid(emp.getEid());	
			}
			//添加新的福利数组
			String[] wids=emp.getWids();
			if(wids!=null&&wids.length>0){
				for (int i = 0; i < wids.length; i++) {
					EmpWelfare ewf=new EmpWelfare(emp.getEid(),new Integer(wids[i]));
					daoservice.getEmpwelfaremapper().save(ewf);
				}
			}
			/*********更新福利end*************/
			return true;
		}
		return false;
	}

	@Override
	public boolean delById(Integer eid) {
		//删除从表数据
		daoservice.getSalarymapper().delByEid(eid);
		daoservice.getEmpwelfaremapper().delByEid(eid);
		//删除员工数据
		int code=daoservice.getEmpmapper().delById(eid);
		if(code>0){
			return true;
		}
		return false;
	}

	@Override
	public Emp findById(Integer eid) {
		//获取员工对象
		Emp emp=daoservice.getEmpmapper().findById(eid);
		/*********获取该员工的薪资，为修改页面准备值*************/
		 Salary sa=daoservice.getSalarymapper().findByEid(eid);
		 if(sa!=null&&sa.getEmoney()!=null){
			 emp.setEmoney(sa.getEmoney());
		 }
		/*********获取该员工的薪资，为修改页面准备值end*************/
		/*********获取该员工的福利，为修改页面准备值*************/
		//获取原来的福利
			List<Welfare> lswf=daoservice.getEmpwelfaremapper().findByEid(eid);
			if(lswf!=null&&lswf.size()>0){
				//创建编号数组
				String[] wids=new String[lswf.size()];
				for (int i = 0; i < wids.length; i++) {
					Welfare wf=lswf.get(i);
					wids[i]=wf.getWid().toString();
				}
				emp.setWids(wids);
			}
			//更详细数据
			emp.setLswf(lswf);
		/*********获取该员工的福利，为修改页面准备值end*************/
		return emp;
	}

	@Override
	public List<Emp> findPageAll(PageBean pb) {
		if(pb==null){
			pb=new PageBean();
		}
		return daoservice.getEmpmapper().findPageAll(pb);
	}

	@Override
	public int findMaxRows() {
		// TODO Auto-generated method stub
		return daoservice.getEmpmapper().findMaxRows();
	}

}
