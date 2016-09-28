package com.perceivedev.randomlore.random;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.perceivedev.randomlore.RandomLore;

/**
 * Picks a random entry
 *
 * @param <T> The type of the entry
 */
public class RandomPicker<T extends WeightedObject> {

    private boolean  dirty;
    private double[] cumSum;
    private double   totalSum;
    private List<T> objects = new ArrayList<T>();

    /**
     * Adds an entry if it doesn't already exist
     *
     * @param object The object to add
     */
    public void add(T object) {
        if (!objects.contains(object)) {
            objects.add(object);
            dirty = true;
        }
    }

    /**
     * Removes an object
     *
     * @param object The object to remove
     */
    public void remove(T object) {
        objects.remove(object);
        dirty = true;
    }

    /**
     * Returns the amount of objects to choose from
     *
     * @return The amount of objects in this parser
     */
    public int getAmount() {
        return objects.size();
    }

    private void update() {
        cumSum = new double[objects.size()];

        totalSum = 0;
        for (int i = 0; i < objects.size(); i++) {
            T object = objects.get(i);
            double weight = object.getWeight();

            totalSum += weight;

            cumSum[i] = totalSum;
        }

        dirty = false;
    }

    public T getRandom() {
        if (objects.isEmpty()) {
            return null;
        }

        if (dirty) {
            update();
        }

        double random = ThreadLocalRandom.current().nextDouble(totalSum);

        for (int i = 0; i < objects.size(); i++) {
            T object = objects.get(i);

            if (cumSum[i] > random) {
                return object;
            }
        }

        // this can't happen
        RandomLore.getInstance().getLogger().warning("What the hell happened?"
                  + " The sums didn't line up o_O."
                  + " Report this and pray to a deity of your choice.");
        return null;
    }
}
