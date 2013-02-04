package com.pereposter.social.twitter;

import com.pereposter.social.api.Constants;
import com.pereposter.social.api.SocialWebServices;
import com.pereposter.social.api.entity.*;
import com.pereposter.social.twitter.holder.*;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TwitterServiceImpl implements SocialWebServices {

    @Autowired
    private FindLastPostTwitterResponseHolder findLastPostTwitterResponseHolder;

    @Autowired
    private FindPostByIdTwitterResponseHolder findPostByIdTwitterResponseHolder;

    @Autowired
    private FindPostsByOverCreatedDateTwitterResponseHolder findPostsByOverCreatedDateTwitterResponseHolder;

    @Autowired
    private WriteNewPostTwitterResponseHolder writeNewPostTwitterResponseHolder;

    @Autowired
    private WriteNewPostsTwitterResponseHolder writeNewPostsTwitterResponseHolder;

    private ProducerTemplate producerTemplate;

    public TwitterServiceImpl(CamelContext camelContext) {
        producerTemplate = camelContext.createProducerTemplate();
    }

    @Override
    public String findLastPost(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findLastPostTwitterResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findLastPost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TWITTER_POST_FIND_LAST_POST_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostEntity> getLastPost(String requestId) {
        return findLastPostTwitterResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostById(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostByIdTwitterResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostById");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TWITTER_POST_FIND_BY_ID_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostEntity> getPostById(String requestId) {
        return findPostByIdTwitterResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostsByOverCreateDate(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostsByOverCreatedDateTwitterResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostsByOverCreateDate");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TWITTER_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostsResponse> getPostsByOverCreateDate(String requestId) {
        return findPostsByOverCreatedDateTwitterResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePost(WritePostRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostTwitterResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TWITTER_POST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<String> getWritePost(String requestId) {
        return writeNewPostTwitterResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePosts(WritePostsRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostsTwitterResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePosts");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TWITTER_POST_LIST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<String> getWritePosts(String requestId) {
        return writeNewPostsTwitterResponseHolder.getResponse(requestId);
    }

    @Override
    public RequestStatus getStatus(String requestId) {
        RequestStatus requestStatus;

        requestStatus = findLastPostTwitterResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostByIdTwitterResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostsByOverCreatedDateTwitterResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostTwitterResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostsTwitterResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        return RequestStatus.NOT_FOUND;
    }

    @Override
    public void cancelRequest(String requestId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private Map<String, Object> createHeaders(String requestId, String operationName) {
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put(Constants.REQUEST_ID, requestId);
        headers.put(Constants.OPERATION_NAME, operationName);
        return headers;
    }

}
