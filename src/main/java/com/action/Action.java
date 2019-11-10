package com.action;


import javax.servlet.http.*;


import com.po.Emp;

public interface Action {
  public String save(HttpServletRequest request, HttpServletResponse response, Emp emp);
  public String update(HttpServletRequest request, HttpServletResponse response, Emp emp);
  public String delById(HttpServletRequest request, HttpServletResponse response, Integer eid);
  public String findById(HttpServletRequest request, HttpServletResponse response, Integer eid);
  public String findPageAll(HttpServletRequest request, HttpServletResponse response, Integer page, Integer rows);
  public String doinit(HttpServletRequest request, HttpServletResponse response);
  public String findDetail(HttpServletRequest request, HttpServletResponse response, Integer eid);
}
