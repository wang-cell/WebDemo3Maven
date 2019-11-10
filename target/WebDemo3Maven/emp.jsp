<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>emp</title>
<!-- 引入easyui -->
<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css"/>
<script type="text/javascript" src="easyui/jquery-1.9.1.js"></script>
<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
/*  准备复选框的值*/
$(function(){
	$("#btupdate").attr("disabled",true);
	$('#winempdetail').window('close');  
	//生成复选框
	$.getJSON('doint_Emp.do',function(map){
		var lswf=map.lswf;
		var lsdep=map.lsdep;
		for (var i = 0; i < lswf.length; i++) {
			var wf=lswf[i];
			$("#wf").append("<input type='checkbox' name='wids' id='wids' value='"+wf.wid+"'/>"+wf.wname);
		}
		$('#depid').combobox({    
		    data:lsdep,    
		    valueField:'depid',    
		    textField:'depname',
		    value:1,
		    panelWidth:80,
		    width:80,
		    panelHeight:80
		}); 
	});
});
/* 员工列表 */
 $(function(){
	 $('#dg').datagrid({    
		    url:'findPageAll_Emp.do',
		    striped:true,
		    singleSelect:true,
		    fixed:true,
		    pagination:true,
		    pageList:[5,10,15,20],
		    pageSize:5,
		    columns:[[    
		        {field:'eid',title:'编号',width:100,align:'center'},
		        {field:'ename',title:'姓名',width:100,align:'center'},
		        {field:'sex',title:'性别',width:100,align:'center'},
		        {field:'address',title:'地址',width:100,align:'center'},
		        {field:'sdate',title:'生日',width:100,align:'center'},
		        {field:'photo',title:'照片',width:100,align:'center',
		        	formatter:function(value,row,index){
		        		return '<img src=uppic/'+row.photo+' width="60" height="50">';
		        	}
		        },
		        
		        {field:'depname',title:'部门',width:100,align:'center'},
		        {field:'opt',title:'操作',width:200,align:'center',
		        	formatter:function(value,row,index){
						var bt1='<input type="button" value="删除" onclick="dodelById('+row.eid+')">';
						var bt2='<input type="button" value="编辑" onclick="findById('+row.eid+')">';
						var bt3='<input type="button" value="详细" onclick="findDetail('+row.eid+')">';
						return bt1+'&nbsp;'+bt2+'&nbsp;'+bt3;
					}	
		        }
		      
		       
		        
		       
		    ]]    
		});  

 });
/* 员工列表end */
/* 删除查找 */
 function dodelById(eid){
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		    if (r){    
		        $.get('delById_Emp.do?eid='+eid,function(code){
		        	if(code=='1'){
		        		$.messager.alert('提示','删除成功');
						$('#dg').datagrid('reload');    // 重新载入当前页面数据  
		        	}else{
		        		$.messager.alert('提示','删除失败');
		        	}
		        });    
		    }    
		});  

}
function findById(eid){
	alert(111111111111111);
	$("#btupdate").attr("disabled",false);
	$("#btsave").attr("disabled",true);
	$.getJSON('findById_Emp.do?eid='+eid,function(emp){
		//删除复选框所有选中
		$(":checkbox[name='wids']").each(function(){
			$(this).prop("checked",false);
		});
		$('#ffemp').form('load',{
			'eid':emp.eid,
			'ename':emp.ename,
			'sex':emp.sex,
			'address':emp.address,
			'sdate':emp.sdate,
			'depid':emp.depid,
			'emoney':emp.emoney,
		});
        $("#imgg").attr('src','uppic/'+emp.photo);
        var wids=emp.wids;
        //设置复选框选中
        $(":checkbox[name='wids']").each(function(){
			for (var i = 0; i < wids.length; i++) {
				if($(this).val()==wids[i]){
					$(this).prop("checked",true);	
				}
			}
		});
        
	});
	alert("222222222222222");
}

function findDetail(eid){
	alert(111111111111111);
	$.getJSON('findDetail_Emp.do?eid='+eid,function(emp){
		//删除复选框所有选中
		$(":checkbox[name='wids']").each(function(){
			$(this).prop("checked",false);
		});
		//详细内容
		$("#nametext").html(emp.ename);
		$("#sextext").html(emp.sex);
		$("#addresstext").html(emp.address);
		$("#sdatetext").html(emp.sdate);
		$("#phototext").html(emp.photo);
		$("#depnametext").html(emp.depname);
		$("#emoneytext").html(emp.emoney);
       
        var lswf=emp.lswf;
        var wnames=[];//福利名称数组
        //设置复选框选中
      for (var i = 0; i < lswf.length; i++) {
		var wf=lswf[i];
		wnames.push(wf.wname);
	}
      var strwname= wnames.join(',');
      $("#wftext").html(strwname);
      $("#dyimgg").attr('src','uppic/'+emp.photo);
      $('#winempdetail').window('open');
	});
	alert("222222222222222");
}
 /* 删除查找end */
/* 保存与修改 */
$(function(){
	$("#btsave").click(function(){
		$.messager.progress();	// 显示进度条
		$('#ffemp').form('submit', {
			url: 'save_Emp.do',
			onSubmit: function(){
				var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
				}
				return isValid;	// 返回false终止表单提交
			},
			success: function(code){
				if(code=='1'){
					$.messager.alert('提示','提交成功');
					$("#ffemp").form("reset");
					$('#dg').datagrid('reload');    // 重新载入当前页面数据  
				}else{
					$.messager.alert('提示','提交失败');  
				}
				$.messager.progress('close');	// 如果提交成功则隐藏进度条
			}
		});
	});
	$("#btupdate").click(function(){
		$.messager.progress();	// 显示进度条
		$('#ffemp').form('submit', {
			url: 'update_Emp.do',
			onSubmit: function(){
				var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
				}
				return isValid;	// 返回false终止表单提交
			},
			success: function(code){
				$.messager.alert('提示返回',code);
				if(code=='1'){
					$.messager.alert('提示','修改成功');
					$("#ffemp").form("reset");
					$("#btsave").attr("disabled",false);
					$('#dg').datagrid('reload');    // 重新载入当前页面数据  
				}else{
					$.messager.alert('提示','修改失败');  
				}
				$.messager.progress('close');	// 如果提交成功则隐藏进度条
			}
		});
	});
});

 


/* 保存与修改end */
</script>
</head>
<body>
<p align="center">员工列表</p>
<hr/>
<table id="dg"></table>
<p></p>
<form method="post" enctype="multipart/form-data" name="ffemp" id="ffemp">
  <div align="center">
    <table width="600" border="1">
      <tr>
        <td colspan="3"><div align="center">员工管理</div></td>
      </tr>
      <tr>
        <td width="89">姓名</td>
        <td width="247">
        <input type="hidden" name="eid" id="eid">
        <input type="text" name="ename" id="ename" class="easyui-validatebox" data-options="required:true,missingMessage:'姓名'" ></td>
        
        <td width="242" rowspan="7">
        <img id="imgg" alt="图片不存在" src="uppic/default.jpg" width="100%" height="200">
        </td>
      </tr>
      <tr>
        <td>性别</td>
        <td><input type="radio" name="sex" id="sex" value="男" checked="checked">男
        <input type="radio" name="sex" id="sex1" value="女">女
        </td>
      </tr>
      <tr>
        <td>地址</td>
        <td>
        <input type="text" name="address" id="address"></td>
      </tr>
      <tr>
        <td>生日</td>
        <td>
        <input type="date" name="sdate" id="sdate"></td>
      </tr>
      <tr>
        <td>照片</td>
        <td>
        <input type="file" name="pic" id="pic"></td>
      </tr>
      <tr>
        <td>部门</td>
        <td>
          <select name="depid" id="depid">
        </select></td>
      </tr>
      <tr>
        <td>薪资</td>
        <td>
        <input type="text" name="emoney" id="emoney" value="2000"></td>
      </tr>
      <tr>
        <td>福利</td>
        <td colspan="2">
       <span id="wf"></span>
       </td>
      </tr>
      <tr>
        <td colspan="3"><div align="center">
          <input type="button" name="btsave" id="btsave" value="保存">
          <input type="button" name="btupdate" id="btupdate" value="修改">
          <input type="reset" name="button3" id="button3" value="取消">
        </div></td>
      </tr>
    </table>
  </div>
</form>
<!--加入提示窗口，用于显示详细信息  -->
<div id="winempdetail" class="easyui-window" title="员工的详细窗口" style="width:600px;height:400px"   
        data-options="iconCls:'icon-save',modal:true">   
       <table width="600" border="1">
      <tr>
        <td colspan="3"><div align="center">员工管理</div></td>
      </tr>
      <tr>
        <td width="89">姓名</td>
        <td width="247">
        <span id="nametext"></span>
        </td>     
        <td width="242" rowspan="7">
        <img id="dyimgg" alt="图片不存在" src="uppic/default.jpg" width="100%" height="200">
        </td>
      </tr>
      <tr>
        <td>性别</td>
        <td>
        <span id="sextext"></span>
        </td>
      </tr>
      <tr>
        <td>地址</td>
        <td>
        <span id="addresstext"></span></td>
      </tr>
      <tr>
        <td>生日</td>
        <td>
       <span id="sdatetext"></span></td>
      </tr>
      <tr>
        <td>照片</td>
        <td>
        <span id="phototext"></span></td>
      </tr>
      <tr>
        <td>部门</td>
        <td>
          <span id="depnametext"></span></td>
      </tr>
      <tr>
        <td>薪资</td>
        <td>
        <span id="emoneytext"></span></td>
      </tr>
      <tr>
        <td>福利</td>
        <td colspan="2">
       <span id="wftext"></span>
       </td>
      </tr>
    </table>
</div>  
</body>
</html>