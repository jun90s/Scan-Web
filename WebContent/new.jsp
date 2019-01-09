<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>新建 - 扫描</title>
<link href="css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/nav.js"></script>
<script type="text/javascript" src="js/scan.js"></script>
</head>

<body>
<header><nav class="nav"><ul></ul><a class="logo" href="#"></a></nav></header>
<div class="container fix-first" id="ready-panel">
	<input class="hidden" id="upload-image" type="file">
	<div class="row">
		<div class="col-1"></div>
		<div class="col-1 col-radius dark-bg small-panel">
			<p id="prompt" class="center">请点击下面的按钮选择您要处理的图像</p>
			<div class="btn-group"><a class="btn btn-new" href="#">上传图像</a></div>
		</div>
	</div>
</div>
<div class="container fix-first" id="work-panel">
	<input class="hidden" id="upload-image" type="file">
	<div class="row">
		<div class="col-1 sidebar">
			<p class="col-title">调整</p>
			<div>
				<p>亮度</p>
				<input id="brightness" type="range" min="-100" max="100" value="0">
			</div>
			<div>
				<p>对比度</p>
				<input id="contrast" type="range" min="-100" max="100" value="0">
			</div>
			<div>
				<p>旋转</p>
				<input id="angle" type="range" min="-180" max="180" value="0">
			</div>
			<div>
				<label><input id="mirror_x" type="checkbox">水平镜像</label>
			</div>
			<div>
				<label><input id="mirror_y" type="checkbox">垂直镜像</label>
			</div>
			<div class="btn-group">
				<a class="btn btn-reset" href="#">重置</a>
				<a class="btn btn-modify" href="#">确认</a>
			</div>
		</div>
		<div class="col-2 col-radius dark-bg center">
			<div id="canvas-container">
				<canvas id="canvas"></canvas>
			</div>
		</div>
	</div>
</div>
</body>
</html>
