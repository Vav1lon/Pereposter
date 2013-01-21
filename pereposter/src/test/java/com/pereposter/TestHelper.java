package com.pereposter;

import com.pereposter.entity.Post;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TestHelper {

    private Random random = new Random();

    private boolean vkontakteSource = false;
    private boolean facebookSource = false;

    private int countWriteRowToFacebook = 0;
    private int countWriteRowToVkontakte = 0;

    //Откуда брать посты
    private List<Post> sourcePost;

    //Куда писать посты
    private List<Post> targetPost;

    @PostConstruct
    public void setUp() {
        sourcePost = new ArrayList<Post>();
        targetPost = new ArrayList<Post>();
    }

    public List<Post> getSourcePost() {
        return sourcePost;
    }

    public void setSourcePost(List<Post> sourcePost) {
        this.sourcePost = sourcePost;
    }

    public List<Post> getTargetPost() {
        return targetPost;
    }

    public void setTargetPost(List<Post> targetPost) {
        this.targetPost = targetPost;
    }

    public Post createPost(String message) {
        Post post = new Post();
        post.setId(Integer.toString(random.nextInt()));
        post.setCreatedDate(new DateTime().minusHours(random.nextInt(10)));
        post.setUpdatedDate(new DateTime());
        post.setMessage(message);
        post.setOwnerId(Integer.toString(random.nextInt()));
        return post;
    }

    public Post createPost(String message, DateTime createDate) {
        Post post = new Post();
        post.setId(Integer.toString(random.nextInt()));
        post.setCreatedDate(createDate);
        post.setUpdatedDate(createDate.plusHours(2));
        post.setMessage(message);
        post.setOwnerId(Integer.toString(random.nextInt()));
        return post;
    }

    public boolean isVkontakteSource() {
        return vkontakteSource;
    }

    public void setVkontakteSource(boolean vkontakteSource) {
        this.vkontakteSource = vkontakteSource;
    }

    public boolean isFacebookSource() {
        return facebookSource;
    }

    public void setFacebookSource(boolean facebookSource) {
        this.facebookSource = facebookSource;
    }

    public int getCountWriteRowToFacebook() {
        return countWriteRowToFacebook;
    }

    public void setCountWriteRowToFacebook(int countWriteRowToFacebook) {
        this.countWriteRowToFacebook = countWriteRowToFacebook;
    }

    public int getCountWriteRowToVkontakte() {
        return countWriteRowToVkontakte;
    }

    public void setCountWriteRowToVkontakte(int countWriteRowToVkontakte) {
        this.countWriteRowToVkontakte = countWriteRowToVkontakte;
    }
}
