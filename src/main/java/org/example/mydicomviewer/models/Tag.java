package org.example.mydicomviewer.models;

public class Tag {

    private String number;
    private String description;
    private String value;

    public Tag(String number, String description, String value) {
        this.number = number;
        this.description = description;
        this.value = value;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
