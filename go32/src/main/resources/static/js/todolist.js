const todoform = document.querySelector("#todoform");
const todoInput = document.querySelector("#todoform input");
const todoList_ul = document.querySelector("#todolist");
const curDate_p = document.querySelector("#cur_date");

todoform.addEventListener("submit", handleToDoSummit);

let DBLists = [];
let CurrentDate;
const DBLIST_KEY = "DBLISTS";

/**
 * ✅ TodoList 객체 정의 (특정 날짜에 대한 일정 저장)
 */
function TodoList(date) {
    this.date = date;
    this.todos = [];
}

/**
 * ✅ UI에서 일정 목록 초기화
 */
function clearTodoItems() {
    while (todoList_ul.firstChild) {
        todoList_ul.removeChild(todoList_ul.firstChild);
    }
}

/**
 * ✅ UI에 일정 표시
 */
function displayTodoItem(todo_obj_) {
    const todo_cur_li = document.createElement("li");
    const todo_cur_span = document.createElement("span");
    const todo_remove_btn = document.createElement("button");

    todo_cur_li.id = todo_obj_.id;
    todo_cur_span.innerText = todo_obj_.text;
    todo_remove_btn.innerText = "X";
    todo_remove_btn.addEventListener("click", () => deleteTodo(todo_obj_.id));

    todo_cur_li.appendChild(todo_cur_span);
    todo_cur_li.appendChild(todo_remove_btn);
    todoList_ul.appendChild(todo_cur_li);
}

/**
 * ✅ 일정 추가 (서버에도 저장)
 */
function handleToDoSummit(event) {
    event.preventDefault();

    const cur_todos_obj = {
        text: todoInput.value
    };

    todoInput.value = "";

    // 서버에 저장 후 LocalStorage 업데이트
    saveToServer(CurrentDate, cur_todos_obj);
}

/**
 * ✅ 서버에 일정 추가 후 LocalStorage 업데이트
 */
function saveToServer(date, todo) {
    fetch("http://localhost:8088/go32/api/schedule/add", {  // ✅ 수정된 경로
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            scheduleName: todo.text,
            scheduleDesc: "",
            scheduleDate: date,
            poiId: 1
        })
    })
    .then(response => response.json())
    .then(data => {
        console.log("✅ 서버 응답:", data);

        // 서버에서 받은 ID를 로컬 데이터에 반영
        todo.id = data.scheduleId;

        // LocalStorage 업데이트
        addNewTodo(date, todo);
        saveDBListInLocalStorage();

        // UI 업데이트
        displayTodoItem(todo);
    })
    .catch(error => console.error("🚨 서버 저장 실패:", error));
}

/**
 * ✅ LocalStorage에 일정 추가
 */
function addNewTodo(date, newTodo) {
    let curTodoList = DBLists.find(list => list.date === date);
    if (!curTodoList) {
        curTodoList = new TodoList(date);
        DBLists.push(curTodoList);
    }
    curTodoList.todos.push(newTodo);
    saveDBListInLocalStorage();
}

/**
 * ✅ 일정 삭제 (서버에서도 삭제)
 */
function deleteTodo(todoId) {
    fetch(`http://localhost:8088/go32/api/schedule/delete/${todoId}`, {  // ✅ 수정된 경로
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            console.log("✅ 서버에서 삭제 완료:", todoId);

            // LocalStorage에서도 삭제
            DBLists.forEach(todolist => {
                todolist.todos = todolist.todos.filter(todo => todo.id !== todoId);
            });

            // UI에서도 삭제
            document.getElementById(todoId).remove();
            saveDBListInLocalStorage();
        } else {
            console.error("❌ 삭제 실패:", response.status);
        }
    })
    .catch(error => console.error("🚨 서버 삭제 실패:", error));
}

/**
 * ✅ 서버에서 일정 불러오기 + LocalStorage 동기화
 */
function loadcurrentTodo() {
    clearTodoItems();

    fetch(`http://localhost:8088/go32/api/schedule/list?date=${CurrentDate}`)  // ✅ 수정된 경로
        .then(response => response.json())
        .then(data => {
            DBLists = data.map(todo => ({
                date: CurrentDate,
                todos: [{
                    id: todo.scheduleId,
                    text: todo.scheduleName
                }]
            }));

            console.log("✅ 서버에서 받은 일정:", DBLists);

            DBLists.forEach(tlist => {
                if (tlist.date === CurrentDate) {
                    tlist.todos.forEach(displayTodoItem);
                }
            });

            saveDBListInLocalStorage();
        })
        .catch(error => console.error("🚨 서버 일정 불러오기 실패:", error));
}


/**
 * ✅ LocalStorage에 DBLists 저장
 */
function saveDBListInLocalStorage() {
    console.log("✅ LocalStorage 업데이트");
    localStorage.setItem(DBLIST_KEY, JSON.stringify(DBLists));
}


/**
 * ✅ 초기 로드 (서버 데이터 가져오기)
 */
function loadTodoInit() {
    let today = new Date();
    let formattedToday = today.toISOString().split('T')[0];

    setCurrentDate(formattedToday);
    loadcurrentTodo();
}

/**
 * ✅ 현재 선택된 날짜 설정
 */
function setCurrentDate(date) {
    curDate_p.textContent = date + " 일정";
    CurrentDate = date;
}

// ✅ 실행
loadTodoInit();
