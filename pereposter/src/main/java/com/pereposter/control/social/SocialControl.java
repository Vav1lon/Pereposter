package com.pereposter.control.social;

import com.pereposter.entity.Post;
import com.pereposter.entity.internal.SocialUserAccount;

import java.util.List;

public interface SocialControl {

    /*
       * find last message user in social network
       * if not found returned null
     */
    Post findLastUserPost(SocialUserAccount socialAccount);

    Post getPostById(SocialUserAccount socialAccount, String postId);

    List<Post> findNewPostByOverCreateDate(SocialUserAccount socialAccount);

    /*
      * write post to social network
     */
    Post writePost(SocialUserAccount socialAccount, Post post);

    Post writePosts(SocialUserAccount socialAccount, List<Post> posts);

}
