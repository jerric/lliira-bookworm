package net.lliira.bookworm.core;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Book {

	private final Set<Author> authors = new HashSet<>();
	private final Map<Category, Float> series = new HashMap<>();

	private Long id;
	private String name;
	private Date publishDate;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	public void setId() {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the publishDate
	 */
	public Date getPublishDate() {
		return publishDate;
	}

	/**
	 * @param publishDate
	 *           the publishDate to set
	 */
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

}
