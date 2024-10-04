package me.lithid.souls.listeners;

import me.lithid.souls.Souls;
import me.lithid.souls.database.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (Souls.getInstance().getSouls().containsKey(event.getPlayer().getUniqueId())) return;
        DatabaseManager.containsPlayer(event.getPlayer().getUniqueId()).thenAccept(contains -> {
            if (!contains) {
                Souls.getInstance().getSouls().put(event.getPlayer().getUniqueId(), Souls.getInstance().getManager().getMaxSouls(event.getPlayer()));
                Souls.getInstance().getOnlineTime().put(event.getPlayer().getUniqueId(), 0);
            }
            else {
                DatabaseManager.getLives(event.getPlayer().getUniqueId()).thenAccept(lives -> Souls.getInstance().getSouls().put(event.getPlayer().getUniqueId(), lives));
                DatabaseManager.getTime(event.getPlayer().getUniqueId()).thenAccept(onlineTime -> Souls.getInstance().getOnlineTime().put(event.getPlayer().getUniqueId(), onlineTime));
            }
        });
    }
}
