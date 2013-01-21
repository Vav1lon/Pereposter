package com.pereposter.social.api;

import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.SocialAuthService;
import org.joda.time.DateTime;

import java.util.List;

public interface SocialNetworkConnector {

    /*
    *
    * return: new postEntity id
    *
    */

    String writeNewPost(SocialAuthService auth, PostEntity postEntity);

    /*
    *
    * return: last new postEntity id
    *
    */

    String writeNewPosts(SocialAuthService auth, List<PostEntity> postEntities);

    PostEntity findPostById(SocialAuthService auth, String postId);

    List<PostEntity> findPostsByOverCreatedDate(SocialAuthService auth, DateTime createdDate);

    PostEntity findLastPost(SocialAuthService auth);

}
