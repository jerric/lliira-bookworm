package org.lliira.bookworm.core.service;

import java.util.List;

import org.lliira.bookworm.core.AbstractTest;
import org.lliira.bookworm.core.TestHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.lliira.bookworm.core.BookwormHelper;
import net.lliira.bookworm.core.CategoryException;
import net.lliira.bookworm.core.model.Category;
import net.lliira.bookworm.core.service.CategoryService;

public class CategoryServiceTest extends AbstractTest {

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
    public void testUpdateWithPartialNormalize() {

    }

    @Test
    public void testUpdateWithFullNormalize() {
        Assert.assertTrue(false);
    }

    @Test
    public void testDeleteWithBooks() {
        Assert.assertTrue(false);
    }

    @Test
    public void testDeleteWithChildren() {
        Assert.assertTrue(false);
    }

    private void compare(final Category actual, final String name, final String description,
            final float siblingIndex, final Category parent) {
        Assert.assertEquals(actual.getName(), name);
        Assert.assertEquals(actual.getDescription(), description);
        Assert.assertEquals(actual.getSiblingIndex(), siblingIndex);
        compare(actual.getParent(), parent);
    }

    private void compare(final Category actual, final Category expected) {
        if (expected == null) {
            Assert.assertNull(actual);
        } else {
            Assert.assertEquals(actual.getId(), expected.getId());
            Assert.assertEquals(actual.getName(), expected.getName());
            Assert.assertEquals(actual.getDescription(), expected.getDescription());
            Assert.assertEquals(actual.getSiblingIndex(), expected.getSiblingIndex());
            compare(actual.getParent(), expected.getParent());
        }
    }
}
