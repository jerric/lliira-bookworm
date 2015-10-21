package org.lliira.bookworm.core.persist;

import java.util.Calendar;
import java.util.Random;

import org.lliira.bookworm.core.TestHelper;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.AuthorMapper;
import net.lliira.bookworm.core.persist.mapper.BookMapper;
import net.lliira.bookworm.core.persist.model.AuthorEntity;
import net.lliira.bookworm.core.persist.model.BookEntity;

public class PersistTestHelper {

	public static AuthorEntity createAuthor() {
		Random random = TestHelper.getRandom();
		AuthorEntity author = new AuthorEntity();
		author.setName("author-name-" + random.nextInt());
		author.setDescription("author-desc-" + random.nextInt());

		AuthorMapper authorMapper = BookwormHelper.get(AuthorMapper.class);
		authorMapper.insert(author);

		return author;
	}

	public static BookEntity createBook() {
		Random random = TestHelper.getRandom();
		BookEntity book = new BookEntity();
		book.setName("book-name-" + random.nextInt());
		book.setSortedName("sorted-name-" + random.nextInt());
		book.setDescription("book-desc-" + random.nextInt());

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1 * random.nextInt(1000));
		book.setPublishDate(calendar.getTime());

		BookMapper bookMapper = BookwormHelper.get(BookMapper.class);
		bookMapper.insert(book);

		return book;
	}
}
