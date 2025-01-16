package com.binggre.mmoitemshop.objects;

import lombok.Getter;

import java.util.List;

@Getter
public class TradeObject {

    private int reTradeMin;
    private int maxCount;

    private List<TradeItem> materials;
    private List<TradeItem> results;

}