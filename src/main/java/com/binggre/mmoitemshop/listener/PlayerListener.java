package com.binggre.mmoitemshop.listener;

import com.binggre.mmoitemshop.MMOItemTrade;
import com.binggre.mmoitemshop.objects.PlayerTrade;
import com.binggre.mmoitemshop.repository.PlayerRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final PlayerRepository repository = MMOItemTrade.getPlugin().getPlayerRepository();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerTrade playerTrade = repository.findById(player.getUniqueId());
        if (playerTrade == null) {
            playerTrade = new PlayerTrade(player);
            repository.save(playerTrade);
        } else {
            playerTrade.updateNickname(player);
            playerTrade.reloadDateState();
        }
        repository.putIn(playerTrade);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerTrade remove = repository.remove(event.getPlayer().getUniqueId());
        if (remove != null) {
            repository.save(remove);
        }
    }
}