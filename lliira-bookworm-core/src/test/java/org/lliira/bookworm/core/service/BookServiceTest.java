package org.lliira.bookworm.core.service;

import org.lliira.bookworm.core.AbstractTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.service.BookService;

public class BookServiceTest extends AbstractTest {

    private BookService bookService;
    
    @BeforeMethod
    public void prepareService() {
        this.bookService = BookwormHelper.get(BookService.class);
    }

    @Test
    public void testCreate() {
        Assert.assertTrue(false);
    }
    
    @Test
    public void testUpdate() {
        Assert.assertTrue(false);
    }
    
    @Test
    public void testDeleteWithAuthors() {
        Assert.assertTrue(false);
    }
    
    @Test
    public void testDeleteWithCategories() {
        Assert.assertTrue(false);
    }
    
    @Test
    public void testSetAuthors() {
        Assert.assertTrue(false);
    }
    
    @Test
    public void testSetCategoriesWithInsert() {
        Assert.assertTrue(false);
    }
    
    @Test
    public void testSetCategoriesWithDelete() {
        Assert.assertTrue(false);
    }
    
    @Test
    public void testSetCategoriesWithUpdate() {
        Assert.assertTrue(false);
    }
    
    @Test
    public void testSetCategoriesWithFullNormalize() {
        Assert.assertTrue(false);
    }
    
    @Test
    public void testSetCategoriesWithPartialNormalize() {
        Assert.assertTrue(false);
    }

}
