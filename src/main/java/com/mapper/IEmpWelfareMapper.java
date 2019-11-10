package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.Dep;
import com.po.EmpWelfare;
import com.po.Welfare;

@Service("EmpWelfareDao")
public interface IEmpWelfareMapper {
  //保存员工福利
	public int save(EmpWelfare ewf);
	//根据员工编号，删除福利
	public int delByEid(Integer eid);
	//根据员工编号，查询福利集合
	public List<Welfare> findByEid(Integer eid);
}
