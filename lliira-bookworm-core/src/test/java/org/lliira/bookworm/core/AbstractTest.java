package org.lliira.bookworm.core;

import java.util.Random;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.Transaction;

public abstract class AbstractTest {

    protected static final Random random = TestHelper.getRandom();

    private Transaction transaction;

    @BeforeMethod
    public final void setUp() {
        transaction = BookwormHelper.beginTransaction();
    }

    @AfterMethod
    public final void shutdown() {
        transaction.rollback();
    }

}
