package org.lliira.bookworm.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lliira.bookworm.core.AbstractTest;
import org.lliira.bookworm.core.TestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookException;
import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.CategoryException;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.model.Category;
import net.lliira.bookworm.core.service.CategoryService;

public class CategoryServiceTest extends AbstractTest {

    private static final float DELTA = 0.000001F;

    private CategoryService categoryService;

    @BeforeMethod
    public void prepareService() {
        this.categoryService = BookwormHelper.get(CategoryService.class);
    }

    @Test
    public void testCreate() throws CategoryException {
        final String name = "name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final float sibling = random.nextInt(10) + 1F;
        final Category parent = this.categoryService.create(name, null, description, sibling);
        compare(parent, name, description, sibling, null);

        final Category parent1 = this.categoryService.get(parent.getId());
        compare(parent1, parent);

        final Category child1 = TestHelper.createCategory(parent);
        compare(child1.getParent(), parent);

        final Category child2 = TestHelper.createCategory(parent);
        final List<Category> children = this.categoryService.get(parent);
        Assert.assertEquals(children.size(), 2);
        compare(children.get(0), child1);
        compare(children.get(1), child2);
    }

    @Test
    public void testUpdate() throws CategoryException {
        final Category category = TestHelper.createCategory(null);
        final String name = "name-" + random.nextInt();
        final String description = "desc-" + random.nextInt();
        final float siblingIndex = random.nextInt(10) + 1.1F;
        final Category parent = TestHelper.createCategory(null);

        category.setName(name);
        category.setDescription(description);
        category.setSiblingIndex(siblingIndex);
        category.setParent(parent);

        this.categoryService.update(category);
        compare(category, name, description, siblingIndex, parent);

        final Category category1 = this.categoryService.get(category.getId());
        compare(category1, category);

        // also make sure the relationship is set up
        final List<Category> children1 = this.categoryService.get(parent);
        Assert.assertEquals(children1.size(), 1);
        compare(children1.get(0), category);

        // then unset parent
        category.setParent(null);
        this.categoryService.update(category);
        final Category category2 = this.categoryService.get(category.getId());
        Assert.assertNull(category2.getParent());

        final List<Category> roots = this.categoryService.getRoots();
        Assert.assertEquals(roots.size(), 2);
        if (roots.get(0).getId() == parent.getId()) {
            compare(roots.get(0), parent);
            compare(roots.get(1), category);
        } else {
            compare(roots.get(1), parent);
            compare(roots.get(0), category);
        }
    }

    @Test
    public void testUpdateWithPartialNormalize() throws CategoryException {
        final Category[] categories = TestHelper.createCategories(null, 2);

        // now create another category with a gap
        final Category cat3 = this.categoryService.create("name-" + random.nextInt(), null,
                "desc-" + random.nextInt(), categories[1].getSiblingIndex() + 2);

        // now create another category after cat3
        final Category cat4 = TestHelper.createCategory(null);
        Assert.assertTrue(cat4.getSiblingIndex() > cat3.getSiblingIndex());

        // update cat4 and move it to the place where cat2 is
        cat4.setSiblingIndex(categories[1].getSiblingIndex());
        this.categoryService.update(cat4);

        final List<Category> cats = this.categoryService.getRoots();
        compare(cats.get(0), this.categoryService.get(categories[0].getId()));
        compare(cats.get(1), cat4);
        compare(cats.get(2), this.categoryService.get(categories[1].getId()));
        compare(cats.get(3), this.categoryService.get(cat3.getId()));
    }

    @Test
    public void testUpdateWithFullNormalize() throws CategoryException {
        final Category[] list = TestHelper.createCategories(null, 2);
        // now create another category with sibling index to 1, which should push the previous two's indexes
        // up by 1.
        final Category category = TestHelper.createCategory(null);
        category.setSiblingIndex(1);
        this.categoryService.update(category);
        Assert.assertEquals(category.getSiblingIndex(), 1F);

        final List<Category> categories = this.categoryService.getRoots();
        Assert.assertEquals(categories.size(), 3);
        compare(categories.get(0), category);

        final Category cat2 = categories.get(1);
        Assert.assertEquals(cat2.getId(), list[0].getId());
        Assert.assertEquals(cat2.getSiblingIndex(), 2F, DELTA);

        final Category cat3 = categories.get(2);
        Assert.assertEquals(cat3.getId(), list[1].getId());
        Assert.assertEquals(cat3.getSiblingIndex(), 3F, DELTA);
    }

    @Test
    public void testDeleteSingle() throws CategoryException {
        final Category root = TestHelper.createCategory(null);
        final Category child1 = TestHelper.createCategory(root);
        final Category child2 = TestHelper.createCategory(root);

        // now delete child2
        this.categoryService.delete(child2);
        final List<Category> children = this.categoryService.get(root);
        Assert.assertEquals(children.size(), 1);
        compare(children.get(0), child1);

        // now delete child1
        this.categoryService.delete(child1);
        Assert.assertTrue(this.categoryService.get(root).isEmpty());

        // now delete parent
        this.categoryService.delete(root);
        Assert.assertTrue(this.categoryService.getRoots().isEmpty());
    }

    @Test
    public void testDeleteWithBooks() throws BookException, CategoryException {
        final Category category = TestHelper.createCategory(null);
        final Book book = TestHelper.createBook();
        final float siblingIndex = random.nextInt(10) + 1;
        final Map<Category, Float> categories = new HashMap<>();
        categories.put(category, siblingIndex);
        this.categoryService.setCategoriesToBook(book, categories);

        // now delete the category
        this.categoryService.delete(category);
        Assert.assertTrue(this.categoryService.get(book).isEmpty());
    }

    @Test
    public void testDeleteWithChildren() throws CategoryException {
        final Category parent = TestHelper.createCategory(null);
        Category child = TestHelper.createCategory(parent);

        // delete parent
        this.categoryService.delete(parent);
        
        // reload child so that the parent of the child is reset
        child = this.categoryService.get(child.getId());

        final List<Category> roots = this.categoryService.getRoots();
        Assert.assertEquals(roots.size(), 1);
        compare(roots.get(0), child);
    }

    private void compare(final Category actual, final String name, final String description,
            final float siblingIndex, final Category parent) {
        Assert.assertEquals(actual.getName(), name);
        Assert.assertEquals(actual.getDescription(), description);
        Assert.assertEquals(actual.getSiblingIndex(), siblingIndex, DELTA);
        compare(actual.getParent(), parent);
    }

    private void compare(final Category actual, final Category expected) {
        if (expected == null) {
            Assert.assertNull(actual);
        } else {
            Assert.assertEquals(actual.getId(), expected.getId());
            Assert.assertEquals(actual.getName(), expected.getName());
            Assert.assertEquals(actual.getDescription(), expected.getDescription());
            Assert.assertEquals(actual.getSiblingIndex(), expected.getSiblingIndex(), DELTA);
            compare(actual.getParent(), expected.getParent());
        }
    }
}
