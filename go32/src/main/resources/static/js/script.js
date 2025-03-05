// ✅ 팝업 전환 (회원가입, 설문조사)
function nextPopup() {
    document.querySelector(".s_pop").style.display = "none";
    document.querySelector(".s_pop2").style.display = "block";
}

function beforePopup() {
    document.querySelector(".s_pop2").style.display = "none";
    document.querySelector(".s_pop").style.display = "block";
}

// 마이페이지 팝업
function my_page() {
    document.querySelector(".my_pop").style.display = "block";
}

function myp_cls() {
    document.querySelector(".my_pop").style.display = "none";
}

// UUID 복사 기능
function copyCode() {
  const code = document.querySelector(".uid_con");

  window.navigator.clipboard.writeText(code.textContent).then(() => {
    alert('UUID 복사 완료!');
  });
};

// 비디오 버튼
function Galaxy() {
   document.querySelector(".galaxy").style.display = "block";
   document.querySelector(".cat").style.display = "none";
   document.querySelector(".fire").style.display = "none";
   document.querySelector(".water").style.display = "none";
    document.querySelector(".galaxybtn").style.backgroundColor = "#414141";
   document.querySelector(".catbtn").style.backgroundColor = "#000";
    document.querySelector(".firebtn").style.backgroundColor = "#000";
    document.querySelector(".waterbtn").style.backgroundColor = "#000";
}
function cat() {
    document.querySelector(".cat").style.display = "block";
    document.querySelector(".galaxy").style.display = "none";
    document.querySelector(".fire").style.display = "none";
    document.querySelector(".water").style.display = "none";
    document.querySelector(".catbtn").style.backgroundColor = "#414141";
    document.querySelector(".galaxybtn").style.backgroundColor = "#000";
    document.querySelector(".firebtn").style.backgroundColor = "#000";
    document.querySelector(".waterbtn").style.backgroundColor = "#000";
}
function fire() {
   document.querySelector(".fire").style.display = "block";
   document.querySelector(".galaxy").style.display = "none";
   document.querySelector(".cat").style.display = "none";
   document.querySelector(".water").style.display = "none";
    document.querySelector(".firebtn").style.backgroundColor = "#414141";
   document.querySelector(".galaxybtn").style.backgroundColor = "#000";
   document.querySelector(".catbtn").style.backgroundColor = "#000";
    document.querySelector(".waterbtn").style.backgroundColor = "#000";
}
function water() {
   document.querySelector(".water").style.display = "block";
   document.querySelector(".galaxy").style.display = "none";
   document.querySelector(".cat").style.display = "none";
   document.querySelector(".fire").style.display = "none";
    document.querySelector(".waterbtn").style.backgroundColor = "#414141";
   document.querySelector(".galaxybtn").style.backgroundColor = "#000";
   document.querySelector(".catbtn").style.backgroundColor = "#000";
    document.querySelector(".firebtn").style.backgroundColor = "#000";
}

function tp1() {
    document.querySelector(".mp2_con1").style.display = "block";
    document.querySelector(".mp2_con2").style.display = "none";
    document.querySelector(".tm1").style.backgroundColor = "#8f8f8f";
    document.querySelector(".tm1").style.borderRadius = "10px";
    document.querySelector(".tm2").style.backgroundColor = "#2c2c2c";
}

function tp2() {
    document.querySelector(".mp2_con2").style.display = "block";
    document.querySelector(".mp2_con1").style.display = "none";
    document.querySelector(".tm2").style.backgroundColor = "#8f8f8f";
    document.querySelector(".tm2").style.borderRadius = "10px";
    document.querySelector(".tm1").style.backgroundColor = "#2c2c2c";
}



// ✅ 로그아웃 기능
function logoutUser() {
    fetch("/api/user/logout", {
        method: "POST",
        headers: { "Content-Type": "application/json" }
    })
    .then(response => response.json())
    .then(data => {
        console.log("🔍 [DEBUG] 로그아웃 응답:", data);
        if (data.status === "success") {
            alert("로그아웃되었습니다.");
            localStorage.removeItem("userId");
            localStorage.removeItem("userUuid");
            checkLoginStatus(); // 버튼 변경 반영
            setTimeout(() => location.reload(), 500); // 0.5초 후 새로고침
        }
    })
    .catch(error => console.error("❌ [ERROR]: 로그아웃 실패", error));
}

// ✅ 페이지 로드 시 로그인 상태 확인
window.onload = function () {
    checkLoginStatus(); // 🔹 로그인 상태 확인 실행
};

// ✅ 로그인 함수
function loginUser() {
    let userId = document.getElementById("login-id").value;
    let userPw = document.getElementById("login-password").value;

    console.log("🔍 [DEBUG] 로그인 요청:", userId, userPw);

    fetch("/api/user/login", {  // 🔹 API 경로 수정
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, userPw })
    })
    .then(response => {
        console.log("🔍 [DEBUG] 서버 응답 상태 코드:", response.status);
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(`서버 응답 오류: ${response.status} - ${text}`);
            });
        }
        return response.json();
    })
    .then(data => {
        console.log("🔍 [DEBUG] 서버 응답 데이터:", data);
        if (data.status === "success") {
            alert("로그인 성공!");
            localStorage.setItem("userId", data.userId);
            localStorage.setItem("userUuid", data.userUuid);
            checkLoginStatus(); // 버튼 변경 반영
            setTimeout(() => location.reload(), 500); // 0.5초 후 새로고침
        } else {
            alert(data.message);
        }
    })
    .catch(error => console.error("❌ [ERROR]:", error));
}

// ✅ 로그아웃 기능
function logoutUser() {
    fetch("/api/user/logout", {
        method: "POST",
        headers: { "Content-Type": "application/json" }
    })
    .then(response => response.json())
    .then(data => {
        console.log("🔍 [DEBUG] 로그아웃 응답:", data);
        if (data.status === "success") {
            alert("로그아웃되었습니다.");
            localStorage.removeItem("userId");
            localStorage.removeItem("userUuid");
            location.reload(); // 페이지 새로고침
        }
    })
    .catch(error => console.error("❌ [ERROR]: 로그아웃 실패", error));
}

// ✅ 로그인 팝업 열기
function openLoginPopup() {
    document.querySelector(".login_pop_page").style.display = "block";
}

// ✅ 문서 로드 후 실행
window.onload = function () {

    let hideTimeout;

    document.querySelector(".login_on").addEventListener("mouseover", () => {
        clearTimeout(hideTimeout);
        document.querySelector(".login_popup").style.height = "70px";
    });

    document.querySelector(".login_on").addEventListener("mouseout", () => {
        hideTimeout = setTimeout(() => {
            document.querySelector(".login_popup").style.height = "0";
        }, 1000);
    });

    document.querySelector(".login_popup").addEventListener("mouseover", () => {
        clearTimeout(hideTimeout);
    });

    document.querySelector(".login_popup").addEventListener("mouseout", () => {
        hideTimeout = setTimeout(() => {
            document.querySelector(".login_popup").style.height = "0";
        }, 100);
    });

    document.querySelector(".sg_on").addEventListener("click", () => {
        document.querySelector(".signup_pop_page").style.display = "block";
    });

    document.querySelector(".signup_pop_page .close-btn").addEventListener("click", () => {
        document.querySelector(".signup_pop_page").style.display = "none";
    });

    document.querySelector(".login_btn").addEventListener("click", openLoginPopup);

    document.querySelector(".login_pop_page .close-btn").addEventListener("click", () => {
        document.querySelector(".login_pop_page").style.display = "none";
    });

    document.querySelector(".f_submit").addEventListener("click", function (event) {
        event.preventDefault();
        registerUser();
    });

    document.querySelector(".login_form button").addEventListener("click", function (event) {
        event.preventDefault();
        loginUser();
    });

    document.querySelectorAll(".close-btn").forEach(btn => {
        btn.addEventListener("click", function (event) {
            event.preventDefault();
            let popup = this.closest(".signup_pop_page, .login_pop_page");
            if (popup) popup.style.display = "none";
        });
    });
}; 



// ✅ 로그인 함수
function loginUser() {
    let userId = document.getElementById("login-id").value;
    let userPw = document.getElementById("login-password").value;

    console.log("🔍 [DEBUG] 로그인 요청:", userId, userPw);

    fetch("/go32/api/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, userPw })
    })
    .then(response => {
        console.log("🔍 [DEBUG] 서버 응답 상태 코드:", response.status);
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(`서버 응답 오류: ${response.status} - ${text}`);
            });
        }
        return response.json();
    })
    .then(data => {
        console.log("🔍 [DEBUG] 서버 응답 데이터:", data);
        if (data.status === "success") {
            alert("로그인 성공!");
            localStorage.setItem("userId", data.userId);
            localStorage.setItem("userUuid", data.userUuid);
            location.reload();
        } else {
            alert(data.message);
        }
    })
    .catch(error => console.error("❌ [ERROR]:", error));
}

// ✅ 회원가입 함수 (수정)
function registerUser() {
    let userId = document.getElementById("signup-id").value.trim();
    let userPw = document.getElementById("signup-password").value.trim();
    let userName = document.getElementById("signup-name").value.trim();
    let userEmail = document.getElementById("signup-email").value.trim();
    let gender = "M"; // 기본값

    let travelPurpose = document.getElementById("travel-purpose").value.trim();
    let preferredMood = document.getElementById("preferred-atmosphere").value.trim();
    let preferredFood = document.getElementById("preferred-food").value.trim();

    // 🚨 필수 입력값 확인 (userId, userPw, userName, userEmail)
    if (!userId || !userPw || !userName || !userEmail) {
        alert("⚠️ 모든 필수 입력 항목을 입력해주세요!");
        return;
    }

    console.log("🔍 [DEBUG] 회원가입 요청:", {
        userId, userPw, userName, userEmail, gender, travelPurpose, preferredMood, preferredFood
    });

    fetch("/go32/api/user/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, userPw, userName, userEmail, gender, travelPurpose, preferredMood, preferredFood })
    })
    .then(response => {
        console.log("🔍 [DEBUG] 서버 응답 상태 코드:", response.status);
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(`서버 응답 오류: ${response.status} - ${text}`);
            });
        }
        return response.json();
    })
    .then(data => {
        console.log("🔍 [DEBUG] 서버 응답 데이터:", data);
        if (data.status === "success") {
            alert("✅ 회원가입 성공! 로그인해주세요.");
            document.querySelector(".signup_pop_page").style.display = "none";
            document.querySelector(".login_pop_page").style.display = "block";
        } else {
            alert(data.message);
        }
    })
    .catch(error => console.error("❌ [ERROR]:", error));

   
}

document.addEventListener("DOMContentLoaded", function () {
    // ✅ 요소 가져오기
    const videoSection = document.getElementById("videoSection");
    const videoText = document.getElementById("videoText"); // ID 수정
    const slideSection = document.getElementById("slideSection");

    // ✅ 네비게이션 버튼 가져오기
    const homeBtn = document.querySelector("a[href='goMain']");
    const reportBtn = document.querySelector(".report-link"); // 수정된 클래스 사용

    // ✅ 슬라이드 기능
    let slideIndex = 0;
    const slides = document.querySelectorAll(".imgs");

    function changeSlide(n) {
        console.log(`📌 changeSlide 실행됨: ${n}`); // 디버깅 로그 추가
        slides[slideIndex].classList.remove("active");
        slideIndex = (slideIndex + n + slides.length) % slides.length;
        slides[slideIndex].classList.add("active");
    }

    // ✅ "Home" 버튼 클릭 시 비디오 표시
    homeBtn.addEventListener("click", function (event) {
        event.preventDefault();
        videoSection.style.display = "block";
        videoText.style.display = "block";
        slideSection.style.display = "none";
    });

    // ✅ "Report" 버튼 클릭 시 슬라이드 표시
    reportBtn.addEventListener("click", function (event) {
        event.preventDefault();
        videoSection.style.display = "none";
        videoText.style.display = "none";
        slideSection.style.display = "block";
    });

    // ✅ 초기 슬라이드 설정
    if (slides.length > 0) {
        slides[slideIndex].classList.add("active");
    }

    // ✅ 전역에서 사용할 수 있도록 함수 등록
    window.changeSlide = changeSlide;
});


