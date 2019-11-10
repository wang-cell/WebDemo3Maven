package com.action;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.po.Dep;
import com.po.Emp;
import com.po.PageBean;
import com.po.Welfare;
import com.service.BizService;
import com.util.AjAxUtils;
@Controller
public class EmpAction extends HttpServlet  {
	@Resource(name="BizService")
  private BizService bizService;
	
	public BizService getBizService() {
		return bizService;
	}

	public void setBizService(BizService bizService) {
		this.bizService = bizService;
	}

	@RequestMapping(value="save_Emp.do")
	public String save(HttpServletRequest request, HttpServletResponse response, Emp emp) {
		 System.out.println("saveAction");


        String  realpath = request.getRealPath("/");

        System.out.println(realpath);
			/***************** �ϴ��ļ� ************************************/
			// ��ȡ�ϴ���Ƭ�Ķ���
			MultipartFile multipartFile = emp.getPic();
			if (multipartFile != null && !multipartFile.isEmpty()) {
				// ��ȡ�ϴ����ļ�����
				String fname = multipartFile.getOriginalFilename();
				// ����
				if (fname.lastIndexOf(".") != -1) {// ���ں�׺
					// ��ȡ��׺��
					String ext = fname.substring(fname.lastIndexOf("."));

					// �жϺ�׺�Ƿ�Ϊjpg��ʽ
					if (ext.equalsIgnoreCase(".jpg")) {
						// ����
						String newfname = new Date().getTime() + ext;
						// �����ļ�����ָ���ϴ��ļ���·��
						File destFile = new File(realpath + "/uppic/" + newfname);
						// �ϴ�
						try {
							FileUtils.copyInputStreamToFile(
									multipartFile.getInputStream(), destFile);
							emp.setPhoto(newfname);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			/****************************************************************************/
		boolean flag=bizService.getEmpBiz().save(emp);
		if(flag){
			AjAxUtils.printString(response, ""+1);
		}else{
			AjAxUtils.printString(response, ""+0);
		}
		return null;
	}

	@RequestMapping(value="update_Emp.do")
	public String update(HttpServletRequest request, HttpServletResponse response, Emp emp) {
		System.out.println("updateAction");
        Set<String>  realpath = this.getServletContext().getResourcePaths("/WEB-INF");

        System.out.println(realpath);
		 //��ȡԭ���ϴ�����Ƭ
		 String oldphoto=bizService.getEmpBiz().findById(emp.getEid()).getPhoto();
			/***************** �ϴ��ļ� ************************************/
			// ��ȡ�ϴ���Ƭ�Ķ���
			MultipartFile multipartFile = emp.getPic();
			if (multipartFile != null && !multipartFile.isEmpty()) {
				// ��ȡ�ϴ����ļ�����
				String fname = multipartFile.getOriginalFilename();
				// ����
				if (fname.lastIndexOf(".") != -1) {// ���ں�׺
					// ��ȡ��׺��
					String ext = fname.substring(fname.lastIndexOf("."));

					// �жϺ�׺�Ƿ�Ϊjpg��ʽ
					if (ext.equalsIgnoreCase(".jpg")) {
						// ����
						String newfname = new Date().getTime() + ext;
						// �����ļ�����ָ���ϴ��ļ���·��
						File destFile = new File(realpath + "/uppic/" + newfname);
						// �ϴ�
						try {
							FileUtils.copyInputStreamToFile(
									multipartFile.getInputStream(), destFile);
							emp.setPhoto(newfname);
							//ɾ��ԭ������Ƭ
							File oldFile = new File(realpath + "/uppic/" + oldphoto);
							if(oldFile.exists()&&!oldphoto.equalsIgnoreCase("default.jpg")){
								oldFile.delete();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				emp.setPhoto(oldphoto);
			}
			/****************************************************************************/
		
		boolean flag=bizService.getEmpBiz().update(emp);
		if(flag){
			AjAxUtils.printString(response, ""+1);
		}else{
			AjAxUtils.printString(response, ""+0);
		}
		return null;
	}

	@RequestMapping(value="delById_Emp.do")
	public String delById(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		boolean flag=bizService.getEmpBiz().delById(eid);
		if(flag){
			AjAxUtils.printString(response, ""+1);
		}else{
			AjAxUtils.printString(response, ""+0);
		}
		return null;
	}

	@RequestMapping(value="findById_Emp.do")
	public String findById(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		Emp oldemp=bizService.getEmpBiz().findById(eid);
		PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
		String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
		AjAxUtils.printString(response, jsonstr);
		return null;
	}

	@RequestMapping(value="findPageAll_Emp.do")
	public String findPageAll(HttpServletRequest request, HttpServletResponse response, Integer page, Integer rows) {
		Map<String,Object> map=new HashMap<String,Object>();
		PageBean pb=new PageBean();
		page=page==null || page<1?pb.getPage():page;
		rows=rows==null || rows<1?pb.getRows():rows;
		if(rows>10) rows=10;
		pb.setPage(page);
		pb.setRows(rows);
		//��ȡ��ǰҳ��¼����
		List<Emp> lsemp=bizService.getEmpBiz().findPageAll(pb);
		//��ȡ�ܼ�¼��
		int maxrows=bizService.getEmpBiz().findMaxRows();
		map.put("page", page);
		map.put("rows",lsemp);
		map.put("total", maxrows);
		PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
		String jsonstr=JSONObject.toJSONString(map,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
		AjAxUtils.printString(response, jsonstr);
		return null;
	}

	@RequestMapping(value="doint_Emp.do")
	public String doinit(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map=new HashMap<String,Object>();
		List<Dep> lsdep=bizService.getDepBiz().findAll();
		List<Welfare> lswf=bizService.getWelfareBiz().findAll();
		map.put("lsdep", lsdep);
		map.put("lswf", lswf);
		PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
		String jsonstr=JSONObject.toJSONString(map,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
		AjAxUtils.printString(response, jsonstr);
		return null;
	}

	@RequestMapping(value="findDetail_Emp.do")
	public String findDetail(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		Emp oldemp=bizService.getEmpBiz().findById(eid);
		PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
		String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
		AjAxUtils.printString(response, jsonstr);
		return null;
	}

}
