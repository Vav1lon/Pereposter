package com.pereposter.stub;

import com.pereposter.TestHelper;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.entity.Post;
import com.pereposter.entity.SocialAuth;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.PostsResponse;
import com.pereposter.social.api.entity.ResponseObject;
import com.pereposter.social.api.entity.SocialAuthEntity;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("facebookConnector")
public class FacebookConnectorStub implements SocialNetworkConnector {

    @Autowired
    private TestHelper testHelper;

    private List<Post> facebookPosts;

    Random random;

    @PostConstruct
    public void setUp() {
        random = new Random();
        facebookPosts = new ArrayList<Post>();
    }


    public String writeNewPost(SocialAuth auth, Post post) {
        testHelper.setCountWriteRowToFacebook(testHelper.getCountWriteRowToFacebook() + 1);
        return post.getId();
    }


    public String writeNewPosts(SocialAuth auth, List<Post> posts) {
        testHelper.setCountWriteRowToFacebook(testHelper.getCountWriteRowToFacebook() + posts.size());
        facebookPosts.addAll(posts);
        return facebookPosts.get(facebookPosts.size() - 1).getId();
    }


    public Post findPostById(SocialAuth auth, String postId) {

        Post result = null;

        for (Post post : facebookPosts) {
            if (post.getId().equalsIgnoreCase(postId)) {
                result = post;
            }
        }

        return result;
    }


    public List<Post> findPostsByOverCreatedDate(SocialAuth auth, DateTime createdDate) {

        List<Post> result = null;

        if (testHelper.isFacebookSource() && !testHelper.getSourcePost().isEmpty()) {
            result = testHelper.getSourcePost();
        }

        return result;
    }


    public Post findLastPost(SocialAuth auth) {

        Post result = null;

        if (!facebookPosts.isEmpty()) {
            result = facebookPosts.get(facebookPosts.size() - 1);
        }

        return result;
    }

    public List<Post> getFacebookPosts() {
        return facebookPosts;
    }

    public void setFacebookPosts(List<Post> facebookPosts) {
        this.facebookPosts = facebookPosts;
    }

    @Override
    public ResponseObject<String> writeNewPost(SocialAuthEntity auth, PostEntity postEntity) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<String> writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostEntity> findPostById(SocialAuthEntity auth, String postId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostEntity> findLastPost(SocialAuthEntity auth) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
