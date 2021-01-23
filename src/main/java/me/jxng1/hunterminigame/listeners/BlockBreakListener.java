package me.jxng1.hunterminigame.listeners;

import me.jxng1.hunterminigame.managers.GameManager;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final GameManager gameManager;

    public BlockBreakListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private void onBlockBreak(BlockBreakEvent event) {
        if (!gameManager.getBlockManager().canBreak(event.getBlock())) {
            event.setCancelled(true);
        }
    }

}
