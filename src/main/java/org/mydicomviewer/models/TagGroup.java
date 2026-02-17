package org.mydicomviewer.models;

import java.util.ArrayList;

public class TagGroup {

    private final ArrayList<Tag> tags = new ArrayList<>();

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public ArrayList<Tag> allTags() {
        return tags;
    }

    public boolean containsTag(TagAddress soughtAddress) {
        for (Tag tag : tags) {
            if (tag.getAddress().equals(soughtAddress)) {
                return true;
            }
        }
        return false;
    }

    public Tag getTag(TagAddress soughtAddress) {
        for (Tag tag : tags) {
            if (tag.getAddress().equals(soughtAddress)) {
                return tag;
            }
        }
        return null;
    }
}
