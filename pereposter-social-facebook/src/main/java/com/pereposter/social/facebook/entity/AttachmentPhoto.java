package com.pereposter.social.facebook.entity;

import java.util.List;

public class AttachmentPhoto {

    private String aid;
    private String pid;
    private Long fbid;
    private Long owner;
    private Integer index;
    private Integer width;
    private Integer height;
    private List<PhotoImage> images;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Long getFbid() {
        return fbid;
    }

    public void setFbid(Long fbid) {
        this.fbid = fbid;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<PhotoImage> getImages() {
        return images;
    }

    public void setImages(List<PhotoImage> images) {
        this.images = images;
    }

}
