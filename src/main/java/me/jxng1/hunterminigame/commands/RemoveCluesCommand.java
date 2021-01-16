package me.jxng1.hunterminigame.commands;

import me.jxng1.hunterminigame.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCluesCommand implements CommandExecutor {

    private final GameManager gameManager;

    public RemoveCluesCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            this.gameManager.cleanup();
            Bukkit.getLogger().info("Cleaned up successfully...");
            return true;
        } else {
            return false;
        }
    }
}
