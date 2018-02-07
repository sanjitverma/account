package com.virtualbank.account.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

import java.io.IOException;
import java.util.Map;

/**
 * Created by SANJIT on 31/01/18.
 */
public class HeaderSettingRequestCallback implements RequestCallback {


    final Map<String, String> requestHeaders;

    private String body;

    public HeaderSettingRequestCallback(final Map<String, String> headers) {
        this.requestHeaders = headers;
    }

    public void setBody(final String postBody) {
        this.body = postBody;
    }

    @Override
    public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {

        final HttpHeaders clientHeaders = clientHttpRequest.getHeaders();
        for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            clientHeaders.add(entry.getKey(), entry.getValue());
        }
        if (null != body) {
            clientHttpRequest.getBody().write(body.getBytes());
        }
    }
}
