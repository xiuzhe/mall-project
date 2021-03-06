<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8">
  <meta name="renderer" content="webkit|ie-comp|ie-stand">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport"
        content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,member-scalable=no"/>
  <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <link href="${pageContext.request.contextPath}/resources/css/H-ui.min.css" rel="stylesheet" type="text/css"/>
  <link href="${pageContext.request.contextPath}/resources/css/H-ui.admin.css" rel="stylesheet" type="text/css"/>
  <link href="${pageContext.request.contextPath}/resources/lib/icheck/icheck.css" rel="stylesheet" type="text/css"/>
  <link href="${pageContext.request.contextPath}/resources/lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet"
        type="text/css"/>
  <title>修改信息</title>
</head>
<body>
<div class="pd-20">
  <form action="${pageContext.request.contextPath}/manageUser/updatePassword.dhtml" method="post" class="form form-horizontal" id="form-member-update">
    <div class="row cl">
      <div class="formControls col-5">
        <span style="color:#CC0000" id="msg"></span>
      </div>
      <div class="col-4"></div>
    </div>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>登录账号：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text" value="${manageUserDTO.username}" id="username" name="username" readonly="readonly" style="border-style:none;" >
      </div>
      <div class="col-4"></div>
    </div>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>原密码：</label>
      <div class="formControls col-5">
        <input type="password" class="input-text" autocomplete="off" placeholder="" name="oldPassword" id="oldPassword" datatype="*6-18" nullmsg="原密码不能为空" >
      </div>
      <div class="col-4"></div>
    </div>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>密码：</label>
      <div class="formControls col-5">
        <input type="password" class="input-text" autocomplete="off" placeholder="" name="password" id="password" datatype="*6-18" nullmsg="密码不能为空" >
      </div>
      <div class="col-4"> </div>
    </div>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>确认密码：</label>
      <div class="formControls col-5">
        <input type="password" class="input-text" autocomplete="off" placeholder="" name="repassword" id="repassword" recheck="password" datatype="*6-18" errormsg="您两次输入的密码不一致！"nullmsg="请确认密码">
      </div>
      <div class="col-4"> </div>
    </div>
    <div class="row cl">
      <div class="col-9 col-offset-3">
        <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
      </div>
    </div>
  </form>
</div>
</div>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/lib/icheck/jquery.icheck.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/lib/Validform/5.3.2/Validform.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/H-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/H-ui.admin.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/area.js"></script>
<script type="text/javascript">
  $(function(){
    $('.skin-minimal input').iCheck({
      checkboxClass: 'icheckbox-blue',
      radioClass: 'iradio-blue',
      increaseArea: '20%'
    });

    $("#form-member-update").Validform({
      tiptype:2,
      ajaxPost:true,
      callback:function(data){
        if(data){
          setTimeout(function(){
            //$.Hidemsg();
            $.Showmsg("修改成功！");
          },2000);
          setTimeout(function(){
            var index = parent.layer.getFrameIndex(window.name);
            parent.window.location.reload();
            parent.layer.close(index);
          },3000);
        }else{
          $("#msg").html("原始密码错误");
          $("#Validform_msg").hide();
        }
      }
    });
  });
</script>
</body>
</html>
