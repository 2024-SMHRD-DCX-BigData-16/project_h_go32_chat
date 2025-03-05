package com.jtsr.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jtsr.demo.dto.ScheduleData;
import com.jtsr.demo.dto.ScheduleData.TodoData;
import com.jtsr.demo.entity.Schedule;
import com.jtsr.demo.repository.ScheduleRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * ✅ 로컬 스토리지 데이터를 DB와 동기화하는 API
     */
    @PostMapping("/sync")
    public ResponseEntity<String> syncLocalStorageToServer(@RequestBody List<ScheduleData> schedules, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }

        System.out.println("📌 로컬 스토리지 동기화 요청 받음. userId=" + userId);

        for (ScheduleData scheduleData : schedules) {
            for (TodoData todo : scheduleData.getTodos()) {
                // 기존 일정이 있는지 확인
                Optional<Schedule> existingSchedule = scheduleRepository.findByUserIdAndScheduleNameAndScheduleDate(
                        userId, todo.getText(), Date.valueOf(scheduleData.getDate()));

                if (existingSchedule.isEmpty()) {
                    // 새 일정 저장
                    Schedule newSchedule = new Schedule();
                    newSchedule.setUserId(userId);
                    newSchedule.setScheduleName(todo.getText());
                    newSchedule.setScheduleDate(Date.valueOf(scheduleData.getDate()));
                    newSchedule.setPoiId(1); // 기본 값 설정

                    scheduleRepository.save(newSchedule);
                }
            }
        }

        return ResponseEntity.ok("Local storage synced successfully");
    }

    /**
     * ✅ 특정 날짜의 일정 목록 조회 API
     */
    @GetMapping("/list")
    public ResponseEntity<List<Schedule>> getSchedules(@RequestParam("date") String date, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }

        List<Schedule> schedules = scheduleRepository.findByUserIdAndScheduleDate(userId, Date.valueOf(date));

        return ResponseEntity.ok(schedules);
    }

    /**
     * ✅ 일정 추가 API
     */
    @PostMapping("/add")
    public ResponseEntity<Schedule> addSchedule(@RequestBody Schedule schedule, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }

        schedule.setUserId(userId);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return ResponseEntity.ok(savedSchedule);
    }

    /**
     * ✅ 일정 삭제 API
     */
    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);

        if (schedule.isPresent()) {
            scheduleRepository.deleteById(scheduleId);
            return ResponseEntity.ok("Schedule deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Schedule not found");
        }
    }
}
