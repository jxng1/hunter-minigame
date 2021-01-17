package me.jxng1.hunterminigame.commands;

import me.jxng1.hunterminigame.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveQueueCommand implements CommandExecutor {

    private final GameManager gameManager;

    public LeaveQueueCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            this.gameManager.getPlayerList().remove(commandSender);
            Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GOLD + ((Player) commandSender).getDisplayName() + ChatColor.RED + " has left the game queue!");
            Bukkit.broadcastMessage(ChatColor.RED + "" + (this.gameManager.PLAYER_REQUIREMENT - this.gameManager.getplayerListSize()) + ChatColor.BOLD + "" + ChatColor.GOLD + " more players required to start the minigame.");
            return true;
        } else {
            return false;
        }
    }
}
