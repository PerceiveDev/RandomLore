package com.perceivedev.randomlore.listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.perceivedev.randomlore.RandomLore;
import com.perceivedev.randomlore.data.LoreObject;
import com.perceivedev.randomlore.random.RandomPicker;

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

    private void replaceInInventory(Inventory inventory) {
        for (ItemStack itemStack : inventory) {
            if (!isReplaceableItemStack(itemStack)) {
                continue;
            }
            replaceLore(itemStack);
        }
    }

    private void replaceLore(ItemStack itemStack) {
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

    private DecimalFormat format = new DecimalFormat("#.##");

    // // TODO: 28.09.2016 A debug event. Remove it. 
    // A debug event. Notifies you about the weights for the lores, freshly calculated 
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }
        
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Arrays.asList("Sth", "%LORE%"));
        itemStack.setItemMeta(itemMeta);
        e.getPlayer().getInventory().addItem(itemStack);
        
        RandomPicker<LoreObject> randomPicker = RandomLore.getInstance().getRandomPicker();

        Map<LoreObject, Integer> loreMap = new HashMap<>();
        for (int i = 0; i < 1000000; i++) {
            LoreObject random = randomPicker.getRandom();
            int amount = loreMap.getOrDefault(random, 0) + 1;
            loreMap.put(random, amount);
        }

        int sum = loreMap.values().stream().mapToInt(Integer::intValue).sum();

        for (Entry<LoreObject, Integer> entry : loreMap.entrySet()) {
            double value = entry.getValue() / (double) sum;
            String formatted = format.format(value) + " '" + entry.getKey().getLore() + "'";
            e.getPlayer().sendMessage(formatted);
        }
        e.getPlayer().sendMessage(" ");
    }
}
