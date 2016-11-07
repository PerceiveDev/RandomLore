package com.perceivedev.randomlore.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.perceivedev.randomlore.RandomLore;
import com.perceivedev.randomlore.data.LoreObject;

/**
 * The listener
 */
public class LoreListener implements Listener {

    private final Pattern DETECTION_PATTERN = Pattern.compile(RandomLore.getInstance().getConfig().getString("identifierRegEx"));

    @EventHandler
    public void updateOpenInventory(InventoryOpenEvent e) {
        replaceInInventory(e.getPlayer().getInventory());
        replaceInInventory(e.getInventory());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        replaceInInventory(e.getWhoClicked().getInventory());
        replaceInInventory(e.getInventory());
        replaceLore(e.getCursor());
        replaceLore(e.getCurrentItem());
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        replaceLore(e.getItem());
    }

    @EventHandler
    public void onPlayerCollectItem(PlayerPickupItemEvent e) {
        replaceLore(e.getItem().getItemStack());
    }

    private void replaceInInventory(Inventory inventory) {
        for (ItemStack itemStack : inventory) {
            replaceLore(itemStack);
        }
    }

    private void replaceLore(ItemStack itemStack) {
        if (!isReplaceableItemStack(itemStack)) {
            return;
        }
        ItemMeta meta = itemStack.getItemMeta().clone();
        List<String> newLore = new ArrayList<>();

        LoreObject random = RandomLore.getInstance().getRandomPicker().getRandom();

        for (String loreLine : meta.getLore()) {
            if (!isIdentifier(loreLine)) {
                newLore.add(loreLine);
                continue;
            }
            newLore.addAll(random.getLore());
        }

        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
    }

    private boolean isReplaceableItemStack(ItemStack itemStack) {
        return itemStack != null
                && itemStack.getType() != Material.AIR
                && itemStack.hasItemMeta()
                && itemStack.getItemMeta().hasLore()
                && doesListMatchIdentifier(itemStack.getItemMeta().getLore());
    }

    private boolean doesListMatchIdentifier(List<String> list) {
        for (String s : list) {
            if (isIdentifier(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIdentifier(String string) {
        return DETECTION_PATTERN.matcher(string).matches();
    }

}
