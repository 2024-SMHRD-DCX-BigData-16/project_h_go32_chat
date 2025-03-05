package com.jtsr.demo.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class GptService {

    @Value("${openai.api.key}") // ✅ properties 파일에서 API 키 가져오기
    private String openaiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String getGptResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();

        // OpenAI API 요청 데이터
        String requestBody = """
            {
                "model": "gpt-4",
                "messages": [{"role": "user", "content": "%s"}]
            }
        """.formatted(userMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey); // ✅ API 키 추가

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            OPENAI_URL, HttpMethod.POST, request, Map.class
        );

        // OpenAI 응답에서 메시지 추출
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("choices")) {
            return ((Map<String, String>) ((Map<?, ?>) responseBody.get("choices")).get(0)).get("message");
        }
        return "❌ 응답이 없습니다.";
    }
}
