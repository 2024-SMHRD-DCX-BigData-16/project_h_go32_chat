var calendar;
var selectedDate = null; // 선택된 날짜를 저장하는 변수

document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        selectable: true,
        events: [],

        // 날짜 클릭 시 선택된 날짜를 저장
        dateClick: function (info) {
            console.log("Clicked date: " + info.dateStr);
            selectedDate = info.dateStr; // 선택된 날짜 저장
            setCurrentDate(info.dateStr);
            loadcurrentTodo();
        }
    });
    calendar.render();

    // "이벤트 추가" 버튼 클릭 이벤트 처리
    document.getElementById('addEventButton').addEventListener('click', function () {
        if (selectedDate) {
            // 선택된 날짜가 있다면 그 날짜에 이벤트 추가
            var existingEvent = calendar.getEvents().find(event => event.startStr === selectedDate);
            if (existingEvent) {
                // 이벤트가 있으면 삭제
                removeEventFromCalendar(selectedDate);
            } else {
                // 이벤트가 없으면 추가
                addEventToCalendar({
                    id: selectedDate,
                    start: selectedDate
                });
            }
        } else {
            alert('날짜를 먼저 선택해주세요.');
        }
    });
});

function addEventToCalendar(event) {
    calendar.addEvent({
        id: event.id,
        start: event.start,
        backgroundColor: '#eee9e9',
        borderColor: '#eee9e9'
    });
}

function removeEventFromCalendar(date) {
    var calendarEvent = calendar.getEvents().find(event => event.startStr === date);
    if (calendarEvent) {
        calendarEvent.remove();
    }
}
