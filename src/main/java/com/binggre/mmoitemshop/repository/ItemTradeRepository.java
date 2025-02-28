package com.binggre.mmoitemshop.repository;

import com.binggre.binggreapi.utils.file.FileManager;
import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mmoitemshop.objects.MMOTrade;
import com.binggre.mongolibraryplugin.base.MongoCachedRepository;
import org.bson.Document;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ItemTradeRepository extends MongoCachedRepository<Integer, MMOTrade> {

    public ItemTradeRepository(Plugin plugin, String database, String collection, Map<Integer, MMOTrade> cache) {
        super(plugin, database, collection, cache);
    }

    @Override
    public Document toDocument(MMOTrade MMOTrade) {
        return Document.parse(FileManager.toJson(MMOTrade));
    }

    @Override
    public MMOTrade toEntity(Document document) {
        return FileManager.toObject(document.toJson(), MMOTrade.class);
    }

    public void init() {
        cache.clear();

        List<MMOTrade> findAll = findAll();

        if (findAll.isEmpty()) {
            File exampleFile = new File(MMOItemTrade.getPlugin().getDataFolder(), "example.json");
            MMOTrade read = FileManager.read(MMOTrade.class, exampleFile);
            Document document = Document.parse(FileManager.toJson(read));
            save(toEntity(document));
        }

        for (MMOTrade MMOTrade : findAll) {
            putIn(MMOTrade);
        }
    }
}