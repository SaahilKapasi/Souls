package me.lithid.souls.managers;

import me.lithid.souls.Souls;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SoulsManager {

    public int getMaxSouls(Player player) {
        return Souls.getInstance().getConfig().getInt("defaultSouls");
    }

    public int getMaxSouls(UUID uuid) {
        return getMaxSouls(Bukkit.getPlayer(uuid));
    }

}
