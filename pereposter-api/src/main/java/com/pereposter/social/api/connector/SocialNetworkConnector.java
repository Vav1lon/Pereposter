package com.pereposter.social.api.connector;

import com.pereposter.social.entity.Post;
import com.pereposter.social.entity.SocialAuth;
import org.joda.time.DateTime;


import java.util.List;

public interface SocialNetworkConnector {

    /*
    *
    * return: new post id
    *
    */

    String writeNewPost(SocialAuth auth, Post post);

    /*
    *
    * return: last new post id
    *
    */

    String writeNewPosts(SocialAuth auth, List<Post> posts);

    Post findPostById(SocialAuth auth, String postId);

    List<Post> findPostsByOverCreatedDate(SocialAuth auth, DateTime createdDate);

    Post findLastPost(SocialAuth auth);

}
