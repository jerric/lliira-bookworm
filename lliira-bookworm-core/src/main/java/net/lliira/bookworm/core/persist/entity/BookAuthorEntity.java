package net.lliira.bookworm.core.persist.entity;

import java.io.Serializable;

public class BookAuthorEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Integer bookId;
	private final Integer authorId;

	private Integer id;

	/**
	 * @param bookId
	 * @param authorId
	 */
	public BookAuthorEntity(Integer bookId, Integer authorId) {
		super();
		this.bookId = bookId;
		this.authorId = authorId;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *           the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the bookId
	 */
	public Integer getBookId() {
		return bookId;
	}

	/**
	 * @return the authorId
	 */
	public Integer getAuthorId() {
		return authorId;
	}

}
