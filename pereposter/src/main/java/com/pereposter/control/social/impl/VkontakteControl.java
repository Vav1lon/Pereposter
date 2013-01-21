package com.pereposter.control.social.impl;

import com.pereposter.control.social.SocialNetworkControl;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.social.api.vkontakte.SocialVkontakteServices;
import com.pereposter.entity.Post;
import com.pereposter.entity.SocialAuth;
import com.pereposter.utils.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("vkontakteService")
@Transactional(propagation = Propagation.MANDATORY)
public class VkontakteControl implements SocialNetworkControl {

    @Autowired
    private SocialVkontakteServices vkontakteServices;

    @Autowired
    private ServiceHelper serviceHelper;

    @Override
    public Post findLastUserPost(UserSocialAccount socialAccount) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);
        return vkontakteServices.findLastPost(socialAuth);
    }

    @Override
    public Post getPostById(UserSocialAccount socialAccount, String postId) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);

        return vkontakteServices.findPostById(socialAuth, postId);
    }

    @Override
    public List<Post> findNewPostByOverCreateDate(UserSocialAccount socialAccount) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);

        return vkontakteServices.findPostsByOverCreatedDate(socialAuth, socialAccount.getCreateDateLastPost());
    }

    @Override
    public Post writePost(UserSocialAccount socialAccount, Post post) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);
        return vkontakteServices.findPostById(socialAuth, vkontakteServices.writeNewPost(socialAuth, post));

    }

    @Override
    public Post writePosts(UserSocialAccount socialAccount, List<Post> posts) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);
        return vkontakteServices.findPostById(socialAuth, vkontakteServices.writeNewPosts(socialAuth, posts));
    }
}
