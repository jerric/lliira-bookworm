package net.lliira.bookworm.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class BookwormHelper {
    
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

    public static void main(String[] args) {
        long val = Long.MAX_VALUE * 10;
        System.out.println(val);
    }
}
