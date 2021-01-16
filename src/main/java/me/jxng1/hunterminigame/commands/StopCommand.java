package me.jxng1.hunterminigame.commands;

import me.jxng1.hunterminigame.managers.GameManager;
import me.jxng1.hunterminigame.managers.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopCommand implements CommandExecutor {

    private GameManager gameManager;

    public StopCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        gameManager.setGameState(GameState.RESTARTING);

        return false;
    }
}
