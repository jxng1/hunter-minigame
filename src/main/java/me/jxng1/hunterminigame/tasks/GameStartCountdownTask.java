package me.jxng1.hunterminigame.tasks;

import me.jxng1.hunterminigame.managers.GameManager;
import me.jxng1.hunterminigame.managers.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartCountdownTask extends BukkitRunnable {

    private GameManager gameManager;

    public GameStartCountdownTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private int timeLeft = 10;

    @Override
    public void run() {
        timeLeft--;
        if (timeLeft <= 0) {
            cancel();
            gameManager.setGameState(GameState.ACTIVE);
            return;
        }

        gameManager.getPlayerManager().sendTitles(timeLeft + "" +  ChatColor.GOLD + " until game starts!");
    }
}
