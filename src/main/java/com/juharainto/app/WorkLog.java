package com.juharainto.app;

import java.time.LocalDateTime;

public class WorkLog {
    int id;
    LocalDateTime workStart;
    LocalDateTime workEnd;

    public WorkLog(int id, LocalDateTime workStart, LocalDateTime workEnd){
        this.id = id;
        this.workStart = workStart;
        this.workEnd = workEnd;
    }

}