package org.lliira.bookworm.calibre;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.lliira.bookworm.calibre.mapper.CalibreDBMapper;
import org.lliira.bookworm.calibre.model.CalibreLibrary;
import org.omg.IOP.TransactionService;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.Transaction;

/**
 * Hello world!
 *
 */
public class CalibreReader {

    private static final String DB_FILE = "metadata.db";
    private static final String CONFIG_FILE = "META-INF/calibre.xml";
    private static final String DB_FILE_KEY = "db.file";

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: calibreReader <path>");
            System.exit(-1);
        }
        final CalibreReader reader = new CalibreReader();
        reader.read(new File(args[0]));
    }

    public void read(final File root) throws IOException {
        // get all library paths
        final List<File> libraryPaths = new LinkedList<>();
        scanLibraries(root, libraryPaths);
        
        // read libraries
        for (File libraryPath : libraryPaths) {
            readLibrary(libraryPath);
        }
    }

    public void scanLibraries(final File root, final List<File> libraryPaths) {
        final List<File> children = new LinkedList<>();
        for (final File child : root.listFiles()) {
            if (child.isFile() && child.getName().equals(DB_FILE)) {
                // find a db file under current folder, mark it as a library,
                // and skip all its sub-folders.
                libraryPaths.add(root);
                return;
            } else if (child.isDirectory()) {
                children.add(child);
            }
        }
        for (final File child : children) {
            scanLibraries(child, libraryPaths);
        }
    }

    public void readLibrary(File libraryPath) throws IOException {
        // prepare properties
        final Properties properties = new Properties();
        properties.put(DB_FILE_KEY, libraryPath.getAbsolutePath() + "/" + DB_FILE);

        try (final InputStream inputStream = Resources.getResourceAsStream(CONFIG_FILE)) {
            final SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream,
                    properties);
            try (final SqlSession session = sessionFactory.openSession()) {
                final CalibreDBMapper mapper = session.getMapper(CalibreDBMapper.class);
                
                final CalibreLibrary library = readLibraryInfo(mapper);
                
            }
        }
    }
    
    private CalibreLibrary readLibraryInfo(final CalibreDBMapper mapper) {
        final CalibreLibrary library = mapper.selectLibrary();
        library.setReadColumn(mapper.selectReadColumnIndex());
        
        // save the library
//        execute(() -> {
//           
//            
//        });
        
        return library;
    }

    private void readSeries(final CalibreDBMapper mapper) {

    }

    private void readBooks(final CalibreDBMapper mapper) {

    }

    private void readAuthors(final CalibreDBMapper mapper) {

    }
    
    private <T> T execute(final Supplier<T> supplier) {
        final Transaction transaction = BookwormHelper.beginTransaction();
        final T result = supplier.get();
        transaction.commit();
        return result;
    }
}
