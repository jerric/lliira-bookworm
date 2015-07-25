package org.lliira.bookworm.core.persist;

import java.util.Calendar;
import java.util.Random;

import net.lliira.bookworm.core.persist.entity.AuthorEntity;
import net.lliira.bookworm.core.persist.entity.BookEntity;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookMapper;

import org.lliira.bookworm.core.TestHelper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class PersistTestHelper {

	public static TransactionStatus beginTransaction() {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		PlatformTransactionManager transactionManager = TestHelper.get(PlatformTransactionManager.class);
		return transactionManager.getTransaction(definition);
	}

	public static void rollback(TransactionStatus status) {
		PlatformTransactionManager transactionManager = TestHelper.get(PlatformTransactionManager.class);
		transactionManager.rollback(status);
	}

	public static AuthorEntity createAuthor() {
		Random random = TestHelper.getRandom();
		AuthorEntity author = new AuthorEntity();
		author.setName("author-name-" + random.nextInt());
		author.setDescription("author-desc-" + random.nextInt());

		AuthorMapper authorMapper = TestHelper.get(AuthorMapper.class);
		authorMapper.insert(author);

		return author;
	}

	public static BookEntity createBook() {
		Random random = TestHelper.getRandom();
		BookEntity book = new BookEntity();
		book.setName("book-name-" + random.nextInt());
		book.setDescription("book-desc-" + random.nextInt());

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1 * random.nextInt(1000));
		book.setPublishDate(calendar.getTime());

		BookMapper bookMapper = TestHelper.get(BookMapper.class);
		bookMapper.insert(book);

		return book;
	}
}
