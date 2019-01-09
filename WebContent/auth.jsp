<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>身份验证 - 扫描</title>
<link href="css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript" src="js/auth.js"></script>
</head>

<body>
<header>
	<nav class="nav">
		<ul>
			<li><a class="active" href="auth.jsp">身份验证</a></li>
		</ul>
		<a class="logo" href="index.jsp"></a>
	</nav>
</header>
<div class="container fix-first">
	<div class="row">
		<div class="col-1"></div>
		<div class="col-1 col-radius dark-bg small-panel">
			<p id="prompt" class="prompt">请输入您的手机号</p>
			<input id="step" value="1" type="hidden">
			<input id="phonenumber" name="phonenumber" type="text">
			<input id="code" name="code" type="hidden">
			<div class="btn-group"><a class="btn btn-next" href="#">下一步</a></div>
		</div>
		<div class="col-1"></div>
	</div>
</div>
</body>
</html>
