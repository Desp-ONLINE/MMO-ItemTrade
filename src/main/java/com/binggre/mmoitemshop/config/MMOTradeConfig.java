package com.binggre.mmoitemshop.config;

import com.binggre.binggreapi.utils.file.FileManager;
import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mongolibraryplugin.base.MongoConfiguration;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class MMOTradeConfig extends MongoConfiguration {

    private static MMOTradeConfig instance = null;

    public static MMOTradeConfig getInstance() {
        if (instance == null) {
            instance = new MMOTradeConfig(MMOItemTrade.DATA_BASE_NAME, "Config");
        }
        return instance;
    }

    private MMOTradeConfig(String database, String collection) {
        super(database, collection);
    }


    @Override
    public void init() {
        Document configDocument = getConfigDocument();

        if (configDocument != null) {
            instance = FileManager.toObject(configDocument.toJson(), MMOTradeConfig.class);
        }
    }
}
