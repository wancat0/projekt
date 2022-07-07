package com.wanca.aplikacja.repository;

import com.wanca.aplikacja.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface UserCalendarRepository extends JpaRepository<Calendar, Long> {

    Optional<Calendar> findFirstByDateAndEndDateIsNullOrderByStartDateDesc(LocalDate startDate);
    Collection<Calendar> findCalendarsByUser_Id(Long userId);
    Optional<Calendar>  findFirstByUser_IdAndDateAndEndDateIsNullOrderByStartDateDesc(Long userId, LocalDate date);

}
