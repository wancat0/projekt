package com.wanca.aplikacja.controller;

import com.wanca.aplikacja.dto.CalendarDto;
import com.wanca.aplikacja.security.User;
import com.wanca.aplikacja.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class CalendarController {
    private final UserService userService;

    @GetMapping("/user/work-start-date")
    public LocalDateTime getStartDate(@AuthenticationPrincipal User user) {
        return userService.getCurrentStartDate(LocalDate.now(), user.getId())
                .orElse(null);
    }

    @GetMapping("/user/work-end")
    public void endWork(@AuthenticationPrincipal User user) {
        userService.endCurrentWorkDate(user.getId());
    }

    @GetMapping("/user/work-start")
    public LocalDateTime starWork(@AuthenticationPrincipal User user) {
        return userService.createNewCalendar(user.getId());
    }

    @GetMapping("/user/calendar")
    public Collection<CalendarDto> getCalendar(@AuthenticationPrincipal User user) {
        return userService.getUserCalendars(user.getId());
    }
}
