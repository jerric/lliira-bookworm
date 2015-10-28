package org.lliira.bookworm.core.persist.mapper;

import java.util.Arrays;
import java.util.List;

import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.CategoryBookMapper;
import net.lliira.bookworm.core.persist.model.BookEntity;
import net.lliira.bookworm.core.persist.model.CategoryBookEntity;
import net.lliira.bookworm.core.persist.model.CategoryEntity;

public class CategoryBookMapperTest extends AbstractTest {

    private CategoryBookMapper categoryBookMapper;

    @BeforeMethod
    public void prepareMapper() {
        this.categoryBookMapper = BookwormHelper.get(CategoryBookMapper.class);
    }

    @Test
    public void testSelect() {
        final CategoryEntity category = PersistTestHelper.createCategory(null);
        final BookEntity book = PersistTestHelper.createBook();
        final float siblingIndex = 1F;
        final CategoryBookEntity catBook = new CategoryBookEntity(category.getId(), book.getId(), siblingIndex);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook), 1);
        Assert.assertNotNull(catBook.getId());
        Assert.assertEquals(catBook.getBookId(), book.getId());
        Assert.assertEquals(catBook.getCategoryId(), category.getId());
        Assert.assertEquals(catBook.getSiblingIndex(), siblingIndex);
        
        final CategoryBookEntity catBook2 = this.categoryBookMapper.select(catBook.getId());
        compare(catBook2, catBook);
    }
    
    @Test
    public void testSelectByBook() {
        final BookEntity book = PersistTestHelper.createBook();
        final CategoryEntity category1 = PersistTestHelper.createCategory(null);
        final CategoryEntity category2 = PersistTestHelper.createCategory(null);
        final CategoryBookEntity cat1Book = new CategoryBookEntity(category1.getId(), book.getId(), 1F);
        final CategoryBookEntity cat2Book = new CategoryBookEntity(category2.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(cat1Book), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(cat2Book), 1);
        
        final List<CategoryBookEntity> catBooks = this.categoryBookMapper.selectByBook(book);
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
        final CategoryEntity category = PersistTestHelper.createCategory(null);
        final BookEntity book1 = PersistTestHelper.createBook();
        final BookEntity book2 = PersistTestHelper.createBook();
        final CategoryBookEntity catBook1 = new CategoryBookEntity(category.getId(), book1.getId(), 1F);
        final CategoryBookEntity catBook2 = new CategoryBookEntity(category.getId(), book2.getId(), 2F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook1), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook2), 1);
        
        final List<CategoryBookEntity> catBooks = this.categoryBookMapper.selectByCategory(category);
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
        final BookEntity book = PersistTestHelper.createBook();
        final CategoryEntity category1 = PersistTestHelper.createCategory(null);
        final CategoryBookEntity catBook = new CategoryBookEntity(category1.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook), 1);
        
        final CategoryEntity category2 = PersistTestHelper.createCategory(null);
        final float siblingIndex = 3F;
        catBook.setCategoryId(category2.getId());
        catBook.setSiblingIndex(siblingIndex);
        Assert.assertEquals(this.categoryBookMapper.update(catBook), 1);
        
        final CategoryBookEntity catBook2 = this.categoryBookMapper.select(catBook.getId());
        compare(catBook2, catBook);
    }
    
    @Test
    public void testDelete() {
        final BookEntity book = PersistTestHelper.createBook();
        final CategoryEntity category = PersistTestHelper.createCategory(null);
        final CategoryBookEntity catBook = new CategoryBookEntity(category.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook), 1);
        
        Assert.assertEquals(this.categoryBookMapper.delete(catBook), 1);
        
        Assert.assertNull(this.categoryBookMapper.select(catBook.getId()));
    }
    
    @Test
    public void testDeleteByBook() {
        final BookEntity book = PersistTestHelper.createBook();
        final CategoryEntity category1 = PersistTestHelper.createCategory(null);
        final CategoryEntity category2 = PersistTestHelper.createCategory(null);
        final CategoryBookEntity cat1Book = new CategoryBookEntity(category1.getId(), book.getId(), 1F);
        final CategoryBookEntity cat2Book = new CategoryBookEntity(category2.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(cat1Book), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(cat2Book), 1);
        
        Assert.assertEquals(this.categoryBookMapper.deleteByBook(book), 2);
        
        final List<CategoryBookEntity> catBooks = this.categoryBookMapper.selectByBook(book);
        Assert.assertTrue(catBooks.isEmpty());
    }
    
    @Test
    public void testDeleteByCategory() {
        final CategoryEntity category = PersistTestHelper.createCategory(null);
        final BookEntity book1 = PersistTestHelper.createBook();
        final BookEntity book2 = PersistTestHelper.createBook();
        final CategoryBookEntity catBook1 = new CategoryBookEntity(category.getId(), book1.getId(), 1F);
        final CategoryBookEntity catBook2 = new CategoryBookEntity(category.getId(), book2.getId(), 2F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook1), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook2), 1);
        
        Assert.assertEquals(this.categoryBookMapper.deleteByCategory(category), 2);
        
        final List<CategoryBookEntity> catBooks = this.categoryBookMapper.selectByCategory(category);
        Assert.assertTrue(catBooks.isEmpty());
    }
    
    @Test
    public void testDeleteByList() {
        final CategoryEntity category = PersistTestHelper.createCategory(null);
        final BookEntity book1 = PersistTestHelper.createBook();
        final BookEntity book2 = PersistTestHelper.createBook();
        final CategoryBookEntity catBook1 = new CategoryBookEntity(category.getId(), book1.getId(), 1F);
        final CategoryBookEntity catBook2 = new CategoryBookEntity(category.getId(), book2.getId(), 2F);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook1), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(catBook2), 1);
        
        Assert.assertEquals(this.categoryBookMapper.deleteByList(Arrays.asList(catBook1, catBook2)), 2);
        
        final List<CategoryBookEntity> catBooks = this.categoryBookMapper.selectByCategory(category);
        Assert.assertTrue(catBooks.isEmpty());
    }
    
    @Test
    public void testDeleteByCategories() {
        final BookEntity book = PersistTestHelper.createBook();
        final CategoryEntity category1 = PersistTestHelper.createCategory(null);
        final CategoryEntity category2 = PersistTestHelper.createCategory(null);
        final CategoryBookEntity cat1Book = new CategoryBookEntity(category1.getId(), book.getId(), 1F);
        final CategoryBookEntity cat2Book = new CategoryBookEntity(category2.getId(), book.getId(), 1F);
        Assert.assertEquals(this.categoryBookMapper.insert(cat1Book), 1);
        Assert.assertEquals(this.categoryBookMapper.insert(cat2Book), 1);
        
        Assert.assertEquals(this.categoryBookMapper.deleteByCategories(Arrays.asList(category1, category2)), 2);
        
        final List<CategoryBookEntity> catBooks = this.categoryBookMapper.selectByBook(book);
        Assert.assertTrue(catBooks.isEmpty());
    }
    
    private void compare(CategoryBookEntity actual, CategoryBookEntity expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getBookId(), expected.getBookId());
        Assert.assertEquals(actual.getCategoryId(), expected.getCategoryId());
        Assert.assertEquals(actual.getSiblingIndex(), expected.getSiblingIndex());
    }
}
