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
public class WorkingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    private LocalDate date;
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public WorkingHistory() {
    }

    public WorkingHistory(User user, LocalDateTime startDate, Shop shop) {
        this.user = user;
        this.startDate = startDate;
        this.date = startDate.toLocalDate();
        this.shop = shop;
    }
}
