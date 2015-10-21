package net.lliira.bookworm.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class BookwormHelper {
    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        if (BookwormHelper.context == null) {
            synchronized (BookwormHelper.class) {
                if (BookwormHelper.context == null) {
                    final GenericXmlApplicationContext context = new GenericXmlApplicationContext();
                    context.load("classpath:META-INF/app-context.xml");
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
        final DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        final PlatformTransactionManager transactionManager = BookwormHelper
                .get(PlatformTransactionManager.class);
        final TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
        return new Transaction(transactionStatus);
    }

}
