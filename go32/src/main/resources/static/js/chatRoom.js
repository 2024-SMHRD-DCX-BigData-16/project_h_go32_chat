async function sendMessage() {
    var input = document.getElementById('message-input');
    var text = input.value.trim();
    var chatBox = document.getElementById('chat-messages');

    if (text === '') return; // 빈 메시지 방지

    var userId = localStorage.getItem("userId");
    console.log("📢 [프론트엔드] 현재 로그인된 userId:", userId);

    if (!userId) {
        console.warn("🚨 사용자 ID가 없습니다. 로그인이 필요합니다.");
        alert("로그인 후 이용해주세요.");
        return;
    }
	
    displayMessage("user", text);
    input.value = ''; 
    chatBox.scrollTop = chatBox.scrollHeight;
    
    // ✅ "답변 중입니다..." 메시지 표시
    var typingIndicator = document.createElement('div');
    typingIndicator.classList.add('message', 'received', 'typing');
    typingIndicator.textContent = "답변을 작성 중입니다...";
    chatBox.appendChild(typingIndicator);
    chatBox.scrollTop = chatBox.scrollHeight;

    fetch("http://localhost:8088/go32/bot/chat", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: userId, prompt: text })
    })
    .then(response => response.json())  // ✅ 백엔드 응답을 JSON으로 받음
    .then(data => {
        console.log("📢 서버 응답 데이터:", data);

        if (!data.botResponse) {
            console.log("⏭️ 불필요한 응답이므로 표시하지 않음.");
            return;
        }
        
        // ✅ "답변 중입니다..." 메시지 제거
        chatBox.removeChild(typingIndicator);

        // ✅ 일정이 추가되었으면 일정 추가 성공 메시지를 표시
        if (data.scheduleAdded) {
            displayMessage("bot", "📅 일정이 성공적으로 추가되었습니다!");
            refreshScheduleList();
        } else {
            // ✅ 일반 대화 응답은 그대로 표시
            displayMessage("bot", data.botResponse);
        }
    })
    .catch(error => {
     // ✅ 오류 발생 시 "답변 중입니다..." 메시지 제거
        chatBox.removeChild(typingIndicator);
        console.error('🚨 서버 요청 실패:', error);
    });
}

// ✅ 일정 추가 후 캘린더 갱신 함수
function refreshScheduleList() {
    fetch("http://localhost:8088/go32/schedule/list", {
        method: "GET",
        headers: { "Content-Type": "application/json" }
    })
    .then(response => response.json())
    .then(data => {
        console.log("📅 캘린더 일정 갱신 완료:", data);
        updateScheduleUI(data); // 프론트 UI 업데이트 함수 호출
    })
    .catch(error => {
        console.error("🚨 캘린더 일정 갱신 실패:", error);
    });
}

function displayMessage(sender, message) {
    var chatBox = document.getElementById('chat-messages');
    var messageElement = document.createElement('div');
    messageElement.classList.add("message", sender === "user" ? "sent" : "received");
    var textElement = document.createElement('span');
    textElement.textContent = message;
    messageElement.appendChild(textElement);
    chatBox.appendChild(messageElement);
    chatBox.scrollTop = chatBox.scrollHeight;
}

function handleKeyPress(event) {
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
    }
}

document.addEventListener("DOMContentLoaded", function () {
    var inputElement = document.getElementById('message-input');
    if (inputElement) {
        inputElement.addEventListener('keydown', handleKeyPress);
        inputElement.focus();
    } else {
        console.warn("🚨 'message-input' 요소를 찾을 수 없습니다.");
    }
});

