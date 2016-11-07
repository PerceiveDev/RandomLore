package com.perceivedev.randomlore.data;

import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import com.perceivedev.randomlore.RandomLore;
import com.perceivedev.randomlore.random.RandomPicker;

/**
 * Parses the config to a RandomPicker
 */
public class LoreParser {

    /**
     * Parses the config.
     *
     * @param section The {@link ConfigurationSection} to parse from
     *
     * @return The created RandomPicker.
     *
     * @throws IllegalStateException If no lores were found at the end
     */
    public static RandomPicker<LoreObject> parse(ConfigurationSection section) {
        Objects.requireNonNull(section, "The section 'lores' doesn't exist in the config. This is a severe error!");

        RandomPicker<LoreObject> picker = new RandomPicker<>();
        Logger logger = RandomLore.getInstance().getLogger();

        for (String key : section.getKeys(false)) {
            ConfigurationSection configurationSection = section.getConfigurationSection(key);

            if (!validateCorrectFormat(configurationSection)) {
                logger.warning("Corrupted key: '" + key + "'. No lore or weight given or in a wrong format.");
                logger.warning("Detailed data: [Is null: "
                        + (configurationSection == null)
                        + "] [Misses Lore: "
                        + (!configurationSection.contains("lore")
                                + "] [Misses Weight: "
                                + (!configurationSection.contains("weight")
                                        + "]")));
                continue;
            }

            picker.add(new LoreObject(configurationSection));
        }

        // This is an error, you know.
        if (picker.getAmount() == 0) {
            logger.severe("No lores found. Disabling this plugin.");
            Bukkit.getPluginManager().disablePlugin(RandomLore.getInstance());
            throw new IllegalStateException("No lores found!");
        }

        return picker;
    }

    private static boolean validateCorrectFormat(ConfigurationSection section) {
        return section != null && section.contains("lore") && section.contains("weight");
    }
}
