package org.lliira.bookworm.core.service;

import org.lliira.bookworm.core.AbstractTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.service.CategoryService;

public class CategoryServiceTest extends AbstractTest {

    private CategoryService categoryService;
    
    @BeforeMethod
    public void prepareService() {
        this.categoryService = BookwormHelper.get(CategoryService.class);
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
    public void testDeleteWithBooks() {
        Assert.assertTrue(false);
    }
}
