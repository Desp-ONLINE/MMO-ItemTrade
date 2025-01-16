package com.binggre.mmoitemshop;

import com.binggre.binggreapi.BinggrePlugin;
import com.binggre.mmoitemshop.commands.AdminCommand;
import com.binggre.mmoitemshop.config.GUIConfig;
import com.binggre.mmoitemshop.config.MessageConfig;
import com.binggre.mmoitemshop.repository.ItemTradeRepository;
import com.binggre.mmoitemshop.repository.PlayerRepository;
import lombok.Getter;

import java.util.HashMap;

@Getter
public final class MMOItemTrade extends BinggrePlugin {

    @Getter
    private static MMOItemTrade instance;
    public static final String DATA_BASE_NAME = "MMO-ItemTrade";
    private ItemTradeRepository tradeRepository;
    private PlayerRepository playerRepository;

    @Override
    public void onEnable() {
        instance = this;
        saveResource("example.json", true);

        tradeRepository = new ItemTradeRepository(this, "MMO-ItemTrade", "Trade", new HashMap<>());
        tradeRepository.init();
        playerRepository = new PlayerRepository(this, "MMO-ItemTrade", "Player", new HashMap<>());
        playerRepository.init();
        GUIConfig.getInstance().init();
        MessageConfig.getInstance().init();
        executeCommand(this, new AdminCommand());
    }

    @Override
    public void onDisable() {
        playerRepository.init();
        GUIConfig.getInstance().save();
        MessageConfig.getInstance().save();
    }
}
