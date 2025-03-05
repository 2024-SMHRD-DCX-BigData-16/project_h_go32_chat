package com.jtsr.demo.controller;

import com.jtsr.demo.model.User;
import com.jtsr.demo.model.UserPreferences;
import com.jtsr.demo.repository.UserRepository;
import com.jtsr.demo.service.UserService;
import com.jtsr.demo.repository.UserPreferencesRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user") // ✅ API 기본 경로 설정
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserPreferencesRepository preferencesRepository;
	
	@Autowired
	private UserService userService;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// ✅ API 테스트 엔드포인트
	@GetMapping("/test")
	public String testApi() {
		return "✅ User API 정상 작동 중! (/api/user/test)";
	}

	// ✅ 회원가입 처리 + 선호 유형 저장
	@PostMapping("/register")
	public Map<String, String> registerUser(@RequestBody Map<String, String> params, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		System.out.println("🔍 [DEBUG] 수신된 회원가입 요청 데이터: " + params);

		// 🚨 필수 입력값 검증
		if (!params.containsKey("userEmail") || params.get("userEmail") == null
				|| params.get("userEmail").trim().isEmpty()) {
			response.put("status", "fail");
			response.put("message", "⚠️ 이메일은 필수 입력 항목입니다.");
			return response;
		}

		if (!params.containsKey("userId") || params.get("userId") == null || params.get("userId").trim().isEmpty()) {
			response.put("status", "fail");
			response.put("message", "⚠️ 사용자 ID는 필수 입력 항목입니다.");
			return response;
		}

		if (!params.containsKey("userPw") || params.get("userPw") == null || params.get("userPw").trim().isEmpty()) {
			response.put("status", "fail");
			response.put("message", "⚠️ 비밀번호는 필수 입력 항목입니다.");
			return response;
		}

		if (!params.containsKey("userName") || params.get("userName") == null
				|| params.get("userName").trim().isEmpty()) {
			response.put("status", "fail");
			response.put("message", "⚠️ 이름은 필수 입력 항목입니다.");
			return response;
		}

		// 이메일 중복 체크
		if (userRepository.existsByUserEmail(params.get("userEmail"))) {
			response.put("status", "fail");
			response.put("message", "⚠️ 이미 가입된 이메일입니다!");
			return response;
		}

		// ✅ 사용자 정보 저장
		User newUser = new User();
		newUser.setUserId(params.get("userId"));
		newUser.setUserPw(passwordEncoder.encode(params.get("userPw"))); // 🔒 비밀번호 암호화
		newUser.setUserName(params.get("userName"));
		newUser.setUserEmail(params.get("userEmail"));
		newUser.setUserUuid(UUID.randomUUID().toString()); // 🔥 UUID 자동 생성

		// 🚨 Gender 값 확인 (기본값 "M" 설정)
		String genderValue = params.getOrDefault("gender", "M").toUpperCase();
		try {
			newUser.setGender(User.Gender.valueOf(genderValue));
		} catch (IllegalArgumentException e) {
			response.put("status", "fail");
			response.put("message", "⚠️ 올바른 성별 값을 입력하세요 (M 또는 F).");
			return response;
		}

		userRepository.save(newUser);
		System.out.println("✅ 사용자 정보 저장 완료!");

		// ✅ 선호 유형 정보 저장
		UserPreferences preferences = new UserPreferences();
		preferences.setUserId(newUser.getUserId());
		preferences.setPreferredFood(params.getOrDefault("preferredFood", null));
		preferences.setPreferredMood(params.getOrDefault("preferredMood", null));

		// 🚨 travelPurpose 기본값 설정
		String travelPurpose = params.getOrDefault("travelPurpose", "LEISURE").toUpperCase();
		System.out.println("🔍 [DEBUG] 수신된 travelPurpose 값: " + travelPurpose);

		try {
			preferences.setTravelPurpose(UserPreferences.TravelPurpose.valueOf(travelPurpose));
			preferencesRepository.save(preferences);
			System.out.println("✅ 선호 정보 저장 완료!");
		} catch (IllegalArgumentException e) {
			response.put("status", "fail");
			response.put("message", "⚠️ 잘못된 여행 목적 값입니다.");
			return response;
		}

		response.put("status", "success");
		response.put("message", "✅ 회원가입 및 선호 유형 저장 성공!");
		return response;
	}

	// ✅ 로그인 처리
	@PostMapping("/login")
	public Map<String, String> loginUser(@RequestBody Map<String, String> params, HttpServletRequest request,
			HttpSession session) {
		Map<String, String> response = new HashMap<>();
		String userId = params.get("userId");
		String userPw = params.get("userPw");

		Optional<User> user = userRepository.findByUserId(userId);

		if (user.isPresent() && passwordEncoder.matches(userPw, user.get().getUserPw())) {
			session.setAttribute("loginuser", user);
			session.setAttribute("userId", user.get().getUserId());
			session.setAttribute("userName", user.get().getUserName());
			session.setAttribute("userEmail", user.get().getUserEmail());
			session.setAttribute("userUuid", user.get().getUserUuid());
			session.setAttribute("userPw", user.get().getUserPw());

			response.put("status", "success");
			response.put("message", "로그인 성공!");
			response.put("userId", userId);

			response.put("userName", user.get().getUserName());

		} else {
			response.put("status", "fail");
			response.put("message", "아이디 또는 비밀번호가 잘못되었습니다.");
		}

		return response;
	}

	// ✅ 로그아웃 처리 API 추가
	@PostMapping("/logout")
	public Map<String, String> logoutUser(HttpServletRequest request) {
		Map<String, String> response = new HashMap<>();
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // 세션 삭제
		}

		response.put("status", "success");
		response.put("message", "로그아웃 성공!");
		return response;
	}

	// ✅ 회원 정보 조회 (선호 유형 포함)
	@GetMapping("/profile/{userId}")
	public Map<String, Object> getUserProfile(@PathVariable String userId) {
		Map<String, Object> response = new HashMap<>();
		Optional<User> user = userRepository.findByUserId(userId);
		Optional<UserPreferences> preferences = preferencesRepository.findById(userId);

		if (user.isPresent()) {
			response.put("status", "success");
			response.put("user", user.get());
			response.put("preferences", preferences.orElse(new UserPreferences())); // 선호 유형 정보 없으면 기본 객체 반환
		} else {
			response.put("status", "fail");
			response.put("message", "사용자를 찾을 수 없습니다.");
		}

		return response;
	}

	// ✅ 회원 정보 수정 (이름, 이메일 변경 가능)
	@PutMapping("/update")
	public Map<String, String> updateUser(@RequestBody Map<String, String> params) {
		Map<String, String> response = new HashMap<>();
		Optional<User> user = userRepository.findByUserId(params.get("userId"));

		if (user.isPresent()) {
			User updateUser = user.get();
			updateUser.setUserName(params.get("userName"));
			updateUser.setUserEmail(params.get("userEmail"));

			userRepository.save(updateUser);
			response.put("status", "success");
			response.put("message", "회원 정보가 업데이트되었습니다.");
		} else {
			response.put("status", "fail");
			response.put("message", "사용자를 찾을 수 없습니다.");
		}

		return response;
	}

	// ✅ 선호 정보 업데이트 API
	@PutMapping("/update-preferences")
	public Map<String, String> updateUserPreferences(@RequestBody Map<String, String> params) {
		Map<String, String> response = new HashMap<>();
		Optional<UserPreferences> preferences = preferencesRepository.findById(params.get("userId"));

		if (preferences.isPresent()) {
			UserPreferences updatePreferences = preferences.get();
			updatePreferences
					.setPreferredFood(params.getOrDefault("preferredFood", updatePreferences.getPreferredFood()));
			updatePreferences
					.setPreferredMood(params.getOrDefault("preferredMood", updatePreferences.getPreferredMood()));

			if (params.containsKey("travelPurpose") && params.get("travelPurpose") != null) {
				updatePreferences.setTravelPurpose(
						UserPreferences.TravelPurpose.valueOf(params.get("travelPurpose").toUpperCase()));
			}

			preferencesRepository.save(updatePreferences);
			response.put("status", "success");
			response.put("message", "선호 정보가 업데이트되었습니다.");
		} else {
			response.put("status", "fail");
			response.put("message", "사용자의 선호 정보가 없습니다.");
		}

		return response;
	}

	// ✅ 회원 탈퇴 (DELETE 요청)
	@DeleteMapping("/delete/{userId}")
	public Map<String, String> deleteUser(@PathVariable String userId) {
		Map<String, String> response = new HashMap<>();
		Optional<User> user = userRepository.findByUserId(userId);

		if (user.isPresent()) {
			preferencesRepository.deleteById(userId);
			userRepository.delete(user.get());
			response.put("status", "success");
			response.put("message", "회원 탈퇴가 완료되었습니다.");
		} else {
			response.put("status", "fail");
			response.put("message", "해당 사용자를 찾을 수 없습니다.");
		}

		return response;
	}

	// ✅ 로그인 유저 정보 확인하기
	@GetMapping("/session-user")
	public Map<String, String> getSessionUser(HttpServletRequest request) {
		Map<String, String> response = new HashMap<>();
		HttpSession session = request.getSession(false); // 세션이 없으면 null 반환

		if (session != null && session.getAttribute("userId") != null) {
			response.put("status", "success");
			response.put("userId", (String) session.getAttribute("userId"));
			response.put("userName", (String) session.getAttribute("userName"));
		} else {
			response.put("status", "fail");
			response.put("message", "로그인된 사용자가 없습니다.");
		}

		return response;
	}
	
	

    @PostMapping("/findUser")  // ✅ UUID만 입력받는 방식으로 변경
    public ResponseEntity<?> findUser(@RequestBody Map<String, String> requestData) {
        String userUuid = requestData.get("uuid");

        User user = userService.getUserByUuid(userUuid);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

}
