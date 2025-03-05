package com.jtsr.demo.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequest {
    private String model;
    private List<Message> messages;

    // 기존 생성자 (prompt 단일 입력)
    public ChatGPTRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }

    // 🔹 새롭게 추가한 생성자 (채팅 내역을 저장할 수 있도록 변경)
    public ChatGPTRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages; // 기존 메시지 리스트를 유지하며 전달
    }
}