package com.pereposter.social.vkontakte;

import com.pereposter.social.api.Constants;
import com.pereposter.social.api.entity.*;
import com.pereposter.social.api.vkontakte.SocialVkontakteServices;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VkontakteServiceImpl implements SocialVkontakteServices {

    @Autowired
    private FindLastPostResponseHolder findLastPostResponseHolder;

    @Autowired
    private FindPostByIdResponseHolder findPostByIdResponseHolder;

    @Autowired
    private FindPostsByOverCreatedDateResponseHolder findPostsByOverCreatedDateResponseHolder;

    @Autowired
    private WriteNewPostResponseHolder writeNewPostResponseHolder;

    @Autowired
    private WriteNewPostsResponseHolder writeNewPostsResponseHolder;

    private ProducerTemplate producerTemplate;

    public VkontakteServiceImpl(CamelContext camelContext) {
        producerTemplate = camelContext.createProducerTemplate();
    }

    @Override
    public String findLastPost(FindPostRequest request) {

        String requestId = UUID.randomUUID().toString();
        findLastPostResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findLastPost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_FIND_LAST_POST_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public PostEntity getLastPost(String requestId) {
        return findLastPostResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostById(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostByIdResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostById");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_ID_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public PostEntity getPostById(String requestId) {
        return findPostByIdResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostsByOverCreateDate(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostsByOverCreatedDateResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostsByOverCreateDate");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public PostsResponse getPostsByOverCreateDate(String requestId) {
        return findPostsByOverCreatedDateResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePost(WritePostRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public PostEntity getWritePost(String requestId) {
        return writeNewPostResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePosts(WritePostsRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostsResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_LIST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public PostEntity getWritePosts(String requestId) {
        return writeNewPostsResponseHolder.getResponse(requestId);
    }

    @Override
    public RequestStatus getStatus(String requestId) {
        RequestStatus requestStatus;

        requestStatus = findLastPostResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostByIdResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostsByOverCreatedDateResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostsResponseHolder.getRequestStatus(requestId);
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
