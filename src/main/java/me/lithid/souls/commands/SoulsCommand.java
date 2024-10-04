package me.lithid.souls.commands;

import me.lithid.souls.Souls;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SoulsCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if ((sender instanceof Player player)) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(Souls.getInstance().getConfig().getString("messages.souls", "<green>You have <dark_green>%lives% <green> souls.").replace("%lives%", String.valueOf(String.valueOf(Souls.getInstance().getSouls().get(player.getUniqueId()))))));
            }
            return true;
        }

        if (!sender.hasPermission("souls.admin")) {
            sender.sendMessage(Component.text("You don't have permission to do that!", NamedTextColor.RED));
            return true;
        }
        
        UUID uuid = Bukkit.getPlayerUniqueId(args[1]);
        if (uuid == null) {
            sender.sendMessage(Component.text("No player found!", NamedTextColor.RED));
            return true;
        }

        switch (args.length) {
            case 2 -> {
                if (args[0].equalsIgnoreCase("get")) {
                    sender.sendMessage(Component.text(args[1] + " has %lives% souls!".replace("%lives%", String.valueOf(Souls.getInstance().getSouls().get(uuid))), NamedTextColor.GREEN));
                }
                else sendCommands(sender);
            }
            case 3 -> {
                if (args[0].equalsIgnoreCase("set")) {
                    Souls.getInstance().getSouls().put(uuid, Integer.parseInt(args[2]));
                    sender.sendMessage(Component.text("Lives set!", NamedTextColor.GREEN));
                }
                else if (args[0].equalsIgnoreCase("add")) {
                    Souls.getInstance().getSouls().put(uuid, Souls.getInstance().getSouls().get(uuid) + Integer.parseInt(args[2]));
                    sender.sendMessage(Component.text("Lives added!", NamedTextColor.GREEN));
                }
                else if (args[0].equalsIgnoreCase("remove")) {
                    Souls.getInstance().getSouls().put(uuid, Math.max(0, Souls.getInstance().getSouls().get(uuid) - Integer.parseInt(args[2])));
                    sender.sendMessage(Component.text("Lives removed!", NamedTextColor.GREEN));
                }
                else sendCommands(sender);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(List.of("set", "add", "remove", "get"));
        }
        if (args.length == 2) {
            List<String> result = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) result.add(player.getName());
            return result;
        }
        return null;
    }

    public void sendCommands(CommandSender sender) {
        sender.sendMessage(Component.text("Commands:", NamedTextColor.RED));
        sender.sendMessage(Component.text("/souls set <name> <lives>", NamedTextColor.RED));
        sender.sendMessage(Component.text("/souls add <name> <lives>", NamedTextColor.RED));
        sender.sendMessage(Component.text("/souls remove <name> <lives>", NamedTextColor.RED));
        sender.sendMessage(Component.text("/souls get <name>", NamedTextColor.RED));
    }
}
