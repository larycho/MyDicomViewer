package org.mydicomviewer.models;

public class Tag {

    private final TagAddress address;
    private final String description;
    private final String value;

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

}
