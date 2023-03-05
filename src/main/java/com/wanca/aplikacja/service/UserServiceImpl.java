package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.UserDto;
import com.wanca.aplikacja.dto.WorkingHistoryDto;
import com.wanca.aplikacja.entity.User;
import com.wanca.aplikacja.entity.WorkingHistory;
import com.wanca.aplikacja.enums.Role;
import com.wanca.aplikacja.exceptions.UserAlreadyExistsException;
import com.wanca.aplikacja.exceptions.UserNotFoundException;
import com.wanca.aplikacja.repository.RoleRepository;
import com.wanca.aplikacja.repository.ShopRepository;
import com.wanca.aplikacja.repository.UserRepository;
import com.wanca.aplikacja.repository.UserWorkingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final RoleRepository roleRepository;
    private final UserWorkingHistoryRepository userWorkingHistoryRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void registerUser(UserDto userDto) throws UserAlreadyExistsException {
        boolean exists = userRepository.existsUserByEmail(userDto.getEmail());
        if (exists)
            throw new UserAlreadyExistsException(userDto.getEmail());

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setName(userDto.getName());
        newUser.setSurname(userDto.getSurname());
        newUser.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        newUser.addRole(roleRepository.findById(Role.USER.getId()).orElseThrow(RuntimeException::new));
        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void createWorkingHistory(long userId, long shopId) {
        LocalDateTime now = LocalDateTime.now();
        userRepository.findById(userId)
                .map(u -> new WorkingHistory(u, now, shopRepository.getReferenceById(shopId)))
                .map(userWorkingHistoryRepository::save)
                .map(WorkingHistory::getStartDate)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    @Transactional
    public void endCurrentWorkDate(long userId) {
        userWorkingHistoryRepository.findFirstByUser_IdAndDateAndEndDateIsNullOrderByStartDateDesc(userId, LocalDate.now())
                .ifPresent(c -> {
                    c.setEndDate(LocalDateTime.now());
                    userWorkingHistoryRepository.save(c);
                });

    }

    @Override
    @Transactional(readOnly = true)
    public Collection<WorkingHistoryDto> getUserWorkingHistory(long userId) {
        return userWorkingHistoryRepository.findWorkingHistoryByUser_Id(userId)
                .stream()
                .map(c -> new WorkingHistoryDto(c.getDate(), c.getStartDate(), c.getEndDate(), c.getShop().getName()))
                .sorted(Comparator.comparing(WorkingHistoryDto::getStartDate))
                .toList();
    }
}
