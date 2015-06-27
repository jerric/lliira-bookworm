package net.lliira.bookworm.core.persist.entity;

public class CategoryBookEntity {

	private final Integer categoryId;
	private final Integer bookId;

	private Integer id;
	private int order;

	/**
	 * @param categoryId
	 * @param bookId
	 */
	public CategoryBookEntity(Integer categoryId, Integer bookId) {
		super();
		this.categoryId = categoryId;
		this.bookId = bookId;
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
	 * @return the categoryId
	 */
	public Integer getCategoryId() {
		return categoryId;
	}

	/**
	 * @return the bookId
	 */
	public Integer getBookId() {
		return bookId;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order
	 *           the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

}
