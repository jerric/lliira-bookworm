package net.lliira.bookworm.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class BookwormHelper {

    public static final int PRESICION = 1000;

    private static String contextPath = "classpath:META-INF/app-context.xml";

    private static ApplicationContext context;

    public synchronized static void setContextString(final String contextPath) {
        BookwormHelper.contextPath = contextPath;
        BookwormHelper.context = null;
    }

    public static ApplicationContext getContext() {
        if (BookwormHelper.context == null) {
            synchronized (BookwormHelper.class) {
                if (BookwormHelper.context == null) {
                    final GenericXmlApplicationContext context = new GenericXmlApplicationContext();
                    context.load(contextPath);
                    context.refresh();
                    BookwormHelper.context = context;
                }
            }
        }
        return context;
    }

    public static <T> T get(final Class<T> clazz) {
        return getContext().getBean(clazz);
    }

    public static Transaction beginTransaction() {
        final PlatformTransactionManager transactionManager = BookwormHelper
                .get(PlatformTransactionManager.class);

        final DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        final TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
        return new Transaction(transactionStatus);
    }

    public static int ns(float sibling) {
        return Math.round(sibling * PRESICION);
    }

    public static float ds(int nsibling) {
        return (float) nsibling / PRESICION;
    }

    public static int getIncrement(int nsibling) {
        if (nsibling == 0) return PRESICION;
        int base = 10;
        while (base <= PRESICION && nsibling % base == 0) {
            base *= 10;
        }
        return base /= 10;
    }

    public static String normalizePattern(final String pattern) {
        return pattern.isEmpty() ? "%" : "%" + pattern.replace('*', '%') + "%";
    }
}
