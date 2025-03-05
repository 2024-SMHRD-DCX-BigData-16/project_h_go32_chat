package com.jtsr.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/gpt")  // 📌 /api/gpt 경로를 반드시 포함해야 함
public class GptController {

    @Value("${openai.api.key}") // application.properties에서 API 키 가져오기
    private String openAiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @PostMapping("/chat")  // 📌 /api/gpt/chat 경로가 맞는지 확인
    public ResponseEntity<Map<String, Object>> chatWithGPT(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        if (userMessage == null || userMessage.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Message is required"));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);	
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{"
                + "\"model\": \"gpt-4\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + userMessage + "\"}]"
                + "}";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(response.getStatusCode()).body(Collections.singletonMap("error", "GPT API 호출 실패"));
        }

        return ResponseEntity.ok(Collections.singletonMap("botResponse", response.getBody().get("choices")));
    }
}
