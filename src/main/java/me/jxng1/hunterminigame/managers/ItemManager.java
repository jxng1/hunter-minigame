package me.jxng1.hunterminigame.managers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private GameManager gameManager;

    public ItemManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public ItemStack createGameClue(char c) {
            ItemStack item = new ItemStack(Material.PAPER, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6" + c);
            List<String> lore = new ArrayList<>();
            lore.add("§7This is one part of the clue to find the hunter...");
            lore.add("§7Find the rest to piece together the name!");
            meta.setLore(lore);
            meta.addEnchant(Enchantment.LUCK, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);

            return item;
    }

}
