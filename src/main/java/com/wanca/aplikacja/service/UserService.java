package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.UserDto;
import com.wanca.aplikacja.dto.WorkingHistoryDto;

import java.util.Collection;

public interface UserService {
    void registerUser(UserDto userDto);

    void createWorkingHistory(long userId, long shopId);

    void endCurrentWorkDate(long userId);

    Collection<WorkingHistoryDto> getUserWorkingHistory(long userId);
}
