package com.pereposter.service.socialnetwork;

import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.social.api.connector.SocialNetworkConnector;
import com.pereposter.social.entity.Post;
import com.pereposter.social.entity.PostKeyInfo;
import com.pereposter.social.entity.SocialAuth;
import com.pereposter.utils.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("vkontakteService")
@Transactional(propagation = Propagation.MANDATORY)
public class VkontakteService implements SocialNetworkService {

    @Autowired
    private SocialNetworkConnector vkontakteConnector;

    @Autowired
    private ServiceHelper serviceHelper;

    @Override
    public PostKeyInfo findLastUserPost(UserSocialAccount socialAccount) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);

        Post post = vkontakteConnector.findLastPost(socialAuth);

        return new PostKeyInfo(post.getId(), post.getCreatedDate());
    }

    @Override
    public Post getPostById(UserSocialAccount socialAccount, String postId) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);

        return vkontakteConnector.findPostById(socialAuth, postId);
    }

    @Override
    public List<Post> findNewPostByOverCreateDate(UserSocialAccount socialAccount) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);

        return vkontakteConnector.findPostsByOverCreatedDate(socialAuth, socialAccount.getCreateDateLastPost());
    }

    @Override
    public String writePost(UserSocialAccount socialAccount, Post post) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);

        return vkontakteConnector.writeNewPost(socialAuth, post);

    }

    @Override
    public String writePosts(UserSocialAccount socialAccount, List<Post> posts) {
        SocialAuth socialAuth = serviceHelper.transformSocialAccount(socialAccount);

        return vkontakteConnector.writeNewPosts(socialAuth, posts);
    }
}
