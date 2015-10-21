package org.lliira.bookworm.calibre.model;

public class CalibreLibrary {
    private int id;
    private String uuid;
    private String path;
    private int readColumn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getReadColumn() {
        return this.readColumn;
    }

    public void setReadColumn(int readColumn) {
        this.readColumn = readColumn;
    }

}
