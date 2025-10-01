package com.binggre.mmoitemshop.event;

import com.binggre.mmoitemshop.objects.TradeObject;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ItemTradeEvent extends Event {

    public static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final int tradeId;
    private final boolean success;
    private final TradeObject tradeObject;

    public ItemTradeEvent(Player player, int tradeId, boolean success, TradeObject tradeObject) {
        this.player = player;
        this.tradeId = tradeId;
        this.success = success;
        this.tradeObject = tradeObject;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
