package com.binggre.mmoitemshop.gui;

import com.binggre.binggreapi.functions.HolderListener;
import com.binggre.binggreapi.functions.PageInventory;
import com.binggre.binggreapi.objects.items.CustomItemStack;
import com.binggre.binggreapi.utils.InventoryManager;
import com.binggre.mmoitemshop.config.GUIConfig;
import com.binggre.mmoitemshop.config.MessageConfig;
import com.binggre.mmoitemshop.objects.MMOTrade;
import com.binggre.mmoitemshop.objects.TradeItem;
import com.binggre.mmoitemshop.objects.TradeObject;
import com.binggre.mmoplayerdata.utils.MMOUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeGUI implements InventoryHolder, HolderListener, PageInventory {

    public static void open(Player player, MMOTrade mmoTrade) {
        TradeGUI gui = new TradeGUI(player, mmoTrade);
        player.openInventory(gui.inventory);
        gui.refresh();
    }

    private final Inventory inventory;
    private final Player player;
    private final MMOTrade mmoTrade;
    private TradeObject tradeObject;
    private int page;
    private final int lastPage;

    private TradeGUI(Player player, MMOTrade mmoTrade) {
        this.page = 1;
        this.player = player;
        this.mmoTrade = mmoTrade;
        this.lastPage = mmoTrade.getPageCount();
        this.inventory = create();
    }

    private Inventory create() {
        GUIConfig guiConfig = guiConfig();
        Inventory inventory = Bukkit.createInventory(this, guiConfig.getSize() * 9, Component.text(guiConfig.getTitle().replace("<name>", mmoTrade.getName())));

        CustomItemStack trade = guiConfig.getTrade();
        CustomItemStack previous = guiConfig.getPrevious();
        CustomItemStack next = guiConfig.getNext();

        inventory.setItem(trade.getSlot(), trade.getItemStack());
        inventory.setItem(previous.getSlot(), previous.getItemStack());
        inventory.setItem(next.getSlot(), next.getItemStack());

        return inventory;
    }

    public void refresh() {
        TradeObject trade = mmoTrade.getTrade(page);
        if (trade == null) {
            return;
        }
        tradeObject = trade;
        tradeObject.getMaterials().forEach(tradeItem -> {
            inventory.setItem(tradeItem.getSlotIndex(), tradeItem.getItem());
        });
        tradeObject.getResults().forEach(tradeItem -> {
            inventory.setItem(tradeItem.getSlotIndex(), tradeItem.getItem());
        });
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != this.inventory) {
            return;
        }

        event.setCancelled(true);

        int slot = event.getSlot();
        if (slot == guiConfig().getTrade().getSlot()) {
            MessageConfig messageConfig = messageConfig();
            PlayerInventory playerInventory = player.getInventory();
            Map<String, Integer> itemAmounts = initAmountMap();

            for (Map.Entry<String, Integer> entry : itemAmounts.entrySet()) {
                String[] typeAndId = entry.getKey().split(":");
                String type = typeAndId[0];
                String id = typeAndId[1];
                int totalAmount = entry.getValue();

                if (!MMOUtil.hasItem(playerInventory, type, id, totalAmount)) {
                    player.sendMessage(messageConfig.getLackMaterialAmount());
                    return;
                }
            }
            List<TradeItem> results = tradeObject.getResults();
            if (!InventoryManager.hasEmpty(playerInventory, results.size())) {
                player.playSound(player.getLocation(), Sound.valueOf(messageConfig.getFailedSound()), 1, 1);
                player.sendMessage(messageConfig.getLackInventoryEmptyAmount());
                return;
            }

            tradeObject.getMaterials().forEach(tradeItem -> {
                MMOUtil.removeItem(playerInventory, tradeItem.getType(), tradeItem.getId(), tradeItem.getAmount());
            });

            player.playSound(player.getLocation(), Sound.valueOf(messageConfig.getTradeSound()), 1, 1);
            player.sendMessage(messageConfig.getTrade());

            for (TradeItem result : tradeObject.getResults()) {
                playerInventory.addItem(result.getItem());
            }
        }
    }

    private @NotNull Map<String, Integer> initAmountMap() {
        List<TradeItem> materials = tradeObject.getMaterials();

        Map<String, Integer> itemAmounts = new HashMap<>();
        for (TradeItem material : materials) {
            String key = material.getType() + ":" + material.getId();
            int amount = material.getAmount();

            itemAmounts.put(key, itemAmounts.getOrDefault(key, 0) + amount);
        }
        return itemAmounts;
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    @Override
    public void onDrag(InventoryDragEvent inventoryDragEvent) {

    }

    @Override
    public void next() {
        page = Math.min(lastPage, page + 1);
        refresh();
        player.playSound(player.getLocation(), Sound.valueOf(messageConfig().getNextSound()), 1, 1);
    }

    @Override
    public void previous() {
        page = Math.max(1, page - 1);
        player.playSound(player.getLocation(), Sound.valueOf(messageConfig().getPreviousSound()), 1, 1);
        refresh();
    }

    @Override
    public int getNextSlot() {
        return guiConfig().getNext().getSlot();
    }

    @Override
    public int getPreviousSlot() {
        return guiConfig().getPrevious().getSlot();
    }

    @Override
    public int getPage() {
        return page;
    }

    private GUIConfig guiConfig() {
        return GUIConfig.getInstance();
    }

    private MessageConfig messageConfig() {
        return MessageConfig.getInstance();
    }
}
