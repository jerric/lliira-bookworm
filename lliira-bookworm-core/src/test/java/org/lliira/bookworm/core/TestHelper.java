package org.lliira.bookworm.core;

import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class TestHelper {

	private static ApplicationContext context;
	private static Random random;

	public static ApplicationContext getContext() {
		if (context == null) {
			synchronized (TestHelper.class) {
				if (context == null) {
					GenericXmlApplicationContext context = new GenericXmlApplicationContext();
					context.load("classpath:META-INF/app-context.xml");
					context.refresh();
					TestHelper.context = context;
				}
			}
		}
		return context;
	}

	public static <T> T get(Class<T> clazz) {
		return getContext().getBean(clazz);
	}

	public static Random getRandom() {
		if (random == null) {
			synchronized (Random.class) {
				if (random == null) random = new Random();
			}
		}
		return random;
	}

}
