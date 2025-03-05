<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>TravieAI</title>

<!-- 정적 리소스 경로 수정 -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/style.css">
<script src="<%=request.getContextPath()%>/resources/js/script.js" defer></script>

</head>
<body>
	<div id="wrap">
		<aside>
			<h1 id="header_logo">
				<a href="goMain">Go32</a>
			</h1>
			<nav class="aside_menu">
				<ul>
					<li><a href="goMain">Home</a></li>
					<li class="chatbot"><a href="chatRoom">Chat</a></li>
					<li class="chatbot"><a href="cald">Calendar</a></li>
					<li><a href="#" class="report-link">Report</a></li>
				</ul>
			</nav>
		</aside>

		<main>
			<header id="main_header">
				<c:if test="${loginuser == null }">
					<button class="logbtn">
						<a href="login" class="login_on">Log in</a>
					</button>
				</c:if>
				<c:if test="${loginuser != null}">
					<p class="p_user">${sessionScope.userName}님</p>
					<img onClick="my_page()"
						src="<%=request.getContextPath()%>/resources/img/free-icon-profile-8246876.png"
						alt="마이페이지">
					<button>
						<a href="logout" class="login_out"> Log out</a>
					</button>

					<style>
aside nav .chatbot {
	display: block;
}

.login_popup {
	display: none;
}
</style>
				</c:if>
			</header>
			<div id="space"></div>
			<div id="contents">

				<video id="videoSection"
					src="<%=request.getContextPath()%>/resources/video/6797-196071980_small.mp4"
					muted autoplay loop></video>
				<p class="video_text" id="videoText">TravieAI</p>

				<div class="content1"></div>
				<div class="content2">
					<p>
						<img
							src="<%=request.getContextPath()%>/resources/img/free-icon-loop-8563257.png"
							alt="loop" class="rotate-icon"> Loop
					</p>

					<p>
						Trim down and create seamless repeating videos <br> with Loop
					</p>
					<div class="vdo">
						<ul>
							<li class="cat"><video id="videoSection"
									src="<%=request.getContextPath()%>/resources/video/cat.mp4"
									muted autoplay loop></video></li>
							<li class="fire"><video id="videoSection"
									src="<%=request.getContextPath()%>/resources/video/fire.mp4"
									muted autoplay loop></video></li>
							<li class="water"><video id="videoSection"
									src="<%=request.getContextPath()%>/resources/video/water.mp4"
									muted autoplay loop></video></li>
							<li class="galaxy"><video id="videoSection"
									src="<%=request.getContextPath()%>/resources/video/galaxy.mp4"
									muted autoplay loop></video></li>
						</ul>
					</div>

					<ul>
						<li><button class="catbtn" onclick="cat()">Cat</button></li>
						<li><button class="firebtn" onclick="fire()">Fire</button></li>
						<li><button class="waterbtn" onclick="water()">Water</button></li>
						<li><button class="galaxybtn" onclick="Galaxy()">Galaxy</button></li>
					</ul>
				</div>


				<div id="slideSection" style="display: none;">
					<button class="btn prev" onclick="changeSlide(-1)">&#10094;</button>

					<img class="imgs active"
						src="<%=request.getContextPath()%>/resources/img/001.png"
						alt="슬라이드1"> <img class="imgs"
						src="<%=request.getContextPath()%>/resources/img/005.png"
						alt="슬라이드2"> <img class="imgs"
						src="<%=request.getContextPath()%>/resources/img/007.png"
						alt="슬라이드3"> <img class="imgs"
						src="<%=request.getContextPath()%>/resources/img/013.png"
						alt="슬라이드4"> 

					<button class="btn next" onclick="changeSlide(1)">&#10095;</button>
				</div>

			</div>
		</main>
	</div>
	<!-- //wrap -->

	<!-- 로그인 팝업 -->
	<div class="login_popup">
		<ul>
			<li><a href="#" class="login_btn">Login</a></li>
			<li><a href="#" class="sg_on">Sign Up</a></li>
		</ul>
	</div>

	<!-- 마이페이지 팝업 -->
	<div class="my_pop">
		<div class="mpCon">
			<div class="mp1">
				<div class="tt1">Settings</div>
				<div class="tm">
					<ul>
						<li><a href="#" onclick="tp1()" class="tm1">⚙️ General</a></li>
						<li><a href="#" onclick="tp2()" class="tm2">📃 preference</a></li>
					</ul>
				</div>
			</div>
			<div class="mp2">
				<div class="mp2_con1">
					<h3>General</h3>
					<br>
					<ul>
						<li><h4>Name</h4>
							<p>${sessionScope.userName }</p></li>
						<li><h4>Email</h4>
							<p>${sessionScope.userEmail }</p></li>
						<li class="lilast" onclick="copyCode()"><h4 class="key">UUID</h4>
							<p class="uid_con">${sessionScope.userUuid }</p></li>
						<!-- 기본적으로 가려진 상태 -->
					</ul>
				</div>
				<div class="mp2_con2">
					<h3>Preference</h3>
					<br>
					<ul>
						<li><h4>TravelPurpose</h4>
							<p></p></li>
						<li><h4>PreferredMood</h4>
							<p></p></li>
						<li><h4>PreferredFood</h4>
							<p></p></li>
					</ul>
				</div>

				<button onClick="myp_cls()">Done</button>
			</div>
		</div>
	</div>

	<!-- 로그인 페이지 -->
	<div class="login_pop_page">
		<form action="#" class="login_form">
			<div class="l_pop">
				<h2>로그인</h2>
				<label for="login-id">아이디</label>
				<div>
					<input type="text" id="login-id" placeholder="아이디">
				</div>
				<label for="login-password">비밀번호</label>
				<div>
					<input type="password" id="login-password" placeholder="비밀번호">
				</div>
				<br>
				<button type="submit">로그인</button>
			</div>
		</form>

		<div class="find_acc">
			<ul>
				<li><a href="#" onclick="findUser()">아이디 및 비밀번호 🔍</a></li>
				<c:if test="Uuid"></c:if>

			</ul>
		</div>

		<div class="close-btn">
			<a href="#"><img
				src="<%=request.getContextPath()%>/resources/img/close-btn.png"
				alt="닫기"></a>
		</div>
	</div>

	<!-- 회원가입 페이지 -->
	<div class="signup_pop_page">
		<form action="#" class="signup_form">
			<div class="s_pop">
				<h2>계정 만들기</h2>
				<label for="signup-id">아이디</label>
				<div>
					<input type="text" id="signup-id" placeholder="아이디">
				</div>
				<label for="signup-password">비밀번호</label>
				<div>
					<input type="password" id="signup-password" placeholder="비밀번호">
				</div>
				<label for="signup-name">이름</label>
				<div>
					<input type="text" id="signup-name" placeholder="이름">
				</div>
				<label for="signup-email">이메일</label>
				<div>
					<input type="email" id="signup-email" placeholder="이메일">
				</div>
				<br>
				<button type='button' onclick="nextPopup()">계속</button>
			</div>

			<div class="s_pop2">
				<h2>선호 유형</h2>
				<label for="travel-purpose">여행 목적</label>
				<div>
					<select id="travel-purpose">
						<option value="default">선택 사항</option>
						<option value="leisure">여가</option>
						<option value="business">비즈니스</option>
						<option value="adventure">모험</option>
						<option value="relaxation">휴식</option>
					</select>
				</div>

				<label for="preferred-atmosphere">선호 분위기</label>
				<div>
					<input type="text" id="preferred-atmosphere" placeholder="선호 분위기">
				</div>

				<label for="preferred-food">선호 음식</label>
				<div>
					<input type="text" id="preferred-food" placeholder="선호 음식">
				</div>
				<br>
				<div class="b_s">
					<button type='button' onclick="beforePopup()">이전</button>
					<input class="f_submit" type="submit" value="제출">
				</div>
			</div>
		</form>
		<div class="close-btn">
			<a href="#"><img
				src="<%=request.getContextPath()%>/resources/img/close-btn.png"
				alt="닫기"></a>
		</div>
	</div>

	<script type="text/javascript">
	
   let uid = '${sessionScope.userUuid}';
   console.log(uid);

   function findUser() {
	    let uuid = prompt("UUID를 입력하세요.");  // ✅ UUID 입력받기

	    if (uuid) {  // 사용자가 취소를 누르면 요청을 보내지 않음
	        fetch("http://localhost:8088/go32/api/user/findUser", {  // ✅ URL 수정!
	            method: "POST",
	            headers: {
	                "Content-Type": "application/json"
	            },
	            body: JSON.stringify({ uuid: uuid })
	        })
	        .then(response => response.json())  // ✅ JSON 응답 변환
	        .then(data => {
	            console.log("🔍 서버 응답 데이터:", data); // ✅ 응답 데이터 확인

	            // ✅ Proxy 객체일 가능성이 있으므로, 새로운 객체로 변환
	            let safeData = Object.assign({}, data);
	            console.log("📢 [디버깅] 변환된 safeData:", safeData);

	            // ✅ JSON 형식 그대로 alert 창에 출력
	            alert("🔍 [조회 결과]\n\n" + JSON.stringify(safeData, null, 4));
	        })
	        .catch(error => {
	            alert("❌ 사용자를 찾을 수 없습니다.");
	            console.error("❌ Error:", error);
	        });
	    }
	}




</script>


</body>
</html>
