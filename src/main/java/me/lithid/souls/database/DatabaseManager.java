package me.lithid.souls.database;

import me.lithid.souls.Souls;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(Souls.getConnectionURL(), Souls.getInstance().getConfig().getString("database.user"), Souls.getInstance().getConfig().getString("database.password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void initializeDatabase() {
        Bukkit.getScheduler().runTaskAsynchronously(Souls.getInstance(), () -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Souls`(`UUID` VARCHAR(36) PRIMARY KEY, `Lives` INT NOT NULL);");
                 PreparedStatement preparedStatement1 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `OnlineTime`(`UUID` VARCHAR(36) PRIMARY KEY, `Time` INT NOT NULL);")) {
                preparedStatement.execute();
                preparedStatement1.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<Boolean> containsPlayer(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `Souls` WHERE `UUID` = ?")) {
                preparedStatement.setString(1, uuid.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.isBeforeFirst();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        });
    }

    public static CompletableFuture<Integer> getLives(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT `Lives` FROM `Souls` WHERE `UUID` = ?")) {
                preparedStatement.setString(1, uuid.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next() ? resultSet.getInt("Lives") : 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void setLives(UUID uuid, int lives) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `Souls`(`UUID`, `Lives`) VALUES(?, ?) ON DUPLICATE KEY UPDATE `Lives` = VALUES(`Lives`);")) {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setInt(2, lives);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setLivesAsync(UUID uuid, int lives) {
        Bukkit.getScheduler().runTaskAsynchronously(Souls.getInstance(), () -> setLives(uuid, lives));
    }

    public static CompletableFuture<Integer> getTime(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT `Time` FROM `OnlineTime` WHERE `UUID` = ?")) {
                preparedStatement.setString(1, uuid.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next() ? resultSet.getInt("Time") : 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void setTime(UUID uuid, int time) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `OnlineTime`(`UUID`, `Time`) VALUES(?, ?) ON DUPLICATE KEY UPDATE `Time` = VALUES(`Time`);")) {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setInt(2, time);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTimeAsync(UUID uuid, int time) {
        Bukkit.getScheduler().runTaskAsynchronously(Souls.getInstance(), () -> setTime(uuid, time));
    }
}
