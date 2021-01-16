package me.jxng1.hunterminigame.listeners;

import me.jxng1.hunterminigame.managers.GameManager;
import me.jxng1.hunterminigame.managers.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EventListener implements Listener {

    private GameManager gameManager;

    public EventListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (this.gameManager.getPlayerList().contains(event.getPlayer())) {
            this.gameManager.getPlayerList().remove(event.getPlayer());
            Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GOLD + event.getPlayer().getDisplayName() + " has left the game queue!");
            Bukkit.broadcastMessage(ChatColor.RED + "" + (this.gameManager.getPlayerRequirement() - this.gameManager.getplayerListSize()) + ChatColor.BOLD + "" + ChatColor.GOLD + " more players required to start the minigame.");
        }
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        event.setDeathMessage(ChatColor.BOLD + "" + ChatColor.GOLD + event.getEntity().getDisplayName() + " has died!");
        if (this.gameManager.getSurvivors().contains(event.getEntity())) { // hunter wins
            this.gameManager.getSurvivors().remove(event.getEntity());
            gameManager.getPlayerList().forEach(player -> gameManager.getPlayerManager().updateScoreboard(player));

            if (this.gameManager.getSurvivorListSize() <= 0) {  // if survivors empty, set to win state, hunter won
                gameManager.getPlayerManager().sendTitles(ChatColor.GOLD + "The hunter " + ChatColor.RED + this.gameManager.getHunter().getDisplayName() + ChatColor.GOLD + " has won!");
                this.gameManager.setGameState(GameState.RESTARTING);
            }
        } else if (this.gameManager.getHunter() == event.getEntity()) { // survivors win
            gameManager.getPlayerManager().sendTitles(ChatColor.GOLD + "The survivors have won!");
            this.gameManager.setGameState(GameState.RESTARTING);
        }
    }
}
