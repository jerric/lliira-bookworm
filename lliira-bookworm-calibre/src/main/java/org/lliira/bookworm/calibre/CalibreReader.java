package org.lliira.bookworm.calibre;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class CalibreReader 
{
	private static final String DB_FILE = "metadata.db";
	
    public static void main( String[] args ) {
        if (args.length != 1) {
        	System.err.println("Usage: calibreReader <path>");
        	System.exit(-1);
        }
        final CalibreReader reader = new CalibreReader();
        reader.read(new File(args[0]));
    }
    
    public void read(final File root) {
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
    			// find a db file under current folder, mark it as a library, and skip all its sub-folders.
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
    
    public void readLibrary(File libraryPath) {
    	
    }
    
    private void readBooks() {
    	
    }
    
    private void readAuthors() {
    	
    }
    
    private void readCategories() {
    	
    }
}
