package com.software.abs.videotext;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by sanny.nagveker on 01/02/2018.
 */

public class VideoData extends RealmObject {

    @Required
    private String title;
    private String note;
    private long dateTimeMillis;
    private String path;
    public int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDateTimeMillis() {
        return dateTimeMillis;
    }

    public void setDateTimeMillis(long dateTimeMillis) {
        this.dateTimeMillis = dateTimeMillis;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}