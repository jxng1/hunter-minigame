package me.jxng1.hunterminigame.managers;

import me.jxng1.hunterminigame.HunterMinigamePlugin;
import me.jxng1.hunterminigame.tasks.GameStartCountdownTask;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameManager {

    private final HunterMinigamePlugin plugin;
    private GameState gameState = GameState.LOBBY;

    private final BlockManager blockManager;
    private final PlayerManager playerManager;
    private final ItemManager itemManager;

    private GameStartCountdownTask gameStartCountdownTask;

    private final List<Player> playerList = new ArrayList<>();

    private Player hunter;
    private final List<Player> survivors = new ArrayList<>();

    private List<ItemStack> listOfClues = new ArrayList<>();
    private List<Location> clueLocations = new ArrayList<>();
    private Map<ItemStack, Boolean> clues = new HashMap<>();
    private int playerRequirement;

    public GameManager(HunterMinigamePlugin plugin) {
        this.plugin = plugin;
        this.playerRequirement = 4;

        this.blockManager = new BlockManager(this);
        this.playerManager = new PlayerManager(this);
        this.itemManager = new ItemManager(this);
    }

    public void setGameState(GameState gameState) {
        // will just return if game is in ACTIVE state and is going in STARTING again.
        if (this.gameState == GameState.ACTIVE && gameState == GameState.STARTING) {
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Game is already in active state!");
            return;
        }
        // return if new gameState is the same as the current one.
        if (this.gameState == gameState) {
            return;
        }

        if (this.gameState != GameState.ACTIVE && gameState == GameState.RESTARTING) {
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Game is not in active state, unable to stop!");
            return;
        }

        this.gameState = gameState;

        switch (gameState) {
            case ACTIVE:
                if (this.gameStartCountdownTask != null) this.gameStartCountdownTask.cancel();
                this.getPlayerManager().sendTitles(ChatColor.GOLD + "Game has started!");
                this.getPlayerManager().giveKits();
                break;
            case STARTING:
                // tp players
                this.gameStartCountdownTask = new GameStartCountdownTask(this);
                this.gameStartCountdownTask.runTaskTimer(plugin, 0, 20);
                this.getPlayerManager().clearInventories();
                this.getPlayerManager().setPlayersMode();
                this.assignTeams();
                this.createClues(this.hunter.getDisplayName());
                // etc
                break;
            case RESTARTING:
                // tp
                this.getPlayerManager().sendTitles(ChatColor.GREEN + "Game is restarting!");
                this.getPlayerManager().setPlayersMode();
                this.getPlayerManager().clearInventories();
                this.unassignRoles();
                this.getPlayerManager().removeScoreboards();
                this.getPlayerManager().sendTitles(ChatColor.GREEN + "Game has ended!"); // Redundant so far...
                break;
        }
    }

    public void addToPlayerList(Player player) {
        this.playerList.add(player);
    }

    public void assignTeams() {
        Player newHunter = playerList.get(new Random().nextInt(playerList.size()));
        setHunter(newHunter);
        this.playerList.stream().filter(player -> player != newHunter).forEach(this::setSurvivor);

        this.getPlayerManager().setGameScoreboard(newHunter, "Hunter");
        this.playerList.forEach(player -> this.getPlayerManager().setGameScoreboard(player, "Survivors"));
    }

    public void unassignRoles() {
        this.hunter = null;
        this.survivors.clear();
    }

    public void setHunter(Player player) {
        this.hunter = player;
    }

    public void setSurvivor(Player player) {
        this.survivors.add(player);
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public ItemManager getItemManager() {
        return this.itemManager;
    }

    public int getplayerListSize() {
        return this.playerList.size();
    }

    public List<Player> getPlayerList() {
        return this.playerList;
    }

    public int getPlayerRequirement() {
        return this.playerRequirement;
    }

    public Player getHunter() {
        return this.hunter;
    }

    public List<Player> getSurvivors() {
        return this.survivors;
    }

    public int getSurvivorListSize() {
        return this.survivors.size();
    }

    public void createClues(String name) {
        for (char c : name.toCharArray()) {
            this.listOfClues.add(this.getItemManager().createGameClue(c));
        }
    }

    public void placeClues(Player player) { // For now, generate the clue chests around the player... //
        for (ItemStack clue : listOfClues) {
            Location playerLocation = player.getLocation();
            World playerWorld = player.getWorld();
            Random random = new Random();
            int x, z;
            Block target;

            do {
                x = playerLocation.getBlockX() + (random.nextInt(50) * (random.nextBoolean() ? -1 : 1));
                z = playerLocation.getBlockZ() + (random.nextInt(50) * (random.nextBoolean() ? -1 : 1));
                target = playerWorld.getBlockAt(x, playerLocation.getBlockY(), z);
            } while (!target.getType().isEmpty());

            target.setType(Material.CHEST);
            Chest chest = (Chest) target.getState();
            Inventory inventory = chest.getInventory();
            inventory.addItem(clue);
            clueLocations.add(target.getLocation());
            clues.put(clue, false);
        }
    }

    public void removeClues() {
        for (Location location : clueLocations) {
            Block target;
            if ((target = location.getBlock()).getType().isEmpty()) {
                Bukkit.getLogger().info("Error: Block doesn't exist at location!");
                break;
            }
            Chest chest = (Chest) target.getState();
            Inventory inventory = chest.getInventory();
            inventory.clear();
            target.setType(Material.AIR);
        }

        clueLocations.clear();
        listOfClues.clear();
    }

    public void cleanup() {
        removeClues();
    }
}
