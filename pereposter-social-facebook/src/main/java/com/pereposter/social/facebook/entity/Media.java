package com.pereposter.social.facebook.entity;

public class Media {

    private String href;
    private String alt;
    private String type;
    private String src;
    private AttachmentPhoto photo;
    private AttachmentVideo video;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public AttachmentPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(AttachmentPhoto photo) {
        this.photo = photo;
    }

    public AttachmentVideo getVideo() {
        return video;
    }

    public void setVideo(AttachmentVideo video) {
        this.video = video;
    }
}
