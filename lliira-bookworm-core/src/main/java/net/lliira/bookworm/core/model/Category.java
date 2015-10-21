package net.lliira.bookworm.core.model;

public class Category {

    private final int id;
    private String name;
    private String sortedName;
    private Category parent;
    private int siblingIndex;
    private String description;

    public Category(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the parent
     */
    public Category getParent() {
        return parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(Category parent) {
        this.parent = parent;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public String getSortedName() {
        return this.sortedName;
    }

    public void setSortedName(String sortedName) {
        this.sortedName = sortedName;
    }

    public int getSiblingIndex() {
        return this.siblingIndex;
    }

    public void setSiblingIndex(int siblingIndex) {
        this.siblingIndex = siblingIndex;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
