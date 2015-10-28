package org.lliira.bookworm.core.persist.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.persist.mapper.CategoryBookMapper;
import net.lliira.bookworm.core.persist.mapper.CategoryMapper;
import net.lliira.bookworm.core.persist.model.BookEntity;
import net.lliira.bookworm.core.persist.model.CategoryBookEntity;
import net.lliira.bookworm.core.persist.model.CategoryEntity;

public class CategoryMapperTest extends AbstractTest {

    private CategoryMapper categoryMapper;

    @BeforeMethod
    public void prepareMapper() {
        this.categoryMapper = BookwormHelper.get(CategoryMapper.class);
    }

    @Test
    public void testInsert() {
        final String name = "name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final float siblingIndex = 0;
        CategoryEntity category = new CategoryEntity();
        category.setName(name);
        category.setDescription(description);
        category.setSiblingIndex(siblingIndex);

        // test insert
        final int count = categoryMapper.insert(category);
        Assert.assertEquals(count, 1);
        compare(category, null, name, null, description, siblingIndex);

        final Integer id = category.getId();
        Assert.assertNotNull(id);

        // test get to make sure insert succeeded.
        category = categoryMapper.select(id);
        compare(category, id, name, null, description, siblingIndex);

        // test adding a sibling on root level;
        final CategoryEntity category2 = PersistTestHelper.createCategory(null);
        Assert.assertEquals(category2.getSiblingIndex(), 1F);

        final List<CategoryEntity> categories = categoryMapper.selectRoots();
        Assert.assertEquals(categories.size(), 2);
        compare(categories.get(0), category);
        compare(categories.get(1), category2);
    }

    @Test
    public void testInsertWithParent() {
        final CategoryEntity parent = PersistTestHelper.createCategory(null);

        final CategoryEntity category1 = PersistTestHelper.createCategory(parent);
        final CategoryEntity category2 = PersistTestHelper.createCategory(parent);
        Assert.assertEquals(category1.getSiblingIndex(), 1F);
        Assert.assertEquals(category2.getSiblingIndex(), 2F);

        final List<CategoryEntity> categories = categoryMapper.selectByParent(parent);
        Assert.assertEquals(categories.size(), 2);
        compare(categories.get(0), category1);
        compare(categories.get(1), category2);
    }

    @Test
    public void testUpdate() {
        CategoryEntity category = PersistTestHelper.createCategory(null);
        final Integer id = category.getId();
        final String name = "name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final float siblingIndex = 2F;
        final CategoryEntity parent = PersistTestHelper.createCategory(null);
        category.setName(name);
        category.setDescription(description);
        category.setSiblingIndex(siblingIndex);
        category.setParentId(parent.getId());

        final int count = categoryMapper.update(category);
        Assert.assertEquals(count, 1);
        compare(category, id, name, parent.getId(), description, siblingIndex);

        category = categoryMapper.select(id);
        compare(category, id, name, parent.getId(), description, siblingIndex);
    }

    @Test
    public void testDelete() {
        CategoryEntity category = PersistTestHelper.createCategory(null);
        final Integer id = category.getId();
        final int count = categoryMapper.delete(category);
        Assert.assertEquals(count, 1);
        category = categoryMapper.select(id);
        Assert.assertNull(category);
    }

    @Test
    public void testSelectByBook() {
        final CategoryEntity category1 = PersistTestHelper.createCategory(null);
        final CategoryEntity category2 = PersistTestHelper.createCategory(null);
        final BookEntity book1 = PersistTestHelper.createBook();
        final BookEntity book2 = PersistTestHelper.createBook();

        final CategoryBookMapper catBookMapper = BookwormHelper.get(CategoryBookMapper.class);

        final CategoryBookEntity cat1book1 = new CategoryBookEntity(category1.getId(), book1.getId(), 0);
        final CategoryBookEntity cat2book1 = new CategoryBookEntity(category2.getId(), book1.getId(), 0);
        final CategoryBookEntity cat2book2 = new CategoryBookEntity(category2.getId(), book2.getId(), 1);
        catBookMapper.insert(cat1book1);
        catBookMapper.insert(cat2book1);
        catBookMapper.insert(cat2book2);

        final List<CategoryEntity> categories = categoryMapper.selectByBook(book1);
        compare(categories, category1, category2);
    }

    private void compare(final Collection<CategoryEntity> actual, final CategoryEntity... expected) {
        Assert.assertEquals(actual.size(), expected.length);
        // create a map to store expected authors
        final Map<Integer, CategoryEntity> map = new HashMap<>(expected.length);
        for (final CategoryEntity category : expected) {
            map.put(category.getId(), category);
        }
        for (final CategoryEntity category : actual) {
            final CategoryEntity expectedCategory = map.get(category.getId());
            if (expectedCategory != null) compare(category, expectedCategory);
            else Assert.assertTrue(false, "unexpected author");
        }
    }

    private void compare(final CategoryEntity actual, final CategoryEntity expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getName(), expected.getName());
        Assert.assertEquals(actual.getDescription(), expected.getDescription());
        Assert.assertEquals(actual.getSiblingIndex(), expected.getSiblingIndex());
        Assert.assertEquals(actual.getParentId(), expected.getParentId());
    }

    private void compare(final CategoryEntity actual, final Integer id, final String name,
            final Integer parentId, final String description, final float siblingIndex) {
        Assert.assertEquals(actual.getName(), name);
        Assert.assertEquals(actual.getParentId(), parentId);
        Assert.assertEquals(actual.getSiblingIndex(), siblingIndex);
        Assert.assertEquals(actual.getDescription(), description);
        if (id != null) Assert.assertEquals(actual.getId(), id);
    }
}
