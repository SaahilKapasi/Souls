package me.lithid.souls;

import me.lithid.souls.commands.SoulsCommand;
import me.lithid.souls.database.DatabaseManager;
import me.lithid.souls.listeners.PlayerDeathListener;
import me.lithid.souls.listeners.PlayerJoinListener;
import me.lithid.souls.listeners.PlayerQuitListener;
import me.lithid.souls.managers.SoulsManager;
import me.lithid.souls.tasks.OnlineTimeTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Souls extends JavaPlugin {

    private static String connectionURL;
    private static Souls instance;
    private SoulsManager manager;
    private final ConcurrentHashMap<UUID, Integer> souls = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Integer> onlineTime = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;
        connectionURL = "jdbc:mysql://" + getConfig().getString("database.host") + "/" + getConfig().getString("database.name");
        manager = new SoulsManager();

        DatabaseManager.initializeDatabase();
        registerListeners();
        registerCommands();

        Bukkit.getScheduler().runTaskTimer(this, OnlineTimeTask::updateOnlineTime, 0L, 20L);
    }

    @Override
    public void onDisable() {
        for (Map.Entry<UUID, Integer> entry : souls.entrySet()) DatabaseManager.setLives(entry.getKey(), entry.getValue());
        for (Map.Entry<UUID, Integer> entry : onlineTime.entrySet()) DatabaseManager.setTime(entry.getKey(), entry.getValue());
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
    }

    public void registerCommands() {
        getCommand("souls").setExecutor(new SoulsCommand());
        getCommand("souls").setTabCompleter(new SoulsCommand());
    }

    public static String getConnectionURL() {
        return connectionURL;
    }

    public static Souls getInstance() {
        return instance;
    }

    public SoulsManager getManager() {
        return manager;
    }

    public ConcurrentHashMap<UUID, Integer> getSouls() {
        return souls;
    }

    public ConcurrentHashMap<UUID, Integer> getOnlineTime() {
        return onlineTime;
    }

}
