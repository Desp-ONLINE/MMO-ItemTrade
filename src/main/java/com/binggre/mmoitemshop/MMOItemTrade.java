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

    private GUIConfig guiConfig;
    private MessageConfig messageConfig;
    private ItemTradeRepository tradeRepository;
    private PlayerRepository playerRepository;

    @Override
    public void onEnable() {
        instance = this;
        saveResource("example.json", true);
        guiConfig = new GUIConfig(DATA_BASE_NAME, "Config-GUI");
        messageConfig = new MessageConfig(DATA_BASE_NAME, "Config-Message");
        tradeRepository = new ItemTradeRepository(this, DATA_BASE_NAME, "Trade", new HashMap<>());
        tradeRepository.init();
        playerRepository = new PlayerRepository(this, DATA_BASE_NAME, "Player", new HashMap<>());
        playerRepository.init();

        executeCommand(this, new AdminCommand());
    }

    @Override
    public void onDisable() {
        playerRepository.init();

        guiConfig.save();
        messageConfig.save();
    }
}
