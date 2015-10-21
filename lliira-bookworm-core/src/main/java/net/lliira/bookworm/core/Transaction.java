package net.lliira.bookworm.core;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import net.lliira.bookworm.core.BookwormHelper;

public class Transaction {

    private final TransactionStatus transactionStatus;

    Transaction(final TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void rollback() {
        final PlatformTransactionManager transactionManager = BookwormHelper
                .get(PlatformTransactionManager.class);
        transactionManager.rollback(transactionStatus);
    }

    public void commit() {
        final PlatformTransactionManager transactionManager = BookwormHelper
                .get(PlatformTransactionManager.class);
        transactionManager.commit(transactionStatus);
    }
}
