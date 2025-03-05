package com.jtsr.demo.controller;

import com.jtsr.demo.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bot")
public class ChatBotController {
    private final ChatService chatService;

    public ChatBotController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value = "/chat", produces = "application/json")
    @ResponseBody
    public Map<String, Object> chat(@RequestBody Map<String, String> requestData) {
        System.out.println("📢 [백엔드] 채팅 요청 수신!");
        System.out.println("📢 사용자 ID: " + requestData.get("userId"));
        System.out.println("📢 사용자 메시지: " + requestData.get("prompt"));

        String userId = requestData.get("userId");
        String prompt = requestData.get("prompt");

        boolean isScheduleRequest = prompt.contains("일정 추가") || prompt.contains("여행 일정");
        String response = chatService.chat(userId, prompt);

        Map<String, Object> result = new HashMap<>();
        result.put("userMessage", prompt);
        result.put("botResponse", response);

        // ✅ 일정 추가 요청 시 백엔드에서 일정 추가 여부 확인
        if (isScheduleRequest) {
            boolean scheduleAdded = response.contains("📅 일정이 추가되었습니다");
            result.put("scheduleAdded", scheduleAdded);
        } else {
            result.put("scheduleAdded", false);
        }

        return result;
    }
}
