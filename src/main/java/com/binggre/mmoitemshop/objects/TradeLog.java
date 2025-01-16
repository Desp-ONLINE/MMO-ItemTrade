package com.binggre.mmoitemshop.objects;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TradeLog {

    private int amount;
    private final int page;
    private LocalDateTime date;

    public TradeLog(int page, int amount) {
        this.page = page;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public void increaseAmount() {
        amount++;
        date = LocalDateTime.now();
    }
}