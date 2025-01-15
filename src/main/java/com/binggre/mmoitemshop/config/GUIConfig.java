package com.binggre.mmoitemshop.config;

import com.binggre.binggreapi.objects.items.CustomItemStack;
import com.binggre.binggreapi.utils.ColorManager;
import com.binggre.binggreapi.utils.file.FileManager;
import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mongolibraryplugin.MongoLibraryPlugin;
import com.binggre.mongolibraryplugin.base.MongoConfiguration;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

@Getter
public class GUIConfig extends MongoConfiguration {

    private static GUIConfig instance = null;

    public static GUIConfig getInstance() {
        if (instance == null) {
            instance = new GUIConfig(MMOItemTrade.DATA_BASE_NAME, "Config-GUI");
        }
        return instance;
    }

    private String title = "[거래] <name>";
    private final int size = 6;

    private final List<Integer> resultSlots = List.of(10, 11, 12, 19, 20, 21, 28, 29, 30);
    private final List<Integer> materialSlots = List.of(14, 15, 16, 23, 24, 25, 32, 33, 34);

    private final CustomItemStack trade = CustomItemStack.create(22, "chest", "§a교환", List.of(), 1, 0);
    private final CustomItemStack previous = CustomItemStack.create(47, "arrow", "§7이전", List.of(), 1, 0);
    private final CustomItemStack next = CustomItemStack.create(51, "arrow", "§7다음", List.of(), 1, 0);

    private GUIConfig(String database, String collection) {
        super(database, collection);
    }

    @Override
    public void init() {
        Document configDocument = getConfigDocument();

        if (configDocument != null) {
            instance = FileManager.toObject(configDocument.toJson(), GUIConfig.class);
            instance.title = ColorManager.format(instance.title);
        }
    }
}