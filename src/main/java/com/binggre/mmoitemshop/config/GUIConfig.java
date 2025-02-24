package com.binggre.mmoitemshop.config;

import com.binggre.binggreapi.objects.items.CustomItemStack;
import com.binggre.binggreapi.utils.ColorManager;
import com.binggre.binggreapi.utils.file.FileManager;
import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mongolibraryplugin.base.MongoConfiguration;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

@Getter
public class GUIConfig extends MongoConfiguration {

    private String title = "[거래] <name>";
    private int size = 6;

    private String tradeDisplay = "§a교환";
    private String cantTradeDisplay = "§c교환 불가";

    private List<String> cantTradeLore = List.of("§f다음 거래까지 <time> 남음.", "§f거래 제한 : <min> / <max>");

    private CustomItemStack trade = CustomItemStack.create(22, "chest", "§a교환", List.of(), 1, 0);
    private CustomItemStack previous = CustomItemStack.create(47, "arrow", "§7이전", List.of(), 1, 0);
    private CustomItemStack next = CustomItemStack.create(51, "arrow", "§7다음", List.of(), 1, 0);

    public GUIConfig(String database, String collection) {
        super(database, collection);
    }

    @Override
    public void init() {
        Document configDocument = getConfigDocument();
        if (configDocument == null) {
            return;
        }
        String json = configDocument.toJson();
        GUIConfig newInstance = FileManager.toObject(json, GUIConfig.class);
        title = ColorManager.format(newInstance.title);
        size = newInstance.size;

        trade = newInstance.trade;
        previous = newInstance.previous;
        next = newInstance.next;

        tradeDisplay = ColorManager.format(newInstance.tradeDisplay);
        cantTradeDisplay = ColorManager.format(newInstance.cantTradeDisplay);
        cantTradeLore = ColorManager.format(newInstance.cantTradeLore);
    }
}