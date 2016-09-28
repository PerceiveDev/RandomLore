package com.perceivedev.randomlore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.perceivedev.randomlore.data.LoreObject;
import com.perceivedev.randomlore.data.LoreParser;
import com.perceivedev.randomlore.listener.LoreListener;
import com.perceivedev.randomlore.random.RandomPicker;

/**
 * @author Rayzr
 */
public class RandomLore extends JavaPlugin {

    private static RandomLore instance;

    private RandomPicker<LoreObject> randomPicker;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // might throw an error. This is intended.
        randomPicker = LoreParser.parse(getConfig().getConfigurationSection("lores"));
        getLogger().info("Loaded " + randomPicker.getAmount() + " random lores.");

        Bukkit.getPluginManager().registerEvents(new LoreListener(), this);
    }

    @Override
    public void onDisable() {
        // prevent the old instance from still being around.
        instance = null;
    }

    /**
     * @return The random picker
     */
    public RandomPicker<LoreObject> getRandomPicker() {
        return randomPicker;
    }

    /**
     * Returns the plugins instance
     *
     * @return The plugin instance
     */
    public static RandomLore getInstance() {
        return instance;
    }

}
