package net.lliira.bookworm.core.service;

import static net.lliira.bookworm.core.BookwormHelper.ds;
import static net.lliira.bookworm.core.BookwormHelper.getIncrement;
import static net.lliira.bookworm.core.BookwormHelper.ns;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import net.lliira.bookworm.core.BookException;
import net.lliira.bookworm.core.CategoryException;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.model.Category;
import net.lliira.bookworm.core.persist.mapper.CategoryBookMapper;
import net.lliira.bookworm.core.persist.mapper.CategoryMapper;
import net.lliira.bookworm.core.persist.model.BookData;
import net.lliira.bookworm.core.persist.model.CategoryData;

public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryBookMapper categoryBookMapper;

    @Autowired
    private BookService bookService;

    public List<Category> get(final Category parent) {
        final List<CategoryData> categories;
        if (parent == null) {
            categories = this.categoryMapper.selectRoots();
        } else {
            final CategoryData parentData = (parent instanceof CategoryData) ? (CategoryData) parent
                    : new CategoryData(parent);
            categories = this.categoryMapper.selectByParent(parentData);
        }
        return new ArrayList<>(categories);
    }

    public List<Category> getRoots() {
        return get((Category) null);
    }

    public Category get(int id) {
        return this.categoryMapper.select(id);
    }

    public Set<Category> get(Book book) {
        final BookData bookData = (book instanceof BookData) ? (BookData) book : new BookData(book);
        final List<CategoryData> categories = this.categoryMapper.selectByBook(bookData);
        return new HashSet<>(categories);
    }

    public Category create(final String name, final Category parent, final String description,
            final float siblingIndex) throws CategoryException {
        final CategoryData category = new CategoryData();
        category.setName(name);
        category.setParent(parent);
        category.setDescription(description);
        category.setSiblingIndex(siblingIndex);

        validate(category);

        if (1 == this.categoryMapper.insert(category)) return category;
        else throw new CategoryException("Creating category failed.");
    }

    public void update(final Category category) throws CategoryException {
        // always create data, just in case the validation fails and we don't want to change category;
        final CategoryData categoryData = new CategoryData(category);
        validate(categoryData);

        if (1 != this.categoryMapper.update(categoryData))
            throw new CategoryException("Updating category failed.");

        // need to copy back values, since they might be normalized during validation.
        category.setName(categoryData.getName());
        category.setDescription(categoryData.getDescription());
        category.setSiblingIndex(categoryData.getSiblingIndex());
    }

    private void validate(final CategoryData category) throws CategoryException {
        // validate category name
        final String name = category.getName();
        if (name == null || name.trim().isEmpty())
            throw new CategoryException("Category name cannot be null or empty");
        category.setName(name.trim());

        if (category.getDescription() != null) category.setDescription(category.getDescription().trim());

        // make sure name is unique
        final List<Category> siblings = get(category.getParent());
        final int nsibling = ns(category.getSiblingIndex());

        // check if there is a conflict
        int start = -1;
        for (int i = 0; i < siblings.size(); i++) {
            final Category sibling = siblings.get(i);
            final int ns = ns(sibling.getSiblingIndex());
            if (ns == nsibling && sibling.getId() != sibling.getId()) start = i;
            if (ns >= nsibling) break;
        }

        if (start != -1) { // there is a conflict
            final int increment = getIncrement(nsibling);
            int prevIndex = nsibling;
            final Deque<Category> updates = new ArrayDeque<>();
            for (int i = start; i < siblings.size(); i++) {
                final Category sibling = siblings.get(i);
                final int ns = ns(sibling.getSiblingIndex());
                if (ns > prevIndex) break; // the following index is big enough to avoid conflict, break;
                prevIndex = ns + increment;
                sibling.setSiblingIndex(ds(prevIndex));
                updates.offerLast(sibling);
            }

            while (!updates.isEmpty()) {
                final Category sibling = updates.pollLast();
                if (1 != this.categoryMapper.update((CategoryData) sibling))
                    throw new CategoryException("Update sibling failed. " + sibling);
            }
        }
    }

    public void delete(final Category category) throws CategoryException {
        final CategoryData categoryData = (category instanceof CategoryData) ? (CategoryData) category
                : new CategoryData(category);

        // delete all references to the category first
        this.categoryBookMapper.deleteByCategory(categoryData);

        // reset the children to root
        this.categoryMapper.updateParentToNull(categoryData);

        if (1 != this.categoryMapper.delete(categoryData))
            throw new CategoryException("Deleting category failed.");
    }

    public void setCategoriesToBook(final Book book, final Map<Category, Float> categories)
            throws CategoryException {
        try {
            this.bookService.setCategories(book, categories);
        } catch (BookException ex) {
            throw new CategoryException(ex);
        }
    }
}
