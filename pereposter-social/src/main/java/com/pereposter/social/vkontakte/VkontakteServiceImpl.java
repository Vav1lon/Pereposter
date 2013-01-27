package com.pereposter.social.vkontakte;

import com.pereposter.social.api.Constants;
import com.pereposter.social.api.SocialWebServices;
import com.pereposter.social.api.entity.*;
import com.pereposter.social.vkontakte.holder.*;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VkontakteServiceImpl implements SocialWebServices {

    @Autowired
    private FindLastPostVkontakteResponseHolder findLastPostVkontakteResponseHolder;

    @Autowired
    private FindPostByIdVkontakteResponseHolder findPostByIdVkontakteResponseHolder;

    @Autowired
    private FindPostsByOverCreatedDateVkontakteResponseHolder findPostsByOverCreatedDateVkontakteResponseHolder;

    @Autowired
    private WriteNewPostVkontakteResponseHolder writeNewPostVkontakteResponseHolder;

    @Autowired
    private WriteNewPostsVkontakteResponseHolder writeNewPostsVkontakteResponseHolder;

    private ProducerTemplate producerTemplate;

    public VkontakteServiceImpl(CamelContext camelContext) {
        producerTemplate = camelContext.createProducerTemplate();
    }

    @Override
    public String findLastPost(FindPostRequest request) {

        String requestId = UUID.randomUUID().toString();
        findLastPostVkontakteResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findLastPost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_FIND_LAST_POST_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostEntity> getLastPost(String requestId) {
        return findLastPostVkontakteResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostById(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostByIdVkontakteResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostById");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_ID_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostEntity> getPostById(String requestId) {
        return findPostByIdVkontakteResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostsByOverCreateDate(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostsByOverCreatedDateVkontakteResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostsByOverCreateDate");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostsResponse> getPostsByOverCreateDate(String requestId) {
        return findPostsByOverCreatedDateVkontakteResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePost(WritePostRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostVkontakteResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<String> getWritePost(String requestId) {
        return writeNewPostVkontakteResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePosts(WritePostsRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostsVkontakteResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePosts");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_VKONTAKTE_POST_LIST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<String> getWritePosts(String requestId) {
        return writeNewPostsVkontakteResponseHolder.getResponse(requestId);
    }

    @Override
    public RequestStatus getStatus(String requestId) {
        RequestStatus requestStatus;

        requestStatus = findLastPostVkontakteResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostByIdVkontakteResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostsByOverCreatedDateVkontakteResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostVkontakteResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostsVkontakteResponseHolder.getRequestStatus(requestId);
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
