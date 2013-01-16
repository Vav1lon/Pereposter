package com.pereposter.service.socialnetwork;

import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.social.entity.Post;
import com.pereposter.social.entity.PostKeyInfo;

import java.util.List;

public interface SocialNetworkService {

    /*
       * find last message user in social network
       * if not found returned null
     */
    PostKeyInfo findLastUserPost(UserSocialAccount socialAccount);

    Post getPostById(UserSocialAccount socialAccount, String postId);

    List<Post> findNewPostByOverCreateDate(UserSocialAccount socialAccount);

    /*
      * write post to social network
     */
    String writePost(UserSocialAccount socialAccount, Post post);

    String writePosts(UserSocialAccount socialAccount, List<Post> posts);

}
