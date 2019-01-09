<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>首页 - 扫描</title>
<link href="css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
</head>

<body class="home">
<header>
  <nav class="nav home-nav">
    <ul>
      <li><a href="auth.jsp">注册</a></li>
      <li><a href="auth.jsp">登录</a></li>
    </ul>
  </nav>
</header>
<section class="home-section">
  <div class="home-logo"></div>
  <p class="home-title">您随身携带的扫描仪</p>
  <p class="home-description">矫正您任意角度拍摄的证件／文档图像</p>
  <p class="home-description">云存储／云加速支持，海量、便捷、低延迟、低成本</p>
  <div class="home-download"><a class="btn btn-app-download" href="index.jsp">立即下载</a></div>
</section>
<script type="text/javascript">
	$(function() {
		$(".btn-app-download").click(function() {
			alert("即将到来");
		});
		if($.cookie("token") != null) {
			$(window).attr('location', "gallery.jsp");
		}
	});
</script>
</body>
</html>
