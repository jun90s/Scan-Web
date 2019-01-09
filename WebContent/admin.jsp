<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>管理 - 扫描</title>
<link href="css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript" src="js/nav.js"></script>
<script type="text/javascript" src="js/scan.js"></script>
<script type="text/javascript" src="js/admin.js"></script>
</head>

<body>
<header><nav class="nav"><ul></ul><a class="logo" href="index.jsp"></a></nav></header>
<div class="container fix-first">
	<div class="row">
		<div class="col-1"></div>
		<div class="col-1 col-radius dark-bg small-panel">
			<p id="prompt" class="prompt">请输入用户手机号</p>
			<input id="phonenumber" name="phonenumber" type="text">
			<div class="btn-group">
				<a class="btn btn-ban" href="#">封禁</a>
				<a class="btn btn-unban" href="#">解封</a>
			</div>
		</div>
		<div class="col-1"></div>
	</div>
	<br>
</div>
</body>
</html>
