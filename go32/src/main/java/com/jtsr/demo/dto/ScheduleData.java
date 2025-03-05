package com.jtsr.demo.dto;

import java.util.List;

public class ScheduleData {
    private String date;
    private List<TodoData> todos;

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<TodoData> getTodos() { return todos; }
    public void setTodos(List<TodoData> todos) { this.todos = todos; }

    public static class TodoData {
        private String text;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}
