package me.jxng1.hunterminigame.commands;

import me.jxng1.hunterminigame.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GenerateCluesCommand implements CommandExecutor {

    private final GameManager gameManager;

    public GenerateCluesCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            this.gameManager.createClues(((Player) commandSender).getDisplayName());
            this.gameManager.placeClues(((Player) commandSender));
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Clues have been created!");
            return true;
        } else {
            return false;
        }
    }
}
