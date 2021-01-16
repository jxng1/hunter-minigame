package me.jxng1.hunterminigame.commands;

import me.jxng1.hunterminigame.managers.GameManager;
import me.jxng1.hunterminigame.managers.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final GameManager gameManager;

    public StartCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            if (this.gameManager.getPlayerList().contains(commandSender)) {
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "You are already in the minigame queue!");
                return false;
            }

            this.gameManager.addToPlayerList((Player)commandSender);

            // FOR TESTING PURPOSES!
            gameManager.setHunter((Player)commandSender);
            gameManager.getPlayerManager().setGameScoreboard((Player)commandSender, "Hunter");
            //
        }

        /*if (this.gameManager.getplayerListSize() >= this.gameManager.getPlayerRequirement()) {
            this.gameManager.setGameState(GameState.STARTING);
        } else {
            commandSender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "You have joined the game queue!");
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + (this.gameManager.getPlayerRequirement() - this.gameManager.getplayerListSize()) + ChatColor.GOLD + " more players are required to start the minigame!");
            return false;
        }*/
        return true;
    }
}
