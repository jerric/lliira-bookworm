package org.lliira.bookworm.core.persist.mapper;

import java.util.Random;

import org.lliira.bookworm.core.TestHelper;
import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.springframework.transaction.TransactionStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractTest {
	
	protected static final Random random = TestHelper.getRandom();
	
	private TransactionStatus status;
	

	@BeforeMethod
	public final void setUp() {
		status = PersistTestHelper.beginTransaction();
	}
	
	@AfterMethod
	public final void shutdown() {
		PersistTestHelper.rollback(status);
	}

}
