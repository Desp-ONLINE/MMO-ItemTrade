package com.binggre.mmoitemshop.gui;

import com.binggre.binggreapi.functions.HolderListener;
import com.binggre.binggreapi.functions.PageInventory;
import com.binggre.binggreapi.objects.items.CustomItemStack;
import com.binggre.binggreapi.utils.InventoryManager;
import com.binggre.binggreapi.utils.ItemManager;
import com.binggre.binggreapi.utils.NumberUtil;
import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mmoitemshop.config.GUIConfig;
import com.binggre.mmoitemshop.config.MessageConfig;
import com.binggre.mmoitemshop.event.ItemTradeEvent;
import com.binggre.mmoitemshop.objects.*;
import com.binggre.mmoitemshop.repository.PlayerRepository;
import com.binggre.mmoplayerdata.utils.MMOUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
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

    public static void closeAll() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (inventory.getHolder() instanceof TradeGUI) {
                player.closeInventory();
            }
        });
    }

    private static final PlayerRepository playerRepository = MMOItemTrade.getPlugin().getPlayerRepository();

    private final Inventory inventory;
    private final ItemStack AIR = new ItemStack(Material.AIR);
    private final Player player;
    private final int tradeId;
    private final MMOTrade mmoTrade;
    private TradeObject tradeObject;
    private int page;
    private final int lastPage;

    private TradeGUI(Player player, MMOTrade mmoTrade) {
        this.page = 1;
        this.player = player;
        this.mmoTrade = mmoTrade;
        this.tradeId = mmoTrade.getId();
        this.lastPage = mmoTrade.getPageCount();
        this.inventory = create();
    }

    private Inventory create() {
        GUIConfig guiConfig = guiConfig();
        Inventory inventory = Bukkit.createInventory(this, guiConfig.getSize() * 9, Component.text(guiConfig.getTitle().replace("<name>", mmoTrade.getName())));

        CustomItemStack previous = guiConfig.getPrevious();
        CustomItemStack next = guiConfig.getNext();

        inventory.setItem(previous.getSlot(), previous.getItemStack());
        inventory.setItem(next.getSlot(), next.getItemStack());

        return inventory;
    }

    private void refresh() {
        TradeObject trade = mmoTrade.getTrade(page);
        if (trade == null) {
            return;
        }
        tradeObject = trade;

        GUIConfig guiConfig = guiConfig();
        ItemStack tradeButton;

        PlayerTrade playerTrade = playerRepository.get(player.getUniqueId());
        playerTrade.reloadDateState();
        TradeLog tradeLog = playerTrade.findTradeLog(tradeId, page);

        if (isTradableDate() && isTradableAmount()) {
            tradeButton = guiConfig.getTrade().getItemStack();

            switch (tradeObject.getReTradeMin()) {
                case -1 -> {
                    if (tradeLog == null) {
                        ItemManager.addLore(tradeButton, "§f거래 제한 : 0 / ∞");
                    } else {
                        ItemManager.addLore(tradeButton, String.format("§f거래 제한 : %s / ∞", tradeLog.getAmount()));
                    }
                }
                case -2 -> {
                    if (tradeLog == null) {
                        ItemManager.addLore(tradeButton, String.format("§f거래 제한 : 0 / %s", tradeObject.getMaxCount()));
                    } else {
                        ItemManager.addLore(tradeButton, String.format("§f거래 제한 : %s / %s", tradeLog.getAmount(), tradeObject.getMaxCount()));
                    }
                }
            }

        } else {
            int nextSeconds = playerTrade.getNextSeconds(tradeObject, tradeId, page);
            tradeButton = ItemManager.create(Material.PAPER, guiConfig.getCantTradeDisplay(), guiConfig.getCantTradeLore());
            ItemManager.setCustomModelData(tradeButton, 10003);
            ItemManager.replaceLore(tradeButton, "<time>", NumberUtil.toTimeString(nextSeconds));
            ItemManager.replaceLore(tradeButton, "<min>", tradeLog.getAmount() + "");
            ItemManager.replaceLore(tradeButton, "<max>", tradeObject.getMaxCount() + "");
        }

        inventory.setItem(guiConfig.getTrade().getSlot(), tradeButton);

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

        var tradeEvent = new Object() {

            void call(boolean success) {
                ItemTradeEvent tradeEvent = new ItemTradeEvent(player, success, tradeId, tradeObject);
                tradeEvent.callEvent();
            }
        };

        int slot = event.getSlot();
        if (slot == guiConfig().getTrade().getSlot()) {
            refresh();

            if (!isTradableAmount()) {
                player.sendMessage(messageConfig().getOverAmount());
                tradeEvent.call(false);
                return;
            }
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
            player.playSound(player, "uisounds:purchase1", 1, 1);
            log();
            refresh();
            tradeEvent.call(true);
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

    private boolean isTradableDate() {
        PlayerTrade playerTrade = playerRepository.get(player.getUniqueId());
        return playerTrade.isTradableDate(tradeObject, tradeId, page);
    }

    private boolean isTradableAmount() {
        PlayerTrade playerTrade = playerRepository.get(player.getUniqueId());
        return playerTrade.isTradableAmount(tradeObject, tradeId, page);
    }

    private void log() {
        PlayerTrade playerTrade = playerRepository.get(player.getUniqueId());
        if (playerTrade == null) {
            playerTrade = new PlayerTrade(player);
            playerRepository.putIn(playerTrade);
        }
        playerTrade.log(tradeId, page);
        playerRepository.save(playerTrade);
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
        movePageClear();
        refresh();
        player.playSound(player.getLocation(), Sound.valueOf(messageConfig().getNextSound()), 1, 1);
    }

    @Override
    public void previous() {
        page = Math.max(1, page - 1);
        movePageClear();
        player.playSound(player.getLocation(), Sound.valueOf(messageConfig().getPreviousSound()), 1, 1);
        refresh();
    }

    private void movePageClear() {
        if (tradeObject == null) {
            return;
        }
        tradeObject.getMaterials().forEach(tradeItem -> inventory.setItem(tradeItem.getSlotIndex(), AIR));
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
        return MMOItemTrade.getPlugin().getGuiConfig();
    }

    private MessageConfig messageConfig() {
        return MMOItemTrade.getPlugin().getMessageConfig();
    }
}
