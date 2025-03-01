package com.binggre.mmoitemshop.objects;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TradeLog {

    private final int page;
    private int amount;
    private int totalAmount;
    private LocalDateTime lastDate;

    public TradeLog(int page) {
        this.page = page;
        this.amount = 1;
        this.totalAmount = 1;
        this.lastDate = LocalDateTime.now();
    }

    public void increaseAmount() {
        amount++;
        totalAmount++;
        lastDate = LocalDateTime.now();
    }

    public void clearAmount() {
        amount = 0;
    }
}