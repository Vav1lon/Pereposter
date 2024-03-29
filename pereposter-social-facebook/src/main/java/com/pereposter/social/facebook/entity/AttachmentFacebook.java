package com.pereposter.social.facebook.entity;

import java.util.List;

public class AttachmentFacebook {

    private List<Media> media;
    private String name;
    private String href;
    private String caption;
    private String description;
    private String icon;
    private List<KeyValue> properties;
    private String fb_object_type;
    private String fb_object_id;

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<KeyValue> getProperties() {
        return properties;
    }

    public void setProperties(List<KeyValue> properties) {
        this.properties = properties;
    }

    public String getFb_object_type() {
        return fb_object_type;
    }

    public void setFb_object_type(String fb_object_type) {
        this.fb_object_type = fb_object_type;
    }

    public String getFb_object_id() {
        return fb_object_id;
    }

    public void setFb_object_id(String fb_object_id) {
        this.fb_object_id = fb_object_id;
    }
}
