<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="kr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js'></script>
<!-- 정적 리소스 경로 수정 -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/cal.css">
<style type="text/css">
	#contents{
		overflow: hidden;
		background-image: url("<%=request.getContextPath()%>/resources/img/moon-7021271_1280.jpg");
	    background-size: cover;
	    background-repeat: no-repeat;
	    background-position: center;
}
</style>
</head>
<body>
	<div id="wrap">
		<aside>
			<h1 id="header_logo">
				<a href="goMain">TravieAI</a>
			</h1>
			<nav class="aside_menu">
				<ul>
					<li><a href="goMain">Home</a></li>
					<li class="chatbot"><a href="chatRoom" >Chat</a></li> 
				</ul>
			</nav>
			<!-- //nav -->
		</aside>
		<!-- //aside -->
		<main>
			<header id="main_header"> </header>
			<!-- //header -->
			<div id="contents">
					<h2 id="clock">00:00:00</h2>

					<div id="container">
						<div id='calendar'></div>
						<button id="addEventButton">이벤트 추가 및 제거</button>

						<div id='todo_container'>
							<p id="cur_date">일정</p>
							<form id="todoform">
								<input required type="text" placeholder="일정을 추가하세요" />
							</form>
							<ul id="todolist">
							</ul>
						</div>

					</div>
					<!-- //container -->
			</div>
			<!-- //contents -->

		</main>
		<!-- //main -->

	</div>
	<!-- //wrap -->
	
	<script src="<%=request.getContextPath()%>/resources/js/clock.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/calendar.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/todolist.js"></script>
</body>
</html>

</body>
</html>