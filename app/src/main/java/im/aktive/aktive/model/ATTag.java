package im.aktive.aktive.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangtran on 18/7/14.
 */
public class ATTag {
    private int id;
    private String name;
    private String question;
    private ATTagType tagType;
    private Date createdAt;
    private Date updatedAt;
    private List<ATTagValue> tagValues = new ArrayList<ATTagValue>();

    private boolean canBeUsedForFirstTime;

    public ATTag(int id)
    {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ATTagType getTagType() {
        return tagType;
    }

    public void setTagType(ATTagType tagType) {
        this.tagType = tagType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTagValues(List<ATTagValue> tagValues) {
        this.tagValues.clear();
        this.tagValues.addAll(tagValues);
    }

    public List<ATTagValue> getTagValues() {
        return tagValues;
    }

    public void addTagValue(ATTagValue tagValue) {
        this.tagValues.add(tagValue);
    }

    public boolean getCanBeUsedForFirstTime() {
        return canBeUsedForFirstTime;
    }

    public void setCanBeUsedForFirstTime(boolean canBeUsedForFirstTime) {
        this.canBeUsedForFirstTime = canBeUsedForFirstTime;
    }
}
