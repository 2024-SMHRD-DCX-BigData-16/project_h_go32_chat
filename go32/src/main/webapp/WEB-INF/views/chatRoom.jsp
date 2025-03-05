<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
<!-- 정적 리소스 경로 수정 -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/chatRoom.css">
<script src="<%=request.getContextPath()%>/resources/js/chatRoom.js"
	defer></script>
</head>
<body>
    <div id="wrap">
        <aside>
            <h1 id="header_logo"><a href="goMain">TravieAI</a></h1>
            <nav class="aside_menu">
                <ul>
                    <li><a href="goMain">Home</a></li>
                    <li><a href="cald">Calendar</a></li>
                </ul>
            </nav>
            <!-- //nav -->
        </aside>
        <!-- //aside -->
        <main>
            <header id="main_header">
            </header>
            <!-- //header -->
            <div id="contents">
                <div class="chat_container">
                    <div class="chat">
                        <div id="chat-messages" class="talk_con">
                            <div class="message received">안녕하세요! 무엇을 도와드릴까요?</div>
                            <div class="message sent"></div>
                        </div>
                    </div>
                    <!-- //chat -->

                    <div class="in_text">
    					<textarea id="message-input" rows="1" placeholder="무엇이든 물어보세요" onkeydown="handleKeyPress(event)"></textarea>
    					<button onclick="sendMessage()" class="btn_submit"></button>  <!-- ✅ 버튼 동작 수정 -->
					</div>
                    <!-- //in_text -->

                </div>
                <!-- //chat_container -->

            </div>
            <!-- //contents -->

        </main>
        <!-- //main -->

    </div>
    <!-- //wrap -->
</body>
</html>
    
</body>
</html>