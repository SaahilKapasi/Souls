package me.lithid.souls.tasks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.lithid.souls.Souls;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class OnlineTimeTask {

    public static void updateOnlineTime() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isInBlacklistedRegion(player) || player.isDead()) return;
            int lives = Souls.getInstance().getSouls().get(player.getUniqueId());
            if (lives >= Souls.getInstance().getManager().getMaxSouls(player)) return;

            int onlineTime = Souls.getInstance().getOnlineTime().get(player.getUniqueId());
            if (onlineTime + 1 == Souls.getInstance().getConfig().getInt("soulsRegenTime")) {
                Souls.getInstance().getOnlineTime().put(player.getUniqueId(), 0);
                Souls.getInstance().getSouls().put(player.getUniqueId(), lives + 1);
                player.sendMessage(MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("messages.soulRestored", "<green>Enough time has passed and you feel your soul healing, you now have <dark_green>%lives% <green>souls.").replace("%lives%", String.valueOf(lives + 1))));
                player.showTitle(Title.title(MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("titles.soulRestored.title", "<green>You feel your soul healing.")), MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("titles.soulRestored.subtitle", "<green>You now have <dark_green>%lives% <green>souls.").replace("%lives%", String.valueOf(lives + 1)))));
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            }
            else {
                Souls.getInstance().getOnlineTime().put(player.getUniqueId(), onlineTime + 1);
            }
        }
    }

    public static boolean isInBlacklistedRegion(Player player) {
        Location location = BukkitAdapter.adapt(player.getLocation());
        ApplicableRegionSet regionSet = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(location);
        for (ProtectedRegion region : regionSet) {
            for (String blacklistedRegion : Souls.getInstance().getConfig().getStringList("blacklistedRegions")) {
                if (region.getId().equalsIgnoreCase(blacklistedRegion)) return true;
            }
        }
        return false;
    }

}
