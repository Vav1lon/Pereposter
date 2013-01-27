package com.pereposter.social.api;

import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.PostsResponse;
import com.pereposter.social.api.entity.ResponseObject;
import com.pereposter.social.api.entity.SocialAuthEntity;
import org.joda.time.DateTime;

import java.util.List;

public interface SocialNetworkConnector {

    /*
    *
    * return: new postEntity id
    *
    */

    ResponseObject<String> writeNewPost(SocialAuthEntity auth, PostEntity postEntity);

    /*
    *
    * return: last new postEntity id
    *
    */

    ResponseObject<String> writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities);

    ResponseObject<PostEntity> findPostById(SocialAuthEntity auth, String postId);

    ResponseObject<PostsResponse> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate);

    ResponseObject<PostEntity> findLastPost(SocialAuthEntity auth);

}
