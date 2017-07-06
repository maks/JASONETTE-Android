package com.jasonette.builder.model;

import android.net.Uri;

import java.util.List;

/**
 *
 */

public class HeadModel {

    private String title;
    private String description;
    private Uri    icon;
    private boolean offline;

    List <StyleModel> styles;
    List <ActionModel> actions;
    List <TemplateModel> templates;
    List <DataModel> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getIcon() {
        return icon;
    }

    public void setIcon(Uri icon) {
        this.icon = icon;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

}
