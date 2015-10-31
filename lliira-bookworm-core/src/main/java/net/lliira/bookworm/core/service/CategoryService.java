package net.lliira.bookworm.core.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import net.lliira.bookworm.core.CategoryException;
import net.lliira.bookworm.core.model.Book;
import net.lliira.bookworm.core.model.Category;
import net.lliira.bookworm.core.persist.mapper.CategoryMapper;
import net.lliira.bookworm.core.persist.model.BookData;
import net.lliira.bookworm.core.persist.model.CategoryData;

public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

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

        final CategoryData parentData = new CategoryData(parent);
        final float siblingIndex;
        if (parent == null) siblingIndex = this.categoryMapper.selectMaxRootIndex() + 1;
        else siblingIndex = this.categoryMapper.selectMaxSiblingIndex(parentData) + 1;
        category.setSiblingIndex(siblingIndex);

        if (1 == this.categoryMapper.insert(category)) return category;
        else throw new CategoryException("Creating category failed.");
    }
}
