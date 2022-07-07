package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.CalendarDto;
import com.wanca.aplikacja.dto.UserDto;
import com.wanca.aplikacja.entity.Calendar;
import com.wanca.aplikacja.entity.User;
import com.wanca.aplikacja.exceptions.UserAlreadyExistsException;
import com.wanca.aplikacja.exceptions.UserNotFoundException;
import com.wanca.aplikacja.repository.UserCalendarRepository;
import com.wanca.aplikacja.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void registerUser(UserDto userDto) throws UserAlreadyExistsException {

        boolean exists = userRepository.existsUserByEmail(userDto.getEmail());
        if (exists)
            throw new UserAlreadyExistsException(userDto.getEmail());

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setName(userDto.getName());
        newUser.setSurname(userDto.getSurname());
        newUser.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public Optional<LocalDateTime> getCurrentStartDate(LocalDate date, long userId) {
        return userCalendarRepository.findFirstByDateAndEndDateIsNullOrderByStartDateDesc(date)
                .map(Calendar::getStartDate);
    }

    @Override
    public LocalDateTime createNewCalendar(long userId) {
        LocalDateTime now = LocalDateTime.now();
        return userRepository.findById(userId)
                .map(u -> new Calendar(u, now))
                .map(userCalendarRepository::save)
                .map(Calendar::getStartDate)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void endCurrentWorkDate(long userId) {
        userCalendarRepository.findFirstByUser_IdAndDateAndEndDateIsNullOrderByStartDateDesc(userId, LocalDate.now())
                .ifPresent(c -> {
                    c.setEndDate(LocalDateTime.now());
                    userCalendarRepository.save(c);
                });

    }

    @Override
    public Collection<CalendarDto> getUserCalendars(long userId) {
        return userCalendarRepository.findCalendarsByUser_Id(userId)
                .stream()
                .map(c -> new CalendarDto(c.getDate(), c.getStartDate(), c.getEndDate()))
                .sorted(Comparator.comparing(CalendarDto::getStartDate))
                .toList();
    }
}
