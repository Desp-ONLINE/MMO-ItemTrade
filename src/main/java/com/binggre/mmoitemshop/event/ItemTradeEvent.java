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

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Player player;
    private final int id;
    private final TradeObject tradeObject;
    private final boolean success;

    public ItemTradeEvent(Player player, boolean success, int id, TradeObject tradeObject) {
        this.player = player;
        this.id = id;
        this.success = success;
        this.tradeObject = tradeObject;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
