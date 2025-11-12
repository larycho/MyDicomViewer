package org.example.mydicomviewer.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TagGroup {

    private ArrayList<Tag> tags = new ArrayList<Tag>();

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public ArrayList<Tag> allTags() {
        return tags;
    }

}
