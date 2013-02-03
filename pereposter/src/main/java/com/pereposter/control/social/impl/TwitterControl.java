package com.pereposter.control.social.impl;

import com.pereposter.control.social.SocialControl;
import com.pereposter.entity.Post;
import com.pereposter.entity.internal.UserSocialAccount;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("twitterControl")
@Transactional(propagation = Propagation.MANDATORY)
public class TwitterControl implements SocialControl {

    @Override
    public Post findLastUserPost(UserSocialAccount socialAccount) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Post getPostById(UserSocialAccount socialAccount, String postId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Post> findNewPostByOverCreateDate(UserSocialAccount socialAccount) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Post writePost(UserSocialAccount socialAccount, Post post) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Post writePosts(UserSocialAccount socialAccount, List<Post> posts) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
