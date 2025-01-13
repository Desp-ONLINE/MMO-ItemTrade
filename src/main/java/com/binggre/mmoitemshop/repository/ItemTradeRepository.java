package com.binggre.mmoitemshop.repository;

import com.binggre.binggreapi.utils.file.FileManager;
import com.binggre.mmoitemshop.objects.ItemTrade;
import com.binggre.mongolibraryplugin.base.MongoCachedRepository;
import org.bson.Document;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class ItemTradeRepository extends MongoCachedRepository<Integer, ItemTrade> {

    public ItemTradeRepository(Plugin plugin, String database, String collection, Map<Integer, ItemTrade> cache) {
        super(plugin, database, collection, cache);
    }

    @Override
    public Document toDocument(ItemTrade itemTrade) {
        return Document.parse(FileManager.toJson(itemTrade));
    }

    @Override
    public ItemTrade toEntity(Document document) {
        return FileManager.toObject(document.toJson(), ItemTrade.class);
    }

    public void init() {
        cache.clear();

        List<ItemTrade> findAll = findAll();
        for (ItemTrade itemTrade : findAll) {
            putIn(itemTrade);
        }
    }
}