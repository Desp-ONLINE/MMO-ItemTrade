package com.binggre.mmoitemshop.objects;

import lombok.AccessLevel;
import lombok.Getter;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.inventory.ItemStack;

@Getter
public class TradeItem {

    private int slotIndex;
    @Getter(AccessLevel.PRIVATE)
    private String mmoItem;
    private int amount;

    public String getType() {
        return mmoItem.split(":")[0];
    }

    public String getId() {
        return mmoItem.split(":")[1];
    }

    public ItemStack getItem() {
        String type = getType();
        String id = getId();

        ItemStack item = MMOItems.plugin.getItem(type, id);
        if (item == null) {
            throw new NullPointerException("MMOItem이 존재하지 않습니다 : " + type + ":" + id);
        }
        item.setAmount(amount);

        return item;
    }
}