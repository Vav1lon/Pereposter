package com.pereposter.control.social.impl;

import com.pereposter.control.social.SocialControl;
import com.pereposter.entity.Post;
import com.pereposter.entity.internal.SocialUserAccount;
import com.pereposter.social.api.SocialWebServices;
import com.pereposter.social.api.entity.*;
import com.pereposter.utils.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("vkontakteControl")
@Transactional(propagation = Propagation.MANDATORY)
public class VkontakteControl implements SocialControl {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkontakteControl.class);

    @Autowired
    private SocialWebServices vkontakteSocialService;

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    private WebServiceTransform transformer;

    @Override
    public Post findLastUserPost(SocialUserAccount socialAccount) {
        SocialAuthEntity socialAuth = serviceHelper.transformSocialAuthService(socialAccount);

        String requestId = vkontakteSocialService.findLastPost(new FindPostRequest(socialAuth));

        whileCheckStatusRequest(requestId);

        ResponseObject<PostEntity> response = vkontakteSocialService.getLastPost(requestId);

        Post result = null;
        if (response.getStatus() != ResponseStatus.ERROR && response.getValue() != null) {
            result = transformer.toPost(response.getValue());
        }
        return result;
    }

    @Override
    public Post getPostById(SocialUserAccount socialAccount, String postId) {
        SocialAuthEntity socialAuth = serviceHelper.transformSocialAuthService(socialAccount);

        String requestId = vkontakteSocialService.findPostById(new FindPostRequest(socialAuth, postId));

        whileCheckStatusRequest(requestId);

        ResponseObject<PostEntity> response = vkontakteSocialService.getPostById(requestId);

        Post result = null;
        if (response.getStatus() != ResponseStatus.ERROR && response.getValue() != null) {
            result = transformer.toPost(response.getValue());
        }

        return result;

    }

    @Override
    public List<Post> findNewPostByOverCreateDate(SocialUserAccount socialAccount) {
        SocialAuthEntity socialAuth = serviceHelper.transformSocialAuthService(socialAccount);

        String requestId = vkontakteSocialService.findPostsByOverCreateDate(new FindPostRequest(socialAuth, socialAccount.getCreateDateLastPost()));

        whileCheckStatusRequest(requestId);

        ResponseObject<PostsResponse> response = vkontakteSocialService.getPostsByOverCreateDate(requestId);

        List<Post> result = null;

        if (response.getValue().getPostEntityList() != null) {

            List<PostEntity> postsResponses = response.getValue().getPostEntityList();

            if (response.getStatus() != ResponseStatus.ERROR && !postsResponses.isEmpty()) {
                result = transformer.toPosts(postsResponses);
            }
        }

        return result;
    }

    @Override
    public Post writePost(SocialUserAccount socialAccount, Post post) {
        SocialAuthEntity socialAuth = serviceHelper.transformSocialAuthService(socialAccount);

        String requestId = vkontakteSocialService.writePost(new WritePostRequest(socialAuth, transformer.toPostEntity(post)));

        whileCheckStatusRequest(requestId);

        ResponseObject<String> responseWritePost = vkontakteSocialService.getWritePost(requestId);

        Post result = null;

        if (responseWritePost.getStatus() != ResponseStatus.ERROR) {

            socialAccount.setLastPostId(responseWritePost.getValue());

            result = requestFindPostById(responseWritePost.getValue(), socialAuth);
        }

        return result != null ? result : null;

    }

    @Override
    public Post writePosts(SocialUserAccount socialAccount, List<Post> posts) {
        SocialAuthEntity socialAuth = serviceHelper.transformSocialAuthService(socialAccount);

        String requestId = vkontakteSocialService.writePosts(new WritePostsRequest(socialAuth, transformer.toPostEntities(posts)));

        whileCheckStatusRequest(requestId);

        ResponseObject<String> responseWritePosts = vkontakteSocialService.getWritePosts(requestId);

        Post result = null;

        if (responseWritePosts.getStatus() != ResponseStatus.ERROR) {
            socialAccount.setLastPostId(responseWritePosts.getValue());
            result = requestFindPostById(responseWritePosts.getValue(), socialAuth);
        }

        return result != null ? result : null;
    }

    private void whileCheckStatusRequest(String requestId) {
        while (vkontakteSocialService.getStatus(requestId) == RequestStatus.PENDING) {
            try {
                Thread.sleep(3600);
            } catch (InterruptedException e) {
                LOGGER.error("", e);
            }
        }
    }

    private Post requestFindPostById(String postId, SocialAuthEntity socialAuth) {
        String requestId = vkontakteSocialService.findPostById(new FindPostRequest(socialAuth, postId));

        whileCheckStatusRequest(requestId);

        ResponseObject<PostEntity> response = vkontakteSocialService.getPostById(requestId);

        Post result = null;
        if (response.getStatus() != ResponseStatus.ERROR && response.getValue() != null) {
            result = transformer.toPost(response.getValue());
        }
        return result;
    }

}
