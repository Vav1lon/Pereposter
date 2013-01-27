package com.pereposter.social.api;

import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.SocialAuthEntity;
import org.joda.time.DateTime;

import java.util.List;

public interface SocialNetworkConnector {

    /*
    *
    * return: new postEntity id
    *
    */

    String writeNewPost(SocialAuthEntity auth, PostEntity postEntity);

    /*
    *
    * return: last new postEntity id
    *
    */

    String writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities);

    PostEntity findPostById(SocialAuthEntity auth, String postId);

    List<PostEntity> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate);

    PostEntity findLastPost(SocialAuthEntity auth);

}
