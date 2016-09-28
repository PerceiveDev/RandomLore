package com.perceivedev.randomlore.data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.bukkit.configuration.ConfigurationSection;

import com.perceivedev.randomlore.random.WeightedObject;

/**
 * A Lore
 */
public class LoreObject implements WeightedObject {
    
    private List<String> lore;
    private int          weight;

    /**
     * @param lore The Lore to use
     * @param weight The weight for this lore.
     */
    public LoreObject(List<String> lore, int weight) {
        this.lore = lore;
        this.weight = weight;
    }

    /**
     * Parses the {@link ConfigurationSection}
     *
     * @param section The section to read from
     */
    public LoreObject(ConfigurationSection section) {
        this(section.getStringList("lore"), section.getInt("weight"));
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
    public int getWeight() {
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
