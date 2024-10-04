package me.lithid.souls.listeners;

import me.lithid.souls.Souls;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        int lives = Souls.getInstance().getSouls().get(player.getUniqueId());
        int newLives = lives - 1;
        if (lives > 0) {
            event.setKeepInventory(true);
            event.getDrops().clear();
            Souls.getInstance().getSouls().put(player.getUniqueId(), newLives);
            player.sendMessage(MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("messages.deathLives", "<red>You died and now have <dark_red>%lives% soul(s) <red>left! If you die with no souls you will lose your items!").replace("%lives%", String.valueOf(newLives))));
            player.showTitle(Title.title(MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("titles.deathLives.title", "<dark_red>You died!")), MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("titles.deathLives.subtitle", "<red>You have <dark_red>%lives% <red>lives left!").replace("%lives%", String.valueOf(newLives)))));
        }
        else {
            player.sendMessage(MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("messages.deathNoLives", "<red>You died with no souls and lost your items!")));
            player.showTitle(Title.title(MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("titles.deathNoLives.title", "<dark_red>You died!")), MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("titles.deathNoLives.subtitle", "<red>You had no souls left so you lost your items!"))));
        }
    }
}
