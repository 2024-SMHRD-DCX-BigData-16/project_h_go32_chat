package com.jtsr.demo.service;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtsr.demo.dto.ChatGPTRequest;
import com.jtsr.demo.dto.ChatGPTResponse;
import com.jtsr.demo.dto.Message;
import com.jtsr.demo.entity.ChatHistory;
import com.jtsr.demo.entity.Schedule;
import com.jtsr.demo.repository.ChatHistoryRepository;
import com.jtsr.demo.repository.ScheduleRepository;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
public class ChatService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;
    
    @Value("${exchangerate.api.url}")
    private String exchangeRateApiUrl;

    @Value("${exchangerate.api.key}")
    private String exchangeRateApiKey;
    
    @Value("${openweather.api.url}")
    private String openWeatherApiUrl;

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;


    private final RestTemplate restTemplate;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ScheduleRepository scheduleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatService(RestTemplate restTemplate, ChatHistoryRepository chatHistoryRepository, ScheduleRepository scheduleRepository) {
        this.restTemplate = restTemplate;
        this.chatHistoryRepository = chatHistoryRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public String chat(String userId, String prompt) {
        List<Message> chatHistory = loadChatHistoryFromDB(userId); // 기존 대화 불러오기
        boolean isScheduleRequest = prompt.matches(".*(일정 추가|여행 일정|여행 계획|스케줄).*");

        // 🔥 1. 새 사용자 메시지를 대화 내역에 추가한 후 API에 전달할 리스트 생성
        List<Message> updatedChatHistory = new ArrayList<>(chatHistory);
        updatedChatHistory.add(new Message("user", prompt)); // 🆕 사용자 메시지 추가

        // ✅ 현재 시간 및 날짜 가져오기
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

        String currentTime = now.format(timeFormatter);
        String currentDate = now.format(dateFormatter);

        // ✅ "시간" 관련 질문 감지
        Pattern timePattern = Pattern.compile(".*(시간|몇 시|현재 시각|지금 몇 시|몇 시인지|몇시야).*");
        boolean isTimeQuestion = timePattern.matcher(prompt).matches();

        // ✅ "날짜" 관련 질문 감지
        Pattern datePattern = Pattern.compile(".*(오늘 날짜|오늘 며칠|오늘 무슨 날|며칠이야).*");
        boolean isDateQuestion = datePattern.matcher(prompt).matches();

        // ✅ "환율" 관련 질문 감지
        Pattern exchangeRatePattern = Pattern.compile(".*(환율|달러|유로|엔화|원화|currency|exchange rate).*");
        Matcher matcher = exchangeRatePattern.matcher(prompt);
        boolean isExchangeRateQuestion = matcher.matches();

        // ✅ "날씨" 관련 질문 감지 (지역명을 포함하여 인식)
        Pattern weatherPattern = Pattern.compile(".*(날씨|기온|온도|기상).*");
        Matcher weatherMatcher = weatherPattern.matcher(prompt);

        // ✅ 지역명 추출을 위한 정규 표현식 (한글 및 영문 도시명 포함)
        Pattern locationPattern = Pattern.compile("(?i)(?:in |at |of |에서 |의 )?([가-힣A-Za-z\\s]+)\\s?(날씨|기온|온도|기상)");
        Matcher locationMatcher = locationPattern.matcher(prompt);

        // ✅ 기본값을 "광주광역시"로 설정
        String city = "광주광역시"; 
        if (locationMatcher.find()) {
            city = locationMatcher.group(1).trim();
        }

        // ✅ 1. 사용자가 "시간"을 물어봤다면, ChatGPT에 보내지 않고 직접 응답
        if (isTimeQuestion) {
            return "지금은 " + currentTime + "입니다.";
        }

        // ✅ 2. 사용자가 "날짜"를 물어봤다면, ChatGPT에 보내지 않고 직접 응답
        if (isDateQuestion) {
            return "오늘 날짜는 " + currentDate + "입니다.";
        }

        // ✅ 사용자가 "날씨"를 물어봤다면, OpenWeatherMap API 호출
        if (weatherMatcher.matches()) {
            return getWeatherInfo(city);
        }

        // ✅ 3. 사용자가 "환율"을 물어봤다면, ChatGPT 대신 실시간 환율 정보 제공
        if (isExchangeRateQuestion) {
            return getExchangeRateInfo();
        }

        // ✅ 일정 추가 요청 감지
        if (isScheduleRequest) {
            // 🔹 사용자가 입력한 날짜를 추출
            String extractedDate = extractDateFromPrompt(prompt);

            // 🔹 GPT에게 응답 요청
            ChatGPTRequest request = new ChatGPTRequest(model, updatedChatHistory);
            ChatGPTResponse response = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

            if (response == null || response.getChoices().isEmpty()) {
                return "❌ 오류: GPT 응답이 없습니다.";
            }

            String botReply = response.getChoices().get(0).getMessage().getContent();
            saveChatHistoryToDB(userId, prompt, botReply);

            // 🔹 GPT 응답을 바탕으로 일정 저장
            if (saveScheduleFromTextResponse(userId, botReply, extractedDate)) {
                return "📅 일정이 추가되었습니다! 캘린더에서 확인하세요.";
            } else {
                return "❌ 일정 추가에 실패했습니다. 다시 시도해주세요.";
            }
        }

        // ✅ isScheduleRequest가 false일 경우 항상 실행되도록 보장
        ChatGPTRequest request = new ChatGPTRequest(model, updatedChatHistory);
        ChatGPTResponse response = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

        if (response == null || response.getChoices().isEmpty()) {
            return "❌ 오류: GPT 응답이 없습니다.";
        }

        String botReply = response.getChoices().get(0).getMessage().getContent();
        saveChatHistoryToDB(userId, prompt, botReply);
        return botReply;
    }




    private List<Message> loadChatHistoryFromDB(String userId) {
        List<ChatHistory> chatRecords = chatHistoryRepository.findByUserId(userId);
        List<Message> chatMessages = new ArrayList<>();

        // 🔥 기존 대화 내역을 추가하되, 마지막 챗봇 응답을 포함하지 않도록 수정
        for (int i = 0; i < chatRecords.size(); i++) {
            ChatHistory record = chatRecords.get(i);
            chatMessages.add(new Message("user", record.getUserMessage()));

            // 마지막 메시지라면 챗봇 응답을 포함하지 않음 (자동 응답 포함 방지)
            if (i != chatRecords.size() - 1) {
                chatMessages.add(new Message("assistant", record.getBotResponse()));
            }
        }

        return chatMessages;
    }


    private void saveChatHistoryToDB(String userId, String userMessage, String botResponse) {
        ChatHistory chatRecord = new ChatHistory();
        chatRecord.setUserId(userId);
        chatRecord.setUserMessage(userMessage);
        chatRecord.setBotResponse(botResponse);
        chatHistoryRepository.save(chatRecord);
    }

    /**
     * ✅ GPT가 반환한 응답에서 일정 정보를 추출하여 저장
     */
    private boolean saveScheduleFromTextResponse(String userId, String gptResponse, String userProvidedDate) {
        try {
            // 일정 이름과 설명을 추출하기 위한 정규 표현식
            Pattern pattern = Pattern.compile("([^\"]+) 여행");
            Matcher matcher = pattern.matcher(gptResponse);

            if (!matcher.find()) {
                System.err.println("❌ 일정 정보 추출 실패: GPT 응답에서 일정 이름을 찾지 못함.");
                return false;
            }

            String scheduleName = matcher.group(1) + " 여행";
            String scheduleDesc = gptResponse.trim();
            String scheduleDate = userProvidedDate; // 🔹 사용자가 입력한 날짜 반영

            // ✅ 날짜 변환 확인
            if (scheduleDate == null || scheduleDate.isEmpty()) {
                System.err.println("❌ 일정 정보 추출 실패: 날짜가 비어 있음.");
                return false;
            }

            System.out.println("✅ 일정 추가 정보:");
            System.out.println("   - 사용자 ID: " + userId);
            System.out.println("   - 일정 이름: " + scheduleName);
            System.out.println("   - 일정 설명: " + scheduleDesc);
            System.out.println("   - 일정 날짜: " + scheduleDate);

            Schedule schedule = new Schedule();
            schedule.setUserId(userId);
            schedule.setScheduleName(scheduleName);
            schedule.setScheduleDesc(scheduleDesc);
            schedule.setScheduleDate(Date.valueOf(scheduleDate));
            schedule.setPoiId(0);

            scheduleRepository.save(schedule);
            System.out.println("✅ 일정 저장 성공!");
            return true;
        } catch (Exception e) {
            System.err.println("❌ 일정 저장 실패: " + e.getMessage());
            return false;
        }
    }


    /**
     * ✅ GPT 응답에서 날짜를 추출하는 메서드 (기본값: 오늘 날짜)
     */
    private String extractDateFromPrompt(String prompt) {
        // ✅ "3월 11일" 같은 형식의 날짜 추출
        Pattern koreanDatePattern = Pattern.compile("(\\d{1,2})월 (\\d{1,2})일");
        Matcher koreanDateMatcher = koreanDatePattern.matcher(prompt);

        if (koreanDateMatcher.find()) {
            int month = Integer.parseInt(koreanDateMatcher.group(1));
            int day = Integer.parseInt(koreanDateMatcher.group(2));
            int currentYear = LocalDateTime.now().getYear();
            String fullDate = String.format("%d-%02d-%02d", currentYear, month, day);
            System.out.println("✅ 날짜 변환 성공: " + fullDate);
            return fullDate;
        }

        // ✅ 기본값: 오늘 날짜
        return LocalDateTime.now().toLocalDate().toString();
    }

    
 // ✅ 실시간 날씨 정보 가져오는 메서드
    private String getWeatherInfo(String city) {
        String url = openWeatherApiUrl + "?q=" + city + "&appid=" + openWeatherApiKey + "&units=metric&lang=kr";

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("main") && response.containsKey("weather") && response.containsKey("wind")) {
                Map<String, Object> main = (Map<String, Object>) response.get("main");
                List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
                Map<String, Object> wind = (Map<String, Object>) response.get("wind");

                double temp = (double) main.get("temp");           // 현재 기온
                double feelsLike = (double) main.get("feels_like"); // 체감 온도
                int humidity = (int) main.get("humidity");         // 습도(%)
                double windSpeed = (double) wind.get("speed");     // 바람 속도 (m/s)
                String weatherDescription = (String) weatherList.get(0).get("description"); // 날씨 상태

                return String.format("🌤 현재 %s의 날씨 정보:\n" +
                        "기온: %.1f°C\n/" +
                        "체감 온도: %.1f°C\n/" +
                        "상태: %s\n/" +
                        "습도: %d%%\n/" +
                        "바람 속도: %.1f m/s",
                        city, temp, feelsLike, weatherDescription, humidity, windSpeed);
            } else {
                return "❌ 날씨 정보를 불러올 수 없습니다.";
            }
        } catch (Exception e) {
            return "❌ 날씨 정보를 가져오는 중 오류 발생: " + e.getMessage();
        }
    }



    // ✅ 실시간 환율 API 호출 (디버깅 추가)
    private String getExchangeRateInfo() {
        String url = exchangeRateApiUrl + "/" + exchangeRateApiKey + "/latest/KRW";
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("conversion_rates")) {
                Map<String, Number> rates = (Map<String, Number>) response.get("conversion_rates");

                double krwToUsd = rates.getOrDefault("USD", 0).doubleValue();
                double krwToEur = rates.getOrDefault("EUR", 0).doubleValue();
                double krwToJpy = rates.getOrDefault("JPY", 0).doubleValue();
                double krwToCny = rates.getOrDefault("CNY", 0).doubleValue();
                double krwToGbp = rates.getOrDefault("GBP", 0).doubleValue();

                return String.format(
                        " 실시간 환율 정보:\n" +
                                "1 미국 달러(USD) → %.2f 원(KRW)\n/" +
                                "1 유로(EUR) → %.2f 원(KRW)\n/" +
                                "1 일본 엔(JPY) → %.2f 원(KRW)\n/" +
                                "1 중국 위안(CNY) → %.2f 원(KRW)\n/" +
                                "1 영국 파운드(GBP) → %.2f 원(KRW)",
                        1 / krwToUsd, 1 / krwToEur, 1 / krwToJpy, 1 / krwToCny, 1 / krwToGbp);
            } else {
                return "❌ 환율 정보를 찾을 수 없습니다. API 응답: " + response;
            }
        } catch (HttpClientErrorException e) {
            return "❌ API 요청 오류: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (ResourceAccessException e) {
            return "❌ 환율 API 서버에 접근할 수 없습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ 환율 정보를 불러오는 중 오류 발생: " + e.getMessage();
        }
    }
}
