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
		//��ȡ���emp��eid(н�ʺ͸���)
		if(code>0){
			Integer eid=daoservice.getEmpmapper().findMaxEid();
			/*******����н��*******/
			Salary sa=new Salary(eid,emp.getEmoney());
			daoservice.getSalarymapper().save(sa);
			/*******����н��end*******/
			/*******����ѡ��ĸ������飬�Ӹ�ѡ��*******/
			String[] wids=emp.getWids();
			if(wids!=null&&wids.length>0){
				for (int i = 0; i < wids.length; i++) {
					EmpWelfare ewf=new EmpWelfare(eid,new Integer(wids[i]));
					daoservice.getEmpwelfaremapper().save(ewf);
				}
			}
			/*******����ѡ��ĸ������飬�Ӹ�ѡ��end*******/
			return true;
		}
		return false;
	}

	@Override
	public boolean update(Emp emp) {
		int code=daoservice.getEmpmapper().update(emp);
		if(code>0){
			/*********����н��*************/
			   //��ȡԭ����Ա��н��
			Salary oldSalary=daoservice.getSalarymapper().findByEid(emp.getEid());
			if(oldSalary!=null&&oldSalary.getEmoney()!=null){
				oldSalary.setEmoney(emp.getEmoney());
				daoservice.getSalarymapper().updateByEid(oldSalary);
			}else{
				Salary sa=new Salary(emp.getEid(),emp.getEmoney());
				daoservice.getSalarymapper().save(sa);
			}
			/*********����н��end*************/
			/*********���¸���(Ա��������)*************/
			  //��ȡԭ���ĸ���
			List<Welfare> lswf=daoservice.getEmpwelfaremapper().findByEid(emp.getEid());
			if(lswf!=null&&lswf.size()>0){
				//ɾ��ԭ�и���
				daoservice.getEmpwelfaremapper().delByEid(emp.getEid());	
			}
			//����µĸ�������
			String[] wids=emp.getWids();
			if(wids!=null&&wids.length>0){
				for (int i = 0; i < wids.length; i++) {
					EmpWelfare ewf=new EmpWelfare(emp.getEid(),new Integer(wids[i]));
					daoservice.getEmpwelfaremapper().save(ewf);
				}
			}
			/*********���¸���end*************/
			return true;
		}
		return false;
	}

	@Override
	public boolean delById(Integer eid) {
		//ɾ���ӱ�����
		daoservice.getSalarymapper().delByEid(eid);
		daoservice.getEmpwelfaremapper().delByEid(eid);
		//ɾ��Ա������
		int code=daoservice.getEmpmapper().delById(eid);
		if(code>0){
			return true;
		}
		return false;
	}

	@Override
	public Emp findById(Integer eid) {
		//��ȡԱ������
		Emp emp=daoservice.getEmpmapper().findById(eid);
		/*********��ȡ��Ա����н�ʣ�Ϊ�޸�ҳ��׼��ֵ*************/
		 Salary sa=daoservice.getSalarymapper().findByEid(eid);
		 if(sa!=null&&sa.getEmoney()!=null){
			 emp.setEmoney(sa.getEmoney());
		 }
		/*********��ȡ��Ա����н�ʣ�Ϊ�޸�ҳ��׼��ֵend*************/
		/*********��ȡ��Ա���ĸ�����Ϊ�޸�ҳ��׼��ֵ*************/
		//��ȡԭ���ĸ���
			List<Welfare> lswf=daoservice.getEmpwelfaremapper().findByEid(eid);
			if(lswf!=null&&lswf.size()>0){
				//�����������
				String[] wids=new String[lswf.size()];
				for (int i = 0; i < wids.length; i++) {
					Welfare wf=lswf.get(i);
					wids[i]=wf.getWid().toString();
				}
				emp.setWids(wids);
			}
			//����ϸ����
			emp.setLswf(lswf);
		/*********��ȡ��Ա���ĸ�����Ϊ�޸�ҳ��׼��ֵend*************/
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
