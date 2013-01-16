package com.pereposter.stub;

import com.pereposter.social.api.connector.SocialNetworkConnector;
import com.pereposter.social.entity.Post;
import com.pereposter.social.entity.SocialAuth;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("facebookConnector")
public class FacebookConnectorStub implements SocialNetworkConnector {

    Random random;

    @PostConstruct
    public void setUp() {
        random = new Random();
    }

    @Override
    public String writeNewPost(SocialAuth auth, Post post) {
        return Long.toString(random.nextLong());
    }

    @Override
    public String writeNewPosts(SocialAuth auth, List<Post> posts) {
        return Long.toString(random.nextLong());
    }

    @Override
    public Post findPostById(SocialAuth auth, String postId) {
        return createPost("Это одно тестовое сообщение");
    }

    @Override
    public List<Post> findPostsByOverCreatedDate(SocialAuth auth, DateTime createdDate) {

        List<Post> result = new ArrayList<Post>();

        result.add(createPost("Это первое тестовове сообщение в списке из 4-х"));
        result.add(createPost("Это второе тестовове сообщение в списке из 4-х"));
        result.add(createPost("Это третье тестовове сообщение в списке из 4-х"));
        result.add(createPost("Это четвертое тестовове сообщение в списке из 4-х"));

        return result;
    }

    @Override
    public Post findLastPost(SocialAuth auth) {
        return createPost("Это одно тестовое сообщение");
    }

    private Post createPost(String message) {
        Post post = new Post();
        post.setId(Integer.toString(random.nextInt()));
        post.setCreatedDate(new DateTime().minusHours(random.nextInt(10)));
        post.setUpdatedDate(new DateTime());
        post.setMessage(message);
        return post;
    }

}
