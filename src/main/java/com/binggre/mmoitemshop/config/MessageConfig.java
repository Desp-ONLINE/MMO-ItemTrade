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

    private String prefix = "[교환] ";
    private String lackInventoryEmptyAmount = "§c인벤토리 공간이 부족합니다.";
    private String lackMaterialAmount = "§c재료가 부족합니다.";
    private String overAmount = "§c더 이상 거래할 수 없습니다.";
    private String trade = "§a거래가 성사되었습니다.";

    private String nextSound = Sound.UI_BUTTON_CLICK.name();
    private String previousSound = Sound.UI_BUTTON_CLICK.name();
    private String tradeSound = Sound.UI_BUTTON_CLICK.name();
    private String failedSound = Sound.UI_BUTTON_CLICK.name();

    public MessageConfig(String database, String collection) {
        super(database, collection);
    }

    @Override
    public void init() {
        Document configDocument = getConfigDocument();
        if (configDocument == null) {
            return;
        }

        String json = configDocument.toJson();
        MessageConfig newInstance = FileManager.toObject(json, MessageConfig.class);
        prefix = ColorManager.format(newInstance.prefix);

        lackMaterialAmount = prefix + ColorManager.format(newInstance.lackMaterialAmount);
        lackInventoryEmptyAmount = prefix + ColorManager.format(newInstance.lackInventoryEmptyAmount);
        overAmount = prefix + ColorManager.format(newInstance.overAmount);
        trade = prefix + ColorManager.format(newInstance.trade);

        nextSound = newInstance.nextSound.toUpperCase().replace(" ", "_");
        previousSound = newInstance.previousSound.toUpperCase().replace(" ", "_");
        tradeSound = newInstance.tradeSound.toUpperCase().replace(" ", "_");
        failedSound = newInstance.failedSound.toUpperCase().replace(" ", "_");
    }
}
