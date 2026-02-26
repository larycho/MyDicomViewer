package org.mydicomviewer.models;

import java.util.ArrayList;
import java.util.List;

public class TagSeries {
    private final List<TagGroup> tags;

    public TagSeries(List<TagGroup> tags) {
        this.tags = new ArrayList<>(tags);
    }

    public TagSeries(TagGroup tags) {
        this.tags = new ArrayList<>();
        this.tags.add(tags);
    }

    public TagGroup getTags() {
        if (tags.isEmpty()) return new TagGroup();
        return tags.get(0);
    }

    public TagGroup getTags(int index) {
        if (tags.size() == 1) return tags.get(0);
        if (index >= tags.size() || index < 0) return new TagGroup();
        return tags.get(index);
    }

    public boolean containsTag(TagAddress address) {
        return tags.get(0).containsTag(address);
    }

    public boolean containsTag(TagAddress address, int index) {
        if (tags.size() == 1) return containsTag(address);
        if (index >= tags.size() || index < 0) return false;
        return tags.get(index).containsTag(address);
    }

    public Tag getTag(TagAddress address) {
        return tags.get(0).getTag(address);
    }

    public Tag getTag(TagAddress address, int index) {
        if (tags.size() == 1) return getTag(address);
        if (index >= tags.size() || index < 0) return null;
        return tags.get(index).getTag(address);
    }
}
