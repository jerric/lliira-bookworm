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
        final List<CategoryData> categories = (parent == null) ? this.categoryMapper.selectRoots()
                : this.categoryMapper.selectByParent(new CategoryData(parent));
        return new ArrayList<>(categories);
    }

    public List<Category> getRoots() {
        return get((Category) null);
    }

    public Category get(int id) {
        return this.categoryMapper.select(id);
    }

    public Set<Category> get(Book book) {
        final List<CategoryData> categories = this.categoryMapper.selectByBook(new BookData(book));
        return new HashSet<>(categories);
    }

    public Category create(final String name, final Category parent, final String description)
            throws CategoryException {
        final CategoryData category = new CategoryData();
        category.setName(name);
        category.setParent(parent);
        category.setDescription(description);

        final float siblingIndex;
        if (parent == null) siblingIndex = this.categoryMapper.selectMaxRootIndex() + 1;
        else siblingIndex = this.categoryMapper.selectMaxSiblingIndex(new CategoryData(parent)) + 1;
        category.setSiblingIndex(siblingIndex);

        if (1 == this.categoryMapper.insert(category)) return category;
        else throw new CategoryException("Creating category failed.");
    }

    public void update(final Category category) {

    }

    private void validate(final CategoryData category) {

    }

    public void delete(final Category category) {

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
