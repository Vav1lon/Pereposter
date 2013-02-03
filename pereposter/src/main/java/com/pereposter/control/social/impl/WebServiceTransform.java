package com.pereposter.control.social.impl;

import com.google.common.base.Strings;
import com.pereposter.entity.Post;
import com.pereposter.social.api.entity.PostEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebServiceTransform {

    public PostEntity toPostEntity(Post post) {
        return createPostEntity(post);
    }

    public Post toPost(PostEntity postEntity) {
        return createPost(postEntity);
    }

    public List<Post> toPosts(List<PostEntity> postEntities) {

        List<Post> result = new ArrayList<Post>();

        Post post = null;
        for (PostEntity entity : postEntities) {

            post = createPost(entity);
            result.add(post);

        }

        return result;

    }

    private Post createPost(PostEntity entity) {
        Post post = new Post();
        post.setMessage(entity.getMessage());
        post.setId(entity.getId());
        post.setCreatedDate(entity.getCreatedDate());

        if (!Strings.isNullOrEmpty(entity.getOwnerId())) {
            post.setOwnerId(entity.getOwnerId());
        }

        return post;
    }

    public List<PostEntity> toPostEntities(List<Post> posts) {

        List<PostEntity> result = new ArrayList<PostEntity>();

        PostEntity postEntity;
        for (Post post : posts) {
            postEntity = createPostEntity(post);
            result.add(postEntity);
        }

        return result;

    }

    private PostEntity createPostEntity(Post post) {
        PostEntity postEntity = new PostEntity();
        postEntity.setMessage(post.getMessage());
        postEntity.setId(post.getId());
        postEntity.setCreatedDate(post.getCreatedDate());
        return postEntity;
    }
}
