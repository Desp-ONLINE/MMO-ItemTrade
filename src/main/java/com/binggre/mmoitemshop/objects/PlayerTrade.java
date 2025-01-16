package com.binggre.mmoitemshop.objects;

import com.binggre.mongolibraryplugin.base.MongoData;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerTrade implements MongoData<UUID> {

    private UUID id;
    // trade Id, amount
    private Map<Integer, Integer> tradeLogs;

    @Override
    public UUID getId() {
        return id;
    }

    public boolean isTradable(MMOTrade mmoTrade, int page) {
       return false;
    }

    public void log(MMOTrade mmoTrade) {

    }
}
