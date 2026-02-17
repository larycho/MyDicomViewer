package org.mydicomviewer.models;

public class TagAddress {

    // These are called numbers since they are very typically (95% of the time) hexadecimal numbers
    // However, in rare cases they do have values like 60xx hence usage of String
    private final String groupNumber;
    private final String elementNumber;

    public TagAddress(String groupNumber, String elementNumber) {
        validate(groupNumber, elementNumber);
        this.groupNumber = groupNumber;
        this.elementNumber = elementNumber;
    }

    private void validate(String groupNumber, String elementNumber) {
        validateGroup(groupNumber);
        validateElement(elementNumber);
    }

    private void validateGroup(String groupNumber) {
        if (groupNumber == null || groupNumber.length() != 4) {
            throw new IllegalArgumentException("Invalid group number: " + groupNumber);
        }
    }

    private void validateElement(String elementNumber) {
        if (elementNumber == null || elementNumber.length() != 4) {
            throw new IllegalArgumentException("Invalid element number: " + elementNumber);
        }
    }

    public String getGroupNumber() { return groupNumber; }

    public String getElementNumber() { return elementNumber; }

    @Override
    public String toString() {
        return "(" + groupNumber + "," + elementNumber + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TagAddress other)) {
            return false;
        }

        boolean hasSameGroupNumber = groupNumber.equals(other.getGroupNumber());
        boolean hasSameElementNumber = elementNumber.equals(other.getElementNumber());

        return hasSameGroupNumber && hasSameElementNumber;
    }
}
