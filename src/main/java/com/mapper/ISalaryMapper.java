package com.mapper;

import org.springframework.stereotype.Service;

import com.po.Salary;

@Service("SalaryDao")
public interface ISalaryMapper {
    //����н��
	public int save(Salary sa);
	//����Ա����ţ�����н��
	public int updateByEid(Salary sa);
	//����Ա����ţ�ɾ��н��
	public int delByEid(Integer eid);
	//����Ա����ţ���ȡн��
	public Salary findByEid(Integer eid);
}
