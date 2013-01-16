package com.pereposter.social.facebook.entity;

public enum PostType {

    /*
        * info here, ling = http://developers.facebook.com/docs/reference/fql/stream
        *
        * origin data:
        *
        * type int32 The type of this story. Possible values are:
        *
        * 11 - Group created
        * 12 - Event created
        * 46 - Status update
        * 56 - PostFacebook on wall from another user
        * 66 - Note created
        * 80 - Link posted
        * 128 -Video posted
        * 247 - Photos posted
        * 237 - App story
        * 257 - Comment created
        * 272 - App story
        * 285 - Checkin to a place
        * 308 - PostFacebook in Group
     */

    GROUP_CREATED(11),
    EVENT_CREATED(12),
    STATUS_UPDATE(46),
    POST_ON_WALL_FROM_ANOTHER_USER(56),
    NOTE_CREATED(66),
    LINK_POSTED(80),
    VIDEO_POSTED(128),
    PHOTOS_POSTED(247),
    APP_STORY_237(237),
    COMMENT_CREATED(257),
    APP_STORY_272(272),
    CHECKIN_TO_A_PLACE(285),
    Post_IN_GROUP(308);

    private int value;

    PostType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
