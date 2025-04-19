package com.binggre.mmoitemshop;

import com.binggre.binggreapi.BinggrePlugin;
import com.binggre.mmoitemshop.commands.AdminCommand;
import com.binggre.mmoitemshop.config.GUIConfig;
import com.binggre.mmoitemshop.config.MessageConfig;
import com.binggre.mmoitemshop.gui.TradeGUI;
import com.binggre.mmoitemshop.listener.PlayerListener;
import com.binggre.mmoitemshop.repository.ItemTradeRepository;
import com.binggre.mmoitemshop.repository.PlayerRepository;
import lombok.Getter;

import java.util.HashMap;

@Getter
public final class MMOItemTrade extends BinggrePlugin {

    @Getter
    private static MMOItemTrade plugin;
    public static final String DATA_BASE_NAME = "MMO-ItemTrade";

    private GUIConfig guiConfig;
    private MessageConfig messageConfig;
    private ItemTradeRepository tradeRepository;
    private PlayerRepository playerRepository;

    @Override
    public void onEnable() {
        plugin = this;
        saveResource("example.json", true);
        guiConfig = new GUIConfig(DATA_BASE_NAME, "Config-GUI");
        guiConfig.init();

        messageConfig = new MessageConfig(DATA_BASE_NAME, "Config-Message");
        messageConfig.init();

        tradeRepository = new ItemTradeRepository(this, DATA_BASE_NAME, "Trade", new HashMap<>());
        tradeRepository.init();
        playerRepository = new PlayerRepository(this, DATA_BASE_NAME, "Player", new HashMap<>());
        playerRepository.init();

        executeCommand(this, new AdminCommand());
        registerEvents(this, new PlayerListener());
    }

    @Override
    public void onDisable() {
        playerRepository.values().forEach(playerTrade -> {
            playerRepository.save(playerTrade);
        });
        TradeGUI.closeAll();
    }
}
