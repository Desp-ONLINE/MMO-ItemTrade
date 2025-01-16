package com.binggre.mmoitemshop;

import com.binggre.binggreapi.BinggrePlugin;
import com.binggre.mmoitemshop.commands.AdminCommand;
import com.binggre.mmoitemshop.config.GUIConfig;
import com.binggre.mmoitemshop.config.MMOTradeConfig;
import com.binggre.mmoitemshop.config.MessageConfig;
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
        saveResource("example.json", true);

        tradeRepository = new ItemTradeRepository(this, "MMO-ItemTrade", "Trade", new HashMap<>());
        tradeRepository.init();
        GUIConfig.getInstance().init();
        MMOTradeConfig.getInstance().init();
        MessageConfig.getInstance().init();
        executeCommand(this, new AdminCommand());
    }

    @Override
    public void onDisable() {
        GUIConfig.getInstance().save();
        MMOTradeConfig.getInstance().save();
        MessageConfig.getInstance().save();
    }
}
