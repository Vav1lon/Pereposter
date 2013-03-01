package com.pereposter.social.tumblr;

import com.pereposter.social.api.Constants;
import com.pereposter.social.api.SocialWebServices;
import com.pereposter.social.api.entity.*;
import com.pereposter.social.tumblr.holder.*;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TumblrServiceImpl implements SocialWebServices {

    @Autowired
    private FindLastPostTumblrResponseHolder findLastPostTumblrResponseHolder;

    @Autowired
    private FindPostByIdTumblrResponseHolder findPostByIdTumblrResponseHolder;

    @Autowired
    private FindPostsByOverCreatedDateTumblrResponseHolder findPostsByOverCreatedDateTumblrResponseHolder;

    @Autowired
    private WriteNewPostTumblrResponseHolder writeNewPostTumblrResponseHolder;

    @Autowired
    private WriteNewPostsTumblrResponseHolder writeNewPostsTumblrResponseHolder;

    private ProducerTemplate producerTemplate;

    public TumblrServiceImpl(CamelContext camelContext) {
        producerTemplate = camelContext.createProducerTemplate();
    }

    @Override
    public String findLastPost(FindPostRequest request) {

        String requestId = UUID.randomUUID().toString();
        findLastPostTumblrResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findLastPost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TUMBLR_POST_FIND_LAST_POST_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostEntity> getLastPost(String requestId) {
        return findLastPostTumblrResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostById(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostByIdTumblrResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostById");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TUMBLR_POST_FIND_BY_ID_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostEntity> getPostById(String requestId) {
        return findPostByIdTumblrResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostsByOverCreateDate(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostsByOverCreatedDateTumblrResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostsByOverCreateDate");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TUMBLR_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostsResponse> getPostsByOverCreateDate(String requestId) {
        return findPostsByOverCreatedDateTumblrResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePost(WritePostRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostTumblrResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TUMBLR_POST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<String> getWritePost(String requestId) {
        return writeNewPostTumblrResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePosts(WritePostsRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostsTumblrResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePosts");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_TUMBLR_POST_LIST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<String> getWritePosts(String requestId) {
        return writeNewPostsTumblrResponseHolder.getResponse(requestId);
    }

    @Override
    public RequestStatus getStatus(String requestId) {
        RequestStatus requestStatus;

        requestStatus = findLastPostTumblrResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostByIdTumblrResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostsByOverCreatedDateTumblrResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostTumblrResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostsTumblrResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        return RequestStatus.NOT_FOUND;
    }

    @Override
    public void cancelRequest(String requestId) {
    }

    private Map<String, Object> createHeaders(String requestId, String operationName) {
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put(Constants.REQUEST_ID, requestId);
        headers.put(Constants.OPERATION_NAME, operationName);
        return headers;
    }

}
