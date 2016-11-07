package com.perceivedev.randomlore.data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import com.perceivedev.randomlore.random.WeightedObject;

/**
 * A Lore
 */
public class LoreObject implements WeightedObject {

    private List<String> lore;
    private double       weight;

    /**
     * @param lore The Lore to use
     * @param weight The weight for this lore.
     */
    private LoreObject(List<String> lore, double weight) {
        this.lore = colorList(lore);
        this.weight = weight;
    }

    private List<String> colorList(List<String> list) {
        return list.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
    }

    /**
     * Parses the {@link ConfigurationSection}
     *
     * @param section The section to read from
     */
    LoreObject(ConfigurationSection section) {
        this(section.getStringList("lore"), section.getDouble("weight"));
    }

    /**
     * Returns the lore
     *
     * @return The lore
     */
    public List<String> getLore() {
        return Collections.unmodifiableList(lore);
    }

    /**
     * @return The statistical weight of this entry
     */
    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "LoreObject{" +
                "lore=" + lore +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LoreObject))
            return false;
        LoreObject that = (LoreObject) o;
        return Double.compare(that.weight, weight) == 0 &&
                Objects.equals(lore, that.lore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lore, weight);
    }
}
