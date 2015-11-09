package org.lliira.bookworm.core.persist.mapper;

import java.util.Arrays;
import java.util.List;

import org.lliira.bookworm.core.AbstractTest;
import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.CategoryBookMapper;
import net.lliira.bookworm.core.persist.model.BookData;
import net.lliira.bookworm.core.persist.model.CategoryBookData;
import net.lliira.bookworm.core.persist.model.CategoryData;

public class CategoryBookMapperTest extends AbstractTest {

    private CategoryBookMapper categoryBookMapper;

    @BeforeMethod
    public void prepareMapper() {
        this.categoryBookMapper = BookwormHelper.get(CategoryBookMapper.class);
    }

    @Test
    public void testSelect() {
        final CategoryData category = PersistTestHelper.createCategory(null);
        final BookData book = PersistTestHelper.createBook();
        final float siblingIndex = 1F;
        final CategoryBookData catBook = new CategoryBookData(category.getId(), book.getId(), siblingIndex);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook), 1);
        Assert.assertNotNull(catBook.getId());
        Assert.assertEquals(catBook.getBookId(), book.getId());
        Assert.assertEquals(catBook.getCategoryId(), category.getId());
        Assert.assertEquals(catBook.getSiblingIndex(), siblingIndex);
        
        final CategoryBookData catBook2 = this.categoryBookMapper.select(catBook.getId());
        compare(catBook2, catBook);
    }
    
    @Test
    public void testSelectByBook() {
        final BookData book = PersistTestHelper.createBook();
        final CategoryData category1 = PersistTestHelper.createCategory(null);
        final CategoryData category2 = PersistTestHelper.createCategory(null);
        final CategoryBookData cat1Book = new CategoryBookData(category1.getId(), book.getId(), 1F);
        final CategoryBookData cat2Book = new CategoryBookData(category2.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(cat1Book), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(cat2Book), 1);
        
        final List<CategoryBookData> catBooks = this.categoryBookMapper.selectByBook(book);
        Assert.assertEquals(catBooks.size(), 2);
        if (catBooks.get(0).getId() == cat1Book.getId()) {
            compare(catBooks.get(0), cat1Book);
            compare(catBooks.get(1), cat2Book);
        } else {
            compare(catBooks.get(0), cat2Book);
            compare(catBooks.get(1), cat1Book);
        }
    }
    
    @Test
    public void testSelectByCategory() {
        final CategoryData category = PersistTestHelper.createCategory(null);
        final BookData book1 = PersistTestHelper.createBook();
        final BookData book2 = PersistTestHelper.createBook();
        final CategoryBookData catBook1 = new CategoryBookData(category.getId(), book1.getId(), 1F);
        final CategoryBookData catBook2 = new CategoryBookData(category.getId(), book2.getId(), 2F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook1), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook2), 1);
        
        final List<CategoryBookData> catBooks = this.categoryBookMapper.selectByCategory(category);
        Assert.assertEquals(catBooks.size(), 2);
        if (catBooks.get(0).getId() == catBook1.getId()) {
            compare(catBooks.get(0), catBook1);
            compare(catBooks.get(1), catBook2);
        } else {
            compare(catBooks.get(0), catBook2);
            compare(catBooks.get(1), catBook1);
        }
    }
    
    @Test
    public void testUpdate() {
        final BookData book = PersistTestHelper.createBook();
        final CategoryData category1 = PersistTestHelper.createCategory(null);
        final CategoryBookData catBook = new CategoryBookData(category1.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook), 1);
        
        final float siblingIndex = 3F;
        catBook.setSiblingIndex(siblingIndex);
        Assert.assertEquals(this.categoryBookMapper.update(catBook), 1);
        
        final CategoryBookData catBook2 = this.categoryBookMapper.select(catBook.getId());
        compare(catBook2, catBook);
    }
    
    @Test
    public void testDelete() {
        final BookData book = PersistTestHelper.createBook();
        final CategoryData category = PersistTestHelper.createCategory(null);
        final CategoryBookData catBook = new CategoryBookData(category.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook), 1);
        
        Assert.assertEquals(this.categoryBookMapper.delete(catBook), 1);
        
        Assert.assertNull(this.categoryBookMapper.select(catBook.getId()));
    }
    
    @Test
    public void testDeleteByBook() {
        final BookData book = PersistTestHelper.createBook();
        final CategoryData category1 = PersistTestHelper.createCategory(null);
        final CategoryData category2 = PersistTestHelper.createCategory(null);
        final CategoryBookData cat1Book = new CategoryBookData(category1.getId(), book.getId(), 1F);
        final CategoryBookData cat2Book = new CategoryBookData(category2.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(cat1Book), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(cat2Book), 1);
        
        Assert.assertEquals(this.categoryBookMapper.deleteByBook(book), 2);
        
        final List<CategoryBookData> catBooks = this.categoryBookMapper.selectByBook(book);
        Assert.assertTrue(catBooks.isEmpty());
    }
    
    @Test
    public void testDeleteByCategory() {
        final CategoryData category = PersistTestHelper.createCategory(null);
        final BookData book1 = PersistTestHelper.createBook();
        final BookData book2 = PersistTestHelper.createBook();
        final CategoryBookData catBook1 = new CategoryBookData(category.getId(), book1.getId(), 1F);
        final CategoryBookData catBook2 = new CategoryBookData(category.getId(), book2.getId(), 2F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook1), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook2), 1);
        
        Assert.assertEquals(this.categoryBookMapper.deleteByCategory(category), 2);
        
        final List<CategoryBookData> catBooks = this.categoryBookMapper.selectByCategory(category);
        Assert.assertTrue(catBooks.isEmpty());
    }
    
    @Test
    public void testDeleteByList() {
        final CategoryData category = PersistTestHelper.createCategory(null);
        final BookData book1 = PersistTestHelper.createBook();
        final BookData book2 = PersistTestHelper.createBook();
        final CategoryBookData catBook1 = new CategoryBookData(category.getId(), book1.getId(), 1F);
        final CategoryBookData catBook2 = new CategoryBookData(category.getId(), book2.getId(), 2F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook1), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook2), 1);
        
        Assert.assertEquals(this.categoryBookMapper.deleteByList(Arrays.asList(catBook1, catBook2)), 2);
        
        final List<CategoryBookData> catBooks = this.categoryBookMapper.selectByCategory(category);
        Assert.assertTrue(catBooks.isEmpty());
    }
    
    @Test
    public void testDeleteByCategories() {
        final BookData book = PersistTestHelper.createBook();
        final CategoryData category1 = PersistTestHelper.createCategory(null);
        final CategoryData category2 = PersistTestHelper.createCategory(null);
        final CategoryBookData cat1Book = new CategoryBookData(category1.getId(), book.getId(), 1F);
        final CategoryBookData cat2Book = new CategoryBookData(category2.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(cat1Book), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(cat2Book), 1);
        
        Assert.assertEquals(this.categoryBookMapper.deleteByCategories(Arrays.asList(category1, category2)), 2);
        
        final List<CategoryBookData> catBooks = this.categoryBookMapper.selectByBook(book);
        Assert.assertTrue(catBooks.isEmpty());
    }
    
    private void compare(CategoryBookData actual, CategoryBookData expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getBookId(), expected.getBookId());
        Assert.assertEquals(actual.getCategoryId(), expected.getCategoryId());
        Assert.assertEquals(actual.getSiblingIndex(), expected.getSiblingIndex());
    }
}
