package me.lithid.souls.listeners;

import me.lithid.souls.Souls;
import me.lithid.souls.database.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent event) {
        DatabaseManager.setLivesAsync(event.getPlayer().getUniqueId(), Souls.getInstance().getSouls().get(event.getPlayer().getUniqueId()));
        DatabaseManager.setTimeAsync(event.getPlayer().getUniqueId(), Souls.getInstance().getOnlineTime().get(event.getPlayer().getUniqueId()));
    }
}
