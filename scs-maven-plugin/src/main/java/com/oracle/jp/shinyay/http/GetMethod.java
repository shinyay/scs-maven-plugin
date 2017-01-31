package com.oracle.jp.shinyay.http;

import com.oracle.jp.shinyay.util.SCSConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.Credentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GetMethod {
    private GetMethod() {
    }

    public static String HttpGetMethod(String url, BasicNameValuePair[] headers, StringEntity body, Credentials credUser) throws Exception {
        String httpResult;

        HttpGet httpGet = new HttpGet(url);
        for (BasicNameValuePair header : headers) {
            httpGet.addHeader(header.getName(), header.getValue());
        }

        RequestConfig requestConfig = RequestConfig.custom().build();

        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
            int httpStatus = httpResponse.getStatusLine().getStatusCode();

            if (!(httpStatus == SCSConstants.HTTP_STATUS_CODE_OK
                    || httpStatus == SCSConstants.HTTP_STATUS_CODE_ACCEPTED
                    || httpStatus == SCSConstants.HTTP_STATUS_CODE_NO_CONTENT
                    || httpStatus == SCSConstants.HTTP_STATUS_CODE_NOT_FOUND)) {
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

    public static HttpResponse HttpGetMethodWithMetaInfo(String url, BasicNameValuePair[] headers, StringEntity body, Credentials credUser) throws Exception {

        HttpGet httpGet = new HttpGet(url);
        for (BasicNameValuePair header : headers) {
            httpGet.addHeader(header.getName(), header.getValue());
        }

        RequestConfig requestConfig = RequestConfig.custom().build();
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        return httpClient.execute(httpGet);
    }
}
