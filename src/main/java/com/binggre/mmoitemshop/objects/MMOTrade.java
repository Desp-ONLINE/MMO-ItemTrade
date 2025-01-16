package com.binggre.mmoitemshop.objects;

import com.binggre.mongolibraryplugin.base.MongoData;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Map;

@Getter
public class MMOTrade implements MongoData<Integer> {

    private int id;
    private String name;

    @Getter(AccessLevel.PRIVATE)
    // page, object
    private Map<Integer, TradeObject> pages;

    @Override
    public Integer getId() {
        return id;
    }

    public TradeObject getTrade(int page) {
        return pages.get(page);
    }

    public int getPageCount() {
    	return pages.size();
    }
}