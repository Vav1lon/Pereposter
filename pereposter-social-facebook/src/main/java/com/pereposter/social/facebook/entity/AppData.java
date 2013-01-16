package com.pereposter.social.facebook.entity;

import java.util.List;

public class AppData {

    private String attachment_data;
    private String images;
    private List<Long> photo_ids;

    public String getAttachment_data() {
        return attachment_data;
    }

    public void setAttachment_data(String attachment_data) {
        this.attachment_data = attachment_data;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<Long> getPhoto_ids() {
        return photo_ids;
    }

    public void setPhoto_ids(List<Long> photo_ids) {
        this.photo_ids = photo_ids;
    }
}
