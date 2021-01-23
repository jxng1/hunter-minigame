package me.jxng1.hunterminigame;

import me.jxng1.hunterminigame.commands.*;
import me.jxng1.hunterminigame.listeners.BlockBreakListener;
import me.jxng1.hunterminigame.listeners.EventListener;
import me.jxng1.hunterminigame.managers.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HunterPlugin extends JavaPlugin {

    private GameManager gameManager;
    public HunterPlugin plugin;

    @Override
    public void onEnable() {
        this.gameManager = new GameManager(this);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new EventListener(gameManager), this);

        getCommand("leavequeue").setExecutor(new LeaveQueueCommand(gameManager));
        getCommand("start").setExecutor(new StartCommand(gameManager));
        getCommand("stop").setExecutor(new StopCommand(gameManager));
        getCommand("createclue").setExecutor(new GenerateCluesCommand(gameManager));
        getCommand("removeclue").setExecutor(new RemoveCluesCommand(gameManager));
    }

    @Override
    public void onDisable() {
        gameManager.cleanup();
    }
}
