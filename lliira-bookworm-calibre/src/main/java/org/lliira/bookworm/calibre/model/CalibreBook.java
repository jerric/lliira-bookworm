package org.lliira.bookworm.calibre.model;

import java.util.Date;

public class CalibreBook {
	private int id;
	private String uuid;
	private String title;
	private String titleSorted;
	private Date timestamp;
	private Date publishDate;
	private String isbn;
	private float seriesIndex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleSorted() {
		return titleSorted;
	}

	public void setTitleSorted(String titleSorted) {
		this.titleSorted = titleSorted;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public float getSeriesIndex() {
		return seriesIndex;
	}

	public void setSeriesIndex(float seriesIndex) {
		this.seriesIndex = seriesIndex;
	}

}
