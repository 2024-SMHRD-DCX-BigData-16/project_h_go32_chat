package com.jtsr.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtsr.demo.entity.Schedule;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    /**
     * ✅ 특정 사용자(`userId`)와 날짜(`scheduleDate`)에 해당하는 일정 조회
     */
    List<Schedule> findByUserIdAndScheduleDate(String userId, Date scheduleDate);

    /**
     * ✅ 특정 사용자(`userId`), 일정명(`scheduleName`), 날짜(`scheduleDate`)가 동일한 일정 조회 (중복 방지)
     */
    Optional<Schedule> findByUserIdAndScheduleNameAndScheduleDate(String userId, String scheduleName, Date scheduleDate);

}