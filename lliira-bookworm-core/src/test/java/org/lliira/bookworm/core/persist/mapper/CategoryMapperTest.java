package org.lliira.bookworm.core.persist.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lliira.bookworm.core.AbstractTest;
import org.lliira.bookworm.core.persist.PersistTestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.model.Category;
import net.lliira.bookworm.core.persist.mapper.CategoryBookMapper;
import net.lliira.bookworm.core.persist.mapper.CategoryMapper;
import net.lliira.bookworm.core.persist.model.BookData;
import net.lliira.bookworm.core.persist.model.CategoryBookData;
import net.lliira.bookworm.core.persist.model.CategoryData;

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
        final float siblingIndex = 1F;
        CategoryData category = new CategoryData();
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
        final CategoryData category2 = PersistTestHelper.createCategory(null);
        Assert.assertEquals(category2.getSiblingIndex(), 2F);

        final List<CategoryData> roots = categoryMapper.selectRoots();
        Assert.assertEquals(roots.size(), 2);

        // the categories might not be ordered correctly
        Collections.sort(roots, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return Float.compare(o1.getSiblingIndex(), o2.getSiblingIndex());
            }
        });

        compare(roots.get(0), category);
        compare(roots.get(1), category2);
    }

    @Test
    public void testInsertWithParent() {
        final CategoryData parent = PersistTestHelper.createCategory(null);

        final CategoryData category1 = PersistTestHelper.createCategory(parent);
        final CategoryData category2 = PersistTestHelper.createCategory(parent);
        Assert.assertEquals(category1.getSiblingIndex(), 1F);
        Assert.assertEquals(category2.getSiblingIndex(), 2F);

        final List<CategoryData> categories = categoryMapper.selectByParent(parent);
        Assert.assertEquals(categories.size(), 2);
        compare(categories.get(0), category1);
        compare(categories.get(1), category2);
    }

    @Test
    public void testUpdate() {
        CategoryData category = PersistTestHelper.createCategory(null);
        final Integer id = category.getId();
        final String name = "name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final float siblingIndex = 2F;
        final CategoryData parent = PersistTestHelper.createCategory(null);
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
    public void testUpdateParentToNull() {
        final CategoryData parent = PersistTestHelper.createCategory(null);
        final CategoryData child1 = PersistTestHelper.createCategory(parent);

        // assign parent to child2 by update
        final CategoryData child2 = PersistTestHelper.createCategory(null);
        child2.setParentId(parent.getId());
        child2.setSiblingIndex(2);
        Assert.assertEquals(this.categoryMapper.update(child2), 1);

        final List<CategoryData> children = this.categoryMapper.selectByParent(parent);
        Assert.assertEquals(children.size(), 2);
        compare(children.get(0), child1);
        compare(children.get(1), child2);

        // no unset parent
        Assert.assertEquals(this.categoryMapper.updateParentToNull(parent), 2);
        Assert.assertEquals(this.categoryMapper.selectByParent(parent).size(), 0);
        final List<CategoryData> roots = this.categoryMapper.selectRoots();
        Assert.assertEquals(roots.size(), 3);

        // the categories might not be ordered correctly
        Collections.sort(roots, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getId() - o2.getId();
            }
        });

        compare(roots.get(0), parent);
        // cannot use child1 & child2 directly, since the parent has been unset.
        compare(roots.get(1), this.categoryMapper.select(child1.getId()));
        compare(roots.get(2), this.categoryMapper.select(child2.getId()));
    }

    @Test
    public void testDelete() {
        CategoryData category = PersistTestHelper.createCategory(null);
        final Integer id = category.getId();
        final int count = categoryMapper.delete(category);
        Assert.assertEquals(count, 1);
        category = categoryMapper.select(id);
        Assert.assertNull(category);
    }

    @Test
    public void testSelectByBook() {
        final CategoryData category1 = PersistTestHelper.createCategory(null);
        final CategoryData category2 = PersistTestHelper.createCategory(null);
        final BookData book1 = PersistTestHelper.createBook();
        final BookData book2 = PersistTestHelper.createBook();

        final CategoryBookMapper catBookMapper = BookwormHelper.get(CategoryBookMapper.class);

        final CategoryBookData cat1book1 = new CategoryBookData(category1.getId(), book1.getId(), 0);
        final CategoryBookData cat2book1 = new CategoryBookData(category2.getId(), book1.getId(), 0);
        final CategoryBookData cat2book2 = new CategoryBookData(category2.getId(), book2.getId(), 1);
        catBookMapper.insert(cat1book1);
        catBookMapper.insert(cat2book1);
        catBookMapper.insert(cat2book2);

        final List<CategoryData> categories = categoryMapper.selectByBook(book1);
        compare(categories, category1, category2);
    }

    @Test
    public void testSelectMaxRootId() {
        // initial should be 0
        Assert.assertEquals(this.categoryMapper.selectMaxRootIndex(), 0F);

        // insert one
        final CategoryData category1 = PersistTestHelper.createCategory(null);
        Assert.assertTrue(category1.getSiblingIndex() > 0);
        Assert.assertEquals(this.categoryMapper.selectMaxRootIndex(), category1.getSiblingIndex());

        // insert another
        final CategoryData category2 = PersistTestHelper.createCategory(null);
        Assert.assertTrue(category2.getSiblingIndex() > 0);
        Assert.assertEquals(this.categoryMapper.selectMaxRootIndex(), category2.getSiblingIndex());
    }

    @Test
    public void testSelectMaxSiblingIndex() {
        final CategoryData parent = PersistTestHelper.createCategory(null);

        // initial should be 0
        Assert.assertEquals(this.categoryMapper.selectMaxSiblingIndex(parent), 0F);

        // insert one
        final CategoryData category1 = PersistTestHelper.createCategory(parent);
        Assert.assertTrue(category1.getSiblingIndex() > 0);
        Assert.assertEquals(this.categoryMapper.selectMaxSiblingIndex(parent), category1.getSiblingIndex());

        // insert another
        final CategoryData category2 = PersistTestHelper.createCategory(parent);
        Assert.assertTrue(category2.getSiblingIndex() > 0);
        Assert.assertEquals(this.categoryMapper.selectMaxSiblingIndex(parent), category2.getSiblingIndex());
    }

    private void compare(final Collection<CategoryData> actual, final CategoryData... expected) {
        Assert.assertEquals(actual.size(), expected.length);
        // create a map to store expected authors
        final Map<Integer, CategoryData> map = new HashMap<>(expected.length);
        for (final CategoryData category : expected) {
            map.put(category.getId(), category);
        }
        for (final CategoryData category : actual) {
            final CategoryData expectedCategory = map.get(category.getId());
            if (expectedCategory != null) compare(category, expectedCategory);
            else Assert.assertTrue(false, "unexpected author");
        }
    }

    private void compare(final CategoryData actual, final CategoryData expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getName(), expected.getName());
        Assert.assertEquals(actual.getDescription(), expected.getDescription());
        Assert.assertEquals(actual.getSiblingIndex(), expected.getSiblingIndex());
        Assert.assertEquals(actual.getParentId(), expected.getParentId());
    }

    private void compare(final CategoryData actual, final Integer id, final String name,
            final Integer parentId, final String description, final float siblingIndex) {
        Assert.assertEquals(actual.getName(), name);
        Assert.assertEquals(actual.getParentId(), parentId);
        Assert.assertEquals(actual.getSiblingIndex(), siblingIndex);
        Assert.assertEquals(actual.getDescription(), description);
        if (id != null) Assert.assertEquals(actual.getId(), id.intValue());
    }
}
