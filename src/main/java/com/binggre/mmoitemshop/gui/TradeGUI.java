package com.binggre.mmoitemshop.gui;

import com.binggre.binggreapi.functions.HolderListener;
import com.binggre.binggreapi.functions.PageInventory;
import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mmoitemshop.objects.ItemTrade;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class TradeGUI implements InventoryHolder, HolderListener, PageInventory {

    public static void open(Player player, int tradeId) {
        TradeGUI gui = new TradeGUI(player, tradeId);
        player.openInventory(gui.inventory);
        gui.refresh();
    }

    private final Inventory inventory;
    private final Player player;
    private int page;
    private ItemTrade itemTrade;

    private TradeGUI(Player player, int tradeId) {
        this.player = player;
        inventory = create();
        itemTrade = MMOItemTrade.getInstance().getTradeRepository().get(tradeId);
    }

    private Inventory create() {
        Inventory inventory = Bukkit.createInventory(this, 5 * 9, Component.text("Trade"));
        return inventory;
    }

    public void refresh() {
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    @Override
    public void onDrag(InventoryDragEvent inventoryDragEvent) {

    }

    @Override
    public void next() {

    }

    @Override
    public void previous() {

    }

    @Override
    public int getNextSlot() {
        return 0;
    }

    @Override
    public int getPreviousSlot() {
        return 0;
    }

    @Override
    public int getPage() {
        return 0;
    }
}
