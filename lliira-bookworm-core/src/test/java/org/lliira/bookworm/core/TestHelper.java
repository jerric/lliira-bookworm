package org.lliira.bookworm.core;

import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

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
