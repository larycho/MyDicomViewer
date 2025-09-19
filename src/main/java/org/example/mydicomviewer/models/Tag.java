package org.example.mydicomviewer.models;

public class Tag {

    private String address;
    private String description;
    private String value;

    public Tag(String address, String description, String value) {
        this.address = address;
        this.description = description;
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
