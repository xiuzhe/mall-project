<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/9/1
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="${pageContext.request.contextPath}/resources/css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/lib/icheck/icheck.css" rel="stylesheet" type="text/css" />

<head>
  <meta name="renderer" content="webkit|ie-comp|ie-stand">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>注册申请</title>
  <meta name="keywords" content="享买开放平台">
  <meta name="description" content="享买开放平台">
</head>

<body>
<div class="pd-20">
  <form action="${pageContext.request.contextPath}/channel/saveChannel.dhtml" method="post" class="form form-horizontal" id="form-member-add">
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>登录账号：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text" value="" placeholder="" id="name" name="name" datatype="*2-16" nullmsg="登录账号不能为空" ajaxurl="checkLoginAccount.dhtml" sucmsg="验证通过！">
      </div>
      <div class="col-4"> </div>
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
      <label class="form-label col-3"><span class="c-red">*</span>选择用户类型：</label>
      <div class="formControls col-5">
        <select class="select" size="1" name="type" nullmsg="请选择类型" datatype="*">
          <option value="" selected>选择用户类型</option>
          <option value="SELF">系统管理员</option>
          <option value="ADMIN">管理员</option>
          <option value="CLERK">办事员</option>
          <option value="MEMBER">普通成员</option>
        </select>
      </div>
      <div class="col-4"> </div>
    </div>
    <div class="row cl">
      <div class="col-9 col-offset-3">
        <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
      </div>
    </div>
    <input class="btn btn-primary radius" type="hidden" name="provinceCode" id="provinceCode" value="">
    <input class="btn btn-primary radius" type="hidden" name="cityCode" id="cityCode" value="">
    <input class="btn btn-primary radius" type="hidden" name="districtCode" id="districtCode" value="">
  </form>
</div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/icheck/jquery.icheck.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/Validform/5.3.2/Validform.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/H-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/H-ui.admin.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/area.js"></script>
<script type="text/javascript">

  /* 省市县选择 */
  $(function(){
    add_select(0);
    $('body').on('change', '#area select', function() {
      var $me = $(this);
      var $next = $me.next();
      $("#provinceCode").val($(":selected","#selectedProvince").attr("id"));
      $("#cityCode").val($(":selected","#selectedCity").attr("id"));
      $("#districtCode").val($(":selected","#selectedDistrict").attr("id"));
      /**
       * 如果下一级已经是当前所选地区的子地区，则不进行处理
       */
      if ($me.val() == $next.data('pid')) {
        return;
      }
      $me.nextAll().remove();
      //add_select($me.val());.attr("id"));
      add_select($me.find("option:selected").attr("id"));
    });

    function add_select(pid) {
      var area_names = area['name'+pid];
      if (!area_names) {
        return false;
      }
      var area_codes = area['code'+pid];
      var $select = $('<select class="select" size="1" datatype="*" nullmsg="请选择所在城市！" style="width:98px">');
      $select.attr('name', 'city');
      $select.data('pid', pid);
      if($("#selectedProvince").val() == undefined){
        $select.attr('id','selectedProvince');
      }else if($("#selectedCity").val() == undefined){
        $select.attr('id','selectedCity');
      }else{
        $select.attr('id','selectedDistrict');
      }
      if (area_codes[0] != -1) {
        area_names.unshift('请选择');
        area_codes.unshift(-1);
      }
      for (var idx in area_codes) {
        var $option = $('<option>');
        if(area_codes[idx] == -1){
          $option.attr('value', "");
        }else{
          $option.attr('value', area_names[idx]);
        }
        $option.attr('id', area_codes[idx]);
        $option.text(area_names[idx]);
        $select.append($option);
      }
      var length = $("#area select").length;
      if(length < 3){
        $('#area').append($select);
      }
    };
  });

  //验证
  $(function(){
    $('.skin-minimal input').iCheck({
      checkboxClass: 'icheckbox-blue',
      radioClass: 'iradio-blue',
      increaseArea: '20%'
    });

    $("#form-member-add").Validform({
      tiptype:2,
      ajaxPost:true,
      callback:function(data){
        if(data){
          setTimeout(function(){
            //$.Hidemsg(); 公用方法关闭信息提示框
            $.Showmsg("添加成功！");
          },2000);
          setTimeout(function(){
            var index = parent.layer.getFrameIndex(window.name);
            parent.window.location.reload();
            parent.layer.close(index);
          },3000);
        }else{
          setTimeout(function(){
            $.Showmsg("添加失败！");
          },2000);
        }
      }
    });
  });

</script>
</body>
</html>