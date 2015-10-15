package org.lliira.bookworm.calibre.mapper;

import java.util.List;

import org.lliira.bookworm.calibre.model.CalibreAuthor;
import org.lliira.bookworm.calibre.model.CalibreBook;
import org.lliira.bookworm.calibre.model.CalibreLibrary;
import org.lliira.bookworm.calibre.model.CalibreReadFlag;
import org.lliira.bookworm.calibre.model.CalibreSeries;

public interface CalibreMapper {

	CalibreLibrary selectLibrary();
	
	int selectReadColumnIndex();
	
	List<CalibreSeries> selectSeries();
	
	List<CalibreBook> selectBooks(int seriesId);
	
	List<CalibreAuthor> selectAuthors(int bookId);

	boolean selectReadFlag(CalibreReadFlag flag);
}
