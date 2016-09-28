package com.perceivedev.randomlore.listener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.perceivedev.randomlore.RandomLore;
import com.perceivedev.randomlore.data.LoreObject;
import com.perceivedev.randomlore.random.RandomPicker;

/**
 * The listener
 */
public class LoreListener implements Listener {
    
    DecimalFormat format = new DecimalFormat("#.##");
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        RandomPicker<LoreObject> randomPicker = RandomLore.getInstance().getRandomPicker();

        Map<LoreObject, Integer> loreMap = new HashMap<>();
        for (int i = 0; i < 2000000; i++) {
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
