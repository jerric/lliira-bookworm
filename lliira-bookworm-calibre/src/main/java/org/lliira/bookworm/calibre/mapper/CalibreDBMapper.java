package org.lliira.bookworm.calibre.mapper;

import java.util.List;

import org.lliira.bookworm.calibre.model.CalibreAuthor;
import org.lliira.bookworm.calibre.model.CalibreBook;
import org.lliira.bookworm.calibre.model.CalibreLibrary;
import org.lliira.bookworm.calibre.model.CalibreSeries;

public interface CalibreDBMapper {

	CalibreLibrary selectLibrary();
	
	int selectReadColumnIndex();
	
	List<CalibreSeries> selectSeries();
	
	List<CalibreBook> selectBooks(final int seriesId);
	
	List<CalibreAuthor> selectAuthors(final int bookId);

	List<Integer> selectReadBooks(final int columnId);
}
