package com.wanca.aplikacja.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Entity
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date;
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public Calendar() {
    }

    public Calendar(User user, LocalDateTime startDate) {
        this.user = user;
        this.startDate = startDate;
        this.date = startDate.toLocalDate();
    }
}
