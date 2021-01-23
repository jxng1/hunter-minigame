package me.jxng1.hunterminigame.listeners;

import me.jxng1.hunterminigame.managers.GameManager;
import me.jxng1.hunterminigame.managers.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {

    private final GameManager gameManager;

    public EventListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (this.gameManager.getPlayerList().contains(event.getPlayer())) {
            this.gameManager.getPlayerList().remove(event.getPlayer());
            Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GOLD + event.getPlayer().getDisplayName() + ChatColor.RED + " has left the game queue!");
            Bukkit.broadcastMessage(ChatColor.RED + "" + (GameManager.PLAYER_REQUIREMENT - this.gameManager.getplayerListSize()) + ChatColor.BOLD + "" + ChatColor.GOLD + " more players required to start the minigame.");
        }
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) { // Player killed; if player is part of minigame, proceed; else return early.
        if (!(gameManager.getPlayerList().contains(event.getEntity()))) {
            return; // Return early as player not part of minigame.
        } else {
            event.setDeathMessage(ChatColor.BOLD + "" + ChatColor.GOLD + event.getEntity().getDisplayName() + " has died!");
            if (this.gameManager.getHunter() != event.getEntity()) { // entity is a survivor
                gameManager.getSurvivors().remove(event.getEntity());
                gameManager.getPlayerList().forEach(player -> gameManager.getPlayerManager().updateScoreboard(player));
            } else if (this.gameManager.getHunter() == event.getEntity()) { // entity is a hunter
                gameManager.getPlayerManager().sendTitles(ChatColor.GOLD + "The survivors have won!", "", 1, 70, 1);
                this.gameManager.setGameState(GameState.RESTARTING);
            }

            if (this.gameManager.getSurvivorListSize() <= 0) {  // if survivors list empty, set to win state, hunter won
                gameManager.getPlayerManager().sendTitles(ChatColor.GOLD + "The hunter " + ChatColor.RED + this.gameManager.getHunter().getDisplayName() + ChatColor.GOLD + " has won!", "", 1, 70, 1);
                this.gameManager.setGameState(GameState.RESTARTING);
            }
        }
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) { // Player opens chest; if clue then proceed, else do nothing and return early.
        Block target = event.getClickedBlock();

        if (target == null || target.getType() != Material.CHEST) {
            return; // Not a chest block; return silently.
        }

        Chest chest = (Chest) target.getState();
        Inventory inventory = chest.getInventory();
        ItemStack[] items = inventory.getContents();
        for (ItemStack item : items) {
            if (gameManager.getCluesMap().containsKey(item)) {
                gameManager.getCluesMap().put(item, true);
                char[] maskedStringCharArray = gameManager.getMaskedString().toCharArray();
                int index = gameManager.getNameString().indexOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                for (int i = 0; i < maskedStringCharArray.length; i++) {
                    if (i == index) {
                        maskedStringCharArray[i] = ChatColor.stripColor(item.getItemMeta().getDisplayName()).toCharArray()[0];
                    }
                }
                gameManager.updateMaskedString(maskedStringCharArray);
                gameManager.broadcastName();
                // TODO: broadcast clue found, something to do with the ActionBar...
                break; // Early exit as clue is already found.
            }
        }
    }
}
