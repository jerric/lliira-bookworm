package org.lliira.bookworm.core;

import java.util.Random;

public class TestHelper {

    private static Random random;

    public static Random getRandom() {
        if (random == null) {
            synchronized (Random.class) {
                if (random == null) random = new Random();
            }
        }
        return random;
    }

}
