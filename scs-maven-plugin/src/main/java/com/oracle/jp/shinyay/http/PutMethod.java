package com.oracle.jp.shinyay.http;

import com.oracle.jp.shinyay.util.SCSConstants;
import org.apache.http.HttpEntity;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;
import java.util.stream.Collectors;

public class PutMethod {
    private PutMethod() {
    }

    public static String HttpPutMethod(String url, BasicNameValuePair[] headers, HttpEntity body, Credentials credUser) throws Exception {
        String httpResult;

        HttpPut httpPut = new HttpPut(url);
        for (BasicNameValuePair header : headers) {
            httpPut.addHeader(header.getName(), header.getValue());
        }

        Optional<HttpEntity> httpBody = Optional.ofNullable(body);
        httpBody.ifPresent(httpPut::setEntity);

        RequestConfig requestConfig = RequestConfig.custom().build();

        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
                CloseableHttpResponse httpResponse = httpClient.execute(httpPut);
        ) {
            int httpStatus = httpResponse.getStatusLine().getStatusCode();

            if (url.toLowerCase().contains("storage")
                    && httpStatus != SCSConstants.HTTP_STATUS_CODE_ACCEPTED
                    && httpStatus != SCSConstants.HTTP_STATUS_CODE_CREATED) {
                return "HTTP ERROR CODE: " + httpStatus + " : " + "ERROR REASON: " + httpResponse.getStatusLine().getReasonPhrase();
            }

            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String[] results = EntityUtils.toString(entity).split(SCSConstants.LINE_SEPARATOR_LINUX);
                httpResult = Arrays.stream(results).map(s -> s + SCSConstants.LINE_SEPARATOR).collect(Collectors.joining());
            } else {
                httpResult = httpResponse.getStatusLine() + SCSConstants.LINE_SEPARATOR;
            }
        }
        return httpResult;
    }
}
