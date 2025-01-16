package com.binggre.mmoitemshop.config;

import com.binggre.binggreapi.utils.ColorManager;
import com.binggre.binggreapi.utils.file.FileManager;
import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mongolibraryplugin.base.MongoConfiguration;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Sound;

@Getter
public class MessageConfig extends MongoConfiguration {

    private static MessageConfig instance = null;

    public static MessageConfig getInstance() {
        if (instance == null) {
            instance = new MessageConfig(MMOItemTrade.DATA_BASE_NAME, "Config-Message");
        }
        return instance;
    }

    private String prefix = "[교환] ";
    private String lackInventoryEmptyAmount = "§c인벤토리 공간이 부족합니다.";
    private String lackMaterialAmount = "§c재료가 부족합니다.";
    private String overAmount = "§c더 이상 거래할 수 없습니다.";
    private String trade = "§a거래가 성사되었습니다.";

    private String nextSound = Sound.UI_BUTTON_CLICK.name();
    private String previousSound = Sound.UI_BUTTON_CLICK.name();
    private String tradeSound = Sound.UI_BUTTON_CLICK.name();
    private String failedSound = Sound.UI_BUTTON_CLICK.name();

    private MessageConfig(String database, String collection) {
        super(database, collection);
    }

    @Override
    public void init() {
        Document configDocument = getConfigDocument();

        if (configDocument != null) {
            instance = FileManager.toObject(configDocument.toJson(), MessageConfig.class);
            instance.prefix = ColorManager.format(instance.prefix);
            String prefix = instance.prefix;

            instance.lackMaterialAmount = prefix + ColorManager.format(instance.lackMaterialAmount);
            instance.lackInventoryEmptyAmount = prefix + ColorManager.format(instance.lackInventoryEmptyAmount);
            instance.overAmount = prefix + ColorManager.format(instance.overAmount);
            instance.trade = prefix + ColorManager.format(instance.trade);

            instance.nextSound = instance.nextSound.toUpperCase().replace(" ", "_");
            instance.previousSound = instance.previousSound.toUpperCase().replace(" ", "_");
            instance.tradeSound = instance.tradeSound.toUpperCase().replace(" ", "_");
            instance.failedSound = instance.failedSound.toUpperCase().replace(" ", "_");
        }
    }
}
