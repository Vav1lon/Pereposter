package com.pereposter.social.googleplus;

import com.pereposter.social.api.Constants;
import com.pereposter.social.api.SocialWebServices;
import com.pereposter.social.api.entity.*;
import com.pereposter.social.googleplus.holder.*;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GooglePlusServiceImpl implements SocialWebServices {

    @Autowired
    private FindLastPostGooglePlusResponseHolder findLastPostGooglePlusResponseHolder;

    @Autowired
    private FindPostByIdGooglePlusResponseHolder findPostByIdGooglePlusResponseHolder;

    @Autowired
    private FindPostsByOverCreatedDateGooglePlusResponseHolder findPostsByOverCreatedDateGooglePlusResponseHolder;

    @Autowired
    private WriteNewPostGooglePlusResponseHolder writeNewPostGooglePlusResponseHolder;

    @Autowired
    private WriteNewPostsGooglePlusResponseHolder writeNewPostsGooglePlusResponseHolder;

    private ProducerTemplate producerTemplate;

    public GooglePlusServiceImpl(CamelContext camelContext) {
        producerTemplate = camelContext.createProducerTemplate();
    }

    @Override
    public String findLastPost(FindPostRequest request) {

        String requestId = UUID.randomUUID().toString();
        findLastPostGooglePlusResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findLastPost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_GOOGLEPLUS_POST_FIND_LAST_POST_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostEntity> getLastPost(String requestId) {
        return findLastPostGooglePlusResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostById(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostByIdGooglePlusResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostById");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_GOOGLEPLUS_POST_FIND_BY_ID_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostEntity> getPostById(String requestId) {
        return findPostByIdGooglePlusResponseHolder.getResponse(requestId);
    }

    @Override
    public String findPostsByOverCreateDate(FindPostRequest request) {
        String requestId = UUID.randomUUID().toString();
        findPostsByOverCreatedDateGooglePlusResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "findPostsByOverCreateDate");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_GOOGLEPLUS_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<PostsResponse> getPostsByOverCreateDate(String requestId) {
        return findPostsByOverCreatedDateGooglePlusResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePost(WritePostRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostGooglePlusResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePost");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_GOOGLEPLUS_POST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<String> getWritePost(String requestId) {
        return writeNewPostGooglePlusResponseHolder.getResponse(requestId);
    }

    @Override
    public String writePosts(WritePostsRequest request) {
        String requestId = UUID.randomUUID().toString();
        writeNewPostsGooglePlusResponseHolder.waitForResponse(requestId);
        Map<String, Object> headers = createHeaders(requestId, "writePosts");

        producerTemplate.sendBodyAndHeaders(Constants.SOCIAL_GOOGLEPLUS_POST_LIST_WRITE_REQUEST_QUEUE, ExchangePattern.InOnly, request, headers);

        return requestId;
    }

    @Override
    public ResponseObject<String> getWritePosts(String requestId) {
        return writeNewPostsGooglePlusResponseHolder.getResponse(requestId);
    }

    @Override
    public RequestStatus getStatus(String requestId) {
        RequestStatus requestStatus;

        requestStatus = findLastPostGooglePlusResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostByIdGooglePlusResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = findPostsByOverCreatedDateGooglePlusResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostGooglePlusResponseHolder.getRequestStatus(requestId);
        if (requestStatus != RequestStatus.NOT_FOUND) return requestStatus;

        requestStatus = writeNewPostsGooglePlusResponseHolder.getRequestStatus(requestId);
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
