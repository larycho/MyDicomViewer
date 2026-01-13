package org.example.mydicomviewer.models;

public class Tag {

    private TagAddress address;
    private String description;
    private String value;

    public Tag(TagAddress address, String description, String value) {
        this.address = address;
        this.description = description;
        this.value = value;
    }

    public TagAddress getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
