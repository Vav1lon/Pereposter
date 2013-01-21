package com.pereposter.control.social;

import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.entity.Post;

import java.util.List;

public interface SocialNetworkControl {

    /*
       * find last message user in social network
       * if not found returned null
     */
    Post findLastUserPost(UserSocialAccount socialAccount);

    Post getPostById(UserSocialAccount socialAccount, String postId);

    List<Post> findNewPostByOverCreateDate(UserSocialAccount socialAccount);

    /*
      * write post to social network
     */
    Post writePost(UserSocialAccount socialAccount, Post post);

    Post writePosts(UserSocialAccount socialAccount, List<Post> posts);

}
