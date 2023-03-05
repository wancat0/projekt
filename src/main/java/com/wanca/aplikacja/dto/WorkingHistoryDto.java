package com.wanca.aplikacja.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WorkingHistoryDto {
    @JsonProperty("data")
    private final LocalDate date;
    @JsonProperty("początek")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private final LocalDateTime startDate;
    @JsonProperty("koniec")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private final LocalDateTime endDate;
    private final String name;
}
