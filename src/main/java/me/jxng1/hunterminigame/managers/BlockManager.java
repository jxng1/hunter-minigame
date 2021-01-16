package me.jxng1.hunterminigame.managers;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class BlockManager {

    private Set<Material> allowedToBreak = new HashSet<>();
    private GameManager gameManager;

    public BlockManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean canBreak(Block block) {
        return allowedToBreak.contains(block.getType());
    }
}
