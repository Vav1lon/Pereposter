package com.pereposter.social.vkontakte.entity;

public class FindPostByIdResult {

    private Long id;
    private Long from_id;
    private Long to_id;
    private Long date;
    private String text;
    private Geo geo;
    private CountValue countValue;
    private CountValue likes;
    private CountValue reposts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFrom_id() {
        return from_id;
    }

    public void setFrom_id(Long from_id) {
        this.from_id = from_id;
    }

    public Long getTo_id() {
        return to_id;
    }

    public void setTo_id(Long to_id) {
        this.to_id = to_id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public CountValue getComments() {
        return countValue;
    }

    public void setComments(CountValue countValue) {
        this.countValue = countValue;
    }

    public CountValue getLikes() {
        return likes;
    }

    public void setLikes(CountValue likes) {
        this.likes = likes;
    }

    public CountValue getReposts() {
        return reposts;
    }

    public void setReposts(CountValue reposts) {
        this.reposts = reposts;
    }
}
