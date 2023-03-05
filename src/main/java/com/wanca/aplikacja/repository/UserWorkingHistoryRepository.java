package com.wanca.aplikacja.repository;

import com.wanca.aplikacja.entity.WorkingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface UserWorkingHistoryRepository extends JpaRepository<WorkingHistory, Long> {

    Optional<WorkingHistory> findFirstByDateAndEndDateIsNullOrderByStartDateDesc(LocalDate startDate);

    Collection<WorkingHistory> findWorkingHistoryByUser_Id(Long userId);

    Optional<WorkingHistory> findFirstByUser_IdAndDateAndEndDateIsNullOrderByStartDateDesc(Long userId, LocalDate date);

}
