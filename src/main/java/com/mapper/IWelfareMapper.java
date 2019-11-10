package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.Dep;
import com.po.Welfare;

@Service("WelfareDao")
public interface IWelfareMapper {
  public List<Welfare> findAll();
}
