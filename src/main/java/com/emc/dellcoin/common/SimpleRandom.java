package com.emc.dellcoin.common;


public class SimpleRandom {
    private int seed;

    public SimpleRandom(int seed) {
        this.seed = seed % 2147483647;
        if (this.seed <= 0) this.seed += 2147483646;
    }

    /**
     * Returns a pseudo-random value between 1 and 2^32 - 2.
     */
    public int nextInt() {
        return this.seed = this.seed * 16807 % 2147483647;
    }

    /**
     * Returns a pseudo-random int number in range [0, range).
     */
    public int nextInt(int range) {
        // We know that result of next() will be 1 to 2147483646 (inclusive).
        return range * (nextInt() - 1) / 2147483646;
    }
}
