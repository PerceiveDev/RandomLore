package com.perceivedev.randomlore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.perceivedev.randomlore.data.LoreObject;
import com.perceivedev.randomlore.listener.LoreListener;
import com.perceivedev.randomlore.random.RandomPicker;

/**
 * @author Rayzr
 */
public class RandomLore extends JavaPlugin {

    private static RandomLore instance;

    private List<LoreObject>         loreObjects;
    private RandomPicker<LoreObject> randomPicker;

    @Override
    public void onEnable() {
        instance = this;

        new File(getDataFolder(), "config.yml").delete();
        saveDefaultConfig();

        parseSections();

        Bukkit.getPluginManager().registerEvents(new LoreListener(), this);
    }

    @Override
    public void onDisable() {
        // prevent the old instance from still being around.
        instance = null;
    }

    private void parseSections() {
        loreObjects = new ArrayList<>();

        for (String key : getConfig().getConfigurationSection("lores").getKeys(false)) {
            ConfigurationSection configurationSection = getConfig().getConfigurationSection("lores." + key);
            System.out.println(configurationSection);
            if (!validateCorrectFormat(configurationSection)) {
                System.out.println("Corrupted key: '" + key + "'. No lore or percentage given or in a wrong format.");
                continue;
            }
            loreObjects.add(new LoreObject(configurationSection));
        }

        randomPicker = new RandomPicker<>();
        loreObjects.forEach(randomPicker::add);
    }

    private boolean validateCorrectFormat(ConfigurationSection section) {
        return section != null && section.contains("lore") && section.contains("weight");
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
