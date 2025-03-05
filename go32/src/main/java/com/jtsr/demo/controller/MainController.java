package com.jtsr.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping("/")
    public String main() {
        return "main";  // "index.html"을 불러옴 (static 폴더 안에 있으면 자동 연결)
    }
    
    @RequestMapping("/goMain")
    public String goMain() {
        return "main";
    }
    
    @GetMapping("/go32")
    public String mainPage(HttpSession session, Model model) {
        // 세션에서 userId 가져오기
        String userId = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");

        // 로그인 상태 확인
        if (userId != null) {
            model.addAttribute("userId", userId);
            model.addAttribute("userName", userName);
        }

        return "main"; // main.jsp 파일로 이동
    }
    
    @RequestMapping("/chatRoom")
    public String chatRoom() {
    		return "chatRoom";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "main";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    		session.invalidate();
    		return "main"; // 리다이렉트 처리
    }
    
    @RequestMapping("/cald")
    public String cald() {
        return "cal";
    }

}
