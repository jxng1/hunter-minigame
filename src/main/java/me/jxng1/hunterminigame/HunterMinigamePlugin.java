package me.jxng1.hunterminigame;

import me.jxng1.hunterminigame.commands.GenerateCluesCommand;
import me.jxng1.hunterminigame.commands.RemoveCluesCommand;
import me.jxng1.hunterminigame.commands.StartCommand;
import me.jxng1.hunterminigame.commands.StopCommand;
import me.jxng1.hunterminigame.listeners.BlockBreakListener;
import me.jxng1.hunterminigame.listeners.EventListener;
import me.jxng1.hunterminigame.managers.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HunterMinigamePlugin extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        super.onEnable();

        this.gameManager = new GameManager(this);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new EventListener(gameManager), this);

        getCommand("start").setExecutor(new StartCommand(gameManager));
        getCommand("stop").setExecutor(new StopCommand(gameManager));
        getCommand("createclue").setExecutor(new GenerateCluesCommand(gameManager));
        getCommand("removeclue").setExecutor(new RemoveCluesCommand(gameManager));
    }

    @Override
    public void onDisable() {
        super.onDisable();

        gameManager.cleanup();
    }
}
