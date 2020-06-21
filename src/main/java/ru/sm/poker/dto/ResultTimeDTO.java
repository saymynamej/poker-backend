package ru.sm.poker.dto;

import lombok.*;

import java.util.Timer;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ResultTimeDTO {
    private Timer timer;
    @Builder.Default
    private boolean isDone = false;
    private long startTime;
}
