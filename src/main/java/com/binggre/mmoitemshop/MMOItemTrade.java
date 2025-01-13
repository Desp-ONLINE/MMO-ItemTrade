package com.binggre.mmoitemshop;

import com.binggre.binggreapi.BinggrePlugin;
import com.binggre.mmoitemshop.config.GUIConfig;
import com.binggre.mmoitemshop.repository.ItemTradeRepository;
import lombok.Getter;

import java.util.HashMap;

@Getter
public final class MMOItemTrade extends BinggrePlugin {

    @Getter
    private static MMOItemTrade instance;
    public static final String DATA_BASE_NAME = "MMO-ItemTrade";
    private ItemTradeRepository tradeRepository;

    @Override
    public void onEnable() {
        instance = this;
        tradeRepository = new ItemTradeRepository(this, "MMO-ItemTrade", "Trade", new HashMap<>());
        tradeRepository.init();
    }

    @Override
    public void onDisable() {
        GUIConfig.getInstance().save();
    }
}
