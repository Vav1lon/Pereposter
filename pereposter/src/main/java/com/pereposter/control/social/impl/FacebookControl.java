package com.pereposter.control.social.impl;

import com.pereposter.control.social.SocialNetworkControl;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.entity.Post;
import com.pereposter.entity.SocialAuth;
import com.pereposter.utils.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("facebookService")
@Transactional(propagation = Propagation.MANDATORY)
public class FacebookControl implements SocialNetworkControl {

    @Autowired
    private SocialNetworkConnector facebookConnector;

    @Autowired
    private ServiceHelper serviceHelper;

    @Override
    public Post findLastUserPost(UserSocialAccount socialAccount) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);
        return facebookConnector.findLastPost(socialAuth);
    }

    @Override
    public Post getPostById(UserSocialAccount socialAccount, String postId) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);

        return facebookConnector.findPostById(socialAuth, postId);
    }

    @Override
    public List<Post> findNewPostByOverCreateDate(UserSocialAccount socialAccount) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);

        return facebookConnector.findPostsByOverCreatedDate(socialAuth, socialAccount.getCreateDateLastPost());
    }

    @Override
    public Post writePost(UserSocialAccount socialAccount, Post post) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);
        return facebookConnector.findPostById(socialAuth, facebookConnector.writeNewPost(socialAuth, post));

    }

    @Override
    public Post writePosts(UserSocialAccount socialAccount, List<Post> posts) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);
        return facebookConnector.findPostById(socialAuth, facebookConnector.writeNewPosts(socialAuth, posts));
    }
}
