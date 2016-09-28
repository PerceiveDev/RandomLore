package com.perceivedev.randomlore.random;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Picks a random entry
 *
 * @param <T> The type of the entry
 */
public class RandomPicker<T extends WeightedObject> {

    private boolean dirty;
    private int[]   cumSum;
    private int     totalSum;
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

    private void update() {
        cumSum = new int[objects.size()];

        totalSum = 0;
        for (int i = 0; i < objects.size(); i++) {
            T object = objects.get(i);
            int weight = object.getWeight();

            System.out.println("Total: " + totalSum + " -> " + (totalSum + weight) + " " + object.toString());
            
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

        double random = ThreadLocalRandom.current().nextInt(totalSum);

        for (int i = 0; i < objects.size(); i++) {
            T object = objects.get(i);

            if (cumSum[i] > random) {
                return object;
            }
        }
        
        // this can't happen
        System.out.println("WHAT THE HECK HAPPENED");
        return null;
    }
}
