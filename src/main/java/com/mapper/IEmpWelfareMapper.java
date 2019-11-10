package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.Dep;
import com.po.EmpWelfare;
import com.po.Welfare;

@Service("EmpWelfareDao")
public interface IEmpWelfareMapper {
  //����Ա������
	public int save(EmpWelfare ewf);
	//����Ա����ţ�ɾ������
	public int delByEid(Integer eid);
	//����Ա����ţ���ѯ��������
	public List<Welfare> findByEid(Integer eid);
}
