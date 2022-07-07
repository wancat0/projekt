package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.CalendarDto;
import com.wanca.aplikacja.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface UserService {
    void registerUser(UserDto userDto);

    Optional<LocalDateTime> getCurrentStartDate(LocalDate date, long userId);

    LocalDateTime createNewCalendar(long userId);

    void endCurrentWorkDate(long userId);

    Collection<CalendarDto> getUserCalendars(long userId);
}
