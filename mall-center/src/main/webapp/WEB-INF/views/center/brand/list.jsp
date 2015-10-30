<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link href="${pageContext.request.contextPath}/resources/css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
<title>品牌管理</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 产品管理 <span class="c-gray en">&gt;</span> 品牌管理 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
	<div class="text-c">
		<form class="Huiform" target="_self" id="add_brand" enctype="multipart/form-data">
			<input type="text" placeholder="分类名称" value="" id="name" name="name" class="input-text" style="width:120px">
			<span class="btn-upload form-group">
				<input type="text" placeholder="国家" value="" id="country" name="country" class="input-text" style="width:120px">
			<span class="btn-upload form-group">
				<input type="text" placeholder="省份" value="" id="province" name="province" class="input-text" style="width:120px">
			<span class="btn-upload form-group">
				<input type="text" placeholder="城市" value="" id="city" name="city" class="input-text" style="width:120px">
			<span class="btn-upload form-group">
			<input type="text" placeholder="区域" value="" id="district" name="district" class="input-text" style="width:120px">
			<span class="btn-upload form-group">
				<input type="text" placeholder="具体描述"  value="" id="detail" name="detail" class="input-text" style="width:120px">
			<span class="btn-upload form-group">
		<	<input class="input-text upload-url" type="text" name="uploadfile-2" id="uploadfile-2" readonly  datatype="*" nullmsg="请添加附件！" style="width:200px">
			<a href="javascript:void();" class="btn btn-primary upload-btn"><i class="Hui-iconfont">&#xe642;</i> 浏览文件</a>
			<input type="file" multiple name="file" class="input-file">
			<span class="btn-upload form-group">
			</span><!--  <span class="select-box" style="width:0px"> -->
		<!-- 	<select class="select" name="brandclass" size="1">
				<option value="1" selected>国内品牌</option>
				<option value="0">国外品牌</option>
			</select> -->
			</span><button type="button" class="btn btn-success" id="" name="" onClick="picture_colume_add();"><i class="Hui-iconfont">&#xe600;</i> 添加</button>
		</form>
	</div>
	<div class="cl pd-5 bg-1 bk-gray mt-20"> <span class="l"><a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a></span> <span class="r">共有数据：<strong>54</strong> 条</span> </div>
	<div class="mt-20">
		<table class="table table-border table-bordered table-bg table-sort">
			<thead>
				<tr class="text-c">
					<th width="25"><input type="checkbox" name="" value=""></th>
					<th width="70">ID</th>
					<th width="100">名称</th>
					<th width="120">LOGO</th>
					<th width="120">国家</th>
					<th width="120">省</th>
					<th width="120">市</th>
					<th width="120">区</th>
					<th>具体描述</th>
					<th width="100">操作</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/layer/1.9.3/layer.js"></script><script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/laypage/1.2/laypage.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/My97DatePicker/WdatePicker.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/H-ui.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/H-ui.admin.js"></script> 
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/H-ui.admin.product.js"></script>  --%>
<script type="text/javascript">
var targetTable;
var url = "${pageContext.request.contextPath}/brand/allBrand.dhtml";
$(function () {
     targetTable = $('.table-sort').DataTable({
         "oLanguage": {
             "sInfoEmpty": "没有数据",
             "sZeroRecords": "没有数据",
             "sEmptyTable":"没有数据"
         },
         "searching": false,
         "ordering":  false,
        "bProcessing": true,
        "bServerSide": true,
        "bStateSave": false,
        "aLengthMenu":[[2, 5, 15, 30], [2, 5, 15, 30]],
        "ajax": {
            url:url,
            "dataSrc": "content"
        },
         //"sAjaxDataProp":"content",
        "aoColumns": [
                      { "mDataProp": null },
            { "mDataProp": "id" },
            { "mDataProp": "name" },
            { "mDataProp": "logoUrl" },
            { "mDataProp": "country" },
            { "mDataProp": "province" },
            { "mDataProp": "city" },
            { "mDataProp": "district" },
            { "mDataProp": "detail" },
            { "mDataProp": null },
        ],

        "createdRow" : function(row, mDataProp, dataIndex){
            //alert('row = '+row+'mDataProp = ' +mDataProp +'dataIndex = '+dataIndex);
            $(row).addClass('text-c');
        },

        "columnDefs" : [
                        {
                            "targets" : 0 ,
                            "render" : function(mDataProp, type, full) {
                            return '<tr class="text-c"><td ><input type="checkbox" value="1" name="" ></td></tr>';
                        }
                    },

            {
                "targets" : 7,
                "render" : function(mDataProp, type, full) {
                    return '<td class="td-manage"><a title="编辑" href="javascript:;" onclick="brand_edit(\'信息修改\',\'${pageContext.request.contextPath}/brand/update/'+mDataProp.id+'.dhtml\',\'\',\'510\')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a><a title="删除" href="javascript:;" onclick="brand_del(\'${pageContext.request.contextPath}/brand/del/'+mDataProp.id+'.dhtml\')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a></td>  </td>';
}
            },
        ],
    });
});

/*品牌-删除*/
function brand_del(url) {
    if(confirm("确认要删除吗？")){
        $.post(
                url,
                function(data) {
                    alert("删除成功");
                    window.location.reload();
                }
        )
    }
}

/*品牌-编辑*/
function brand_edit(title, url, id, w, h) {
    layer_show(title, url, w, h);
};

function picture_colume_add(){
	$.ajax({
        cache: true,
        type: "POST",
        url:"${pageContext.request.contextPath}/brand/add.dhtml",
        data:$('#add_brand').serialize(),// 你的formid
	});
	//$("#add_brand").submit();
}
</script>
</body>
</html>