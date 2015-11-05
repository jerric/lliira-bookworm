package net.lliira.bookworm.core.service;

import java.util.ArrayList;
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

    private void validate(final CategoryData category) {

    }

    private void normalizeSiblingIndexes(CategoryData category) {

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
