package com.binggre.mmoitemshop.repository;

import com.binggre.binggreapi.utils.file.FileManager;
import com.binggre.mmoitemshop.objects.PlayerTrade;
import com.binggre.mongolibraryplugin.base.MongoCachedRepository;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class PlayerRepository extends MongoCachedRepository<UUID, PlayerTrade> {

    public PlayerRepository(Plugin plugin, String database, String collection, Map<UUID, PlayerTrade> cache) {
        super(plugin, database, collection, cache);
    }

    @Override
    public Document toDocument(PlayerTrade playerTrade) {
        return Document.parse(FileManager.toJson(playerTrade));
    }

    @Override
    public PlayerTrade toEntity(Document document) {
        return FileManager.toObject(document.toJson(), PlayerTrade.class);
    }

    public void init() {
        cache.clear();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.closeInventory();
            PlayerTrade byId = findById(onlinePlayer.getUniqueId());
            if (byId == null) {
                byId = new PlayerTrade(onlinePlayer);
                save(byId);
            }
            putIn(byId);
        }
    }
}
