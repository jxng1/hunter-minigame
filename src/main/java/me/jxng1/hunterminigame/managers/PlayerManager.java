package me.jxng1.hunterminigame.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerManager {

    private final GameManager gameManager;
    private final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void setPlayersMode() {
        gameManager.getPlayerList().forEach(this::setAdventure);
    }

    public void giveKits() {
        gameManager.getPlayerList().forEach(this::giveKit);
    }

    public void clearInventories() {
        gameManager.getPlayerList().forEach(this::clearInventory);
    }

    public void sendTitles(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        gameManager.getPlayerList().forEach(player -> sendTitle(player, title, subtitle, fadeIn, stay, fadeOut));
    }

    public void removeScoreboards() {
        gameManager.getPlayerList().forEach(this::removeScoreboard);
    }

    public void setGameScoreboard(Player player, String team) {
        Scoreboard scoreboard = this.scoreboardManager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("Title", "dummy", ChatColor.AQUA + "" + ChatColor.BOLD + "HUNTER MINIGAME");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score teamAssigned = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Team: " + ChatColor.RED + team + ChatColor.WHITE);
        teamAssigned.setScore(1);
        Score playersLeft = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Players Left: " + ChatColor.RED + gameManager.getplayerListSize() + ChatColor.WHITE);
        playersLeft.setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("Title");
        Score playersLeft = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Players Left: " + ChatColor.RED + (gameManager.getSurvivorListSize() + 1) + ChatColor.WHITE);

        playersLeft.setScore(0);
        player.setScoreboard(scoreboard);
    }

    public void setAdventure(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
    }

    public void giveKit(Player player) {
        if (gameManager.getHunter() == player) {
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1), new ItemStack(Material.WOODEN_SWORD, 1));
        } else if (gameManager.getSurvivors().contains(player)) {
            player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
        }
    }

    public void clearInventory(Player player) {
        player.getInventory().clear();
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public void removeScoreboard(Player player) {
        player.setScoreboard(this.scoreboardManager.getNewScoreboard());
    }
}
