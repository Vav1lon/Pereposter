package com.pereposter.social.facebook.entity;

public class PostFacebook {

//    private List<AppData> app_data;
    private String post_id;
    private Long actor_id;
    private String target_id;
    private String message;
    private Integer type;
    private boolean is_hidden;
    private long created_time;
    private long updated_time;
    private AttachmentFacebook attachment;

//    public List<AppData> getApp_data() {
//        return app_data;
//    }
//
//    public void setApp_data(List<AppData> app_data) {
//        this.app_data = app_data;
//    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Long getActor_id() {
        return actor_id;
    }

    public void setActor_id(Long actor_id) {
        this.actor_id = actor_id;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isIs_hidden() {
        return is_hidden;
    }

    public void setIs_hidden(boolean is_hidden) {
        this.is_hidden = is_hidden;
    }

    public long getCreated_time() {
        return created_time;
    }

    public void setCreated_time(long created_time) {
        this.created_time = created_time;
    }

    public long getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(long updated_time) {
        this.updated_time = updated_time;
    }

    public AttachmentFacebook getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentFacebook attachment) {
        this.attachment = attachment;
    }
}
