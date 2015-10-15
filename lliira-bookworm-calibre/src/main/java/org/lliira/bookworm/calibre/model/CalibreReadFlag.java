package org.lliira.bookworm.calibre.model;

public class CalibreReadFlag {

	private final int columnId;
	private final int bookId;

	public CalibreReadFlag(int columnId, int bookId) {
		this.columnId = columnId;
		this.bookId = bookId;
	}

	public int getColumnId() {
		return columnId;
	}

	public int getBookId() {
		return bookId;
	}

}
