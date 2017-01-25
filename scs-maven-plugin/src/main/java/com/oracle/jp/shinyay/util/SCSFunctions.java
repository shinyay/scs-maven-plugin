package com.oracle.jp.shinyay.util;

import com.oracle.jp.shinyay.http.DeleteMethod;
import com.oracle.jp.shinyay.http.GetMethod;
import com.oracle.jp.shinyay.http.PutMethod;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class SCSFunctions {

    public static Properties getSCSAuthToken(SCSInfo scsInfo) throws Exception {
        Properties scsProps = new Properties();


        HttpGet httpGet = new HttpGet("https://" + scsInfo.getIdentityDomain() + ".storage.oraclecloud.com/auth/v1.0");
        httpGet.addHeader(SCSConstants.HEADER_X_STORAGE_USER, "Storage-" + scsInfo.getIdentityDomain() + ":" + scsInfo.getUsername());
        httpGet.addHeader(SCSConstants.HEADER_X_STORAGE_PASS, scsInfo.getPassword());

        scsInfo.getLog().info("REQUEST: " + httpGet.getRequestLine());

        RequestConfig requestConfig = RequestConfig.custom().build();

        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {

            scsInfo.getLog().info("RESPONSE: " + httpResponse.getStatusLine());

            if (httpResponse.getStatusLine().getStatusCode() != SCSConstants.HTTP_STATUS_CODE_OK) {
                scsInfo.getLog().error("HTTP ERROR CODE: " + httpResponse.getStatusLine().getStatusCode());
            }

            Header[] headers = httpResponse.getAllHeaders();

            String authToken = Arrays.stream(headers).filter(s -> s.getName().contains(SCSConstants.HEADER_X_AUTH_TOKEN))
                    .findFirst()
                    .get()
                    .getValue();
            scsInfo.getLog().info(SCSConstants.HEADER_X_AUTH_TOKEN + ": " + authToken);
            scsProps.setProperty(SCSConstants.HEADER_X_AUTH_TOKEN, authToken);

            String storageUrl = Arrays.stream(headers).filter(s -> s.getName().contains(SCSConstants.HEADER_X_STORAGE_URL))
                    .findFirst()
                    .get()
                    .getValue();
            scsInfo.getLog().info(SCSConstants.HEADER_X_STORAGE_URL + ": " + storageUrl);
            scsProps.setProperty(SCSConstants.HEADER_X_STORAGE_URL, storageUrl);
        }
        return scsProps;
    }

    public static String listContainers(SCSInfo scsInfo) {
        Properties scsProps = scsInfo.getSCSProps();
        String url = scsProps.getProperty(SCSConstants.HEADER_X_STORAGE_URL);
        scsInfo.getLog().info("REQUEST: GET " + url);
        BasicNameValuePair[] headers = new BasicNameValuePair[1];
        headers[0] = new BasicNameValuePair(SCSConstants.HEADER_X_AUTH_TOKEN,
                scsProps.getProperty(SCSConstants.HEADER_X_AUTH_TOKEN));
        String result = null;
        try {
            result = GetMethod.HttpGetMethod(url, headers, null, null);
        } catch (Exception e) {
            scsInfo.getLog().error(e);
        } finally {
            return result;
        }
    }

    public static String showContainer(SCSInfo scsInfo) {
        Properties scsProps = scsInfo.getSCSProps();
        String url = scsProps.getProperty(SCSConstants.HEADER_X_STORAGE_URL) + "/" + scsInfo.getContainerName();
        scsInfo.getLog().info("REQUEST: GET " + url);
        BasicNameValuePair[] headers = new BasicNameValuePair[1];
        headers[0] = new BasicNameValuePair(SCSConstants.HEADER_X_AUTH_TOKEN,
                scsProps.getProperty(SCSConstants.HEADER_X_AUTH_TOKEN));
        String result = null;
        try {
            result = GetMethod.HttpGetMethod(url, headers, null, null);
        } catch (Exception e) {
            scsInfo.getLog().error(e);
        } finally {
            return result;
        }
    }

    public static String createContainer(SCSInfo scsInfo) {
        Properties scsProps = scsInfo.getSCSProps();
        String url = scsProps.getProperty(SCSConstants.HEADER_X_STORAGE_URL) + "/" + scsInfo.getContainerName();
        scsInfo.getLog().info("REQUEST: PUT " + url);
        BasicNameValuePair[] headers = new BasicNameValuePair[1];
        headers[0] = new BasicNameValuePair(SCSConstants.HEADER_X_AUTH_TOKEN,
                scsProps.getProperty(SCSConstants.HEADER_X_AUTH_TOKEN));
        String result = null;
        try {
            result = PutMethod.HttpPutMethod(url, headers, null, null);
        } catch (Exception e) {
            scsInfo.getLog().error(e);
        } finally {
            return result;
        }
    }

    public static String deleteContainer(SCSInfo scsInfo) {
        Properties scsProps = scsInfo.getSCSProps();
        String url = scsProps.getProperty(SCSConstants.HEADER_X_STORAGE_URL) + "/" + scsInfo.getContainerName();
        scsInfo.getLog().info("REQUEST: DELETE " + url);
        BasicNameValuePair[] headers = new BasicNameValuePair[1];
        headers[0] = new BasicNameValuePair(SCSConstants.HEADER_X_AUTH_TOKEN,
                scsProps.getProperty(SCSConstants.HEADER_X_AUTH_TOKEN));
        String result = null;
        try {
            result = DeleteMethod.HttpDeleteMethod(url, headers, null, null);
        } catch (Exception e) {
            scsInfo.getLog().error(e);
        } finally {
            return result;
        }
    }

    public static String deleteObjectInContainer(SCSInfo scsInfo) {
        Properties scsProps = scsInfo.getSCSProps();
        String url = scsProps.getProperty(SCSConstants.HEADER_X_STORAGE_URL)
                + "/"
                + scsInfo.getContainerName()
                + "/"
                + scsInfo.getObjectName();
        scsInfo.getLog().info("REQUEST: DELETE " + url);
        BasicNameValuePair[] headers = new BasicNameValuePair[1];
        headers[0] = new BasicNameValuePair(SCSConstants.HEADER_X_AUTH_TOKEN,
                scsProps.getProperty(SCSConstants.HEADER_X_AUTH_TOKEN));
        String result = null;
        try {
            result = DeleteMethod.HttpDeleteMethod(url, headers, null, null);
        } catch (Exception e) {
            scsInfo.getLog().error(e);
        } finally {
            return result;
        }
    }

    public static String deleteAllObjectsInContainer(SCSInfo scsInfo) {
        Properties scsProps = scsInfo.getSCSProps();
        String url = scsProps.getProperty(SCSConstants.HEADER_X_STORAGE_URL) + "/" + scsInfo.getContainerName();
        scsInfo.getLog().info("REQUEST: GET " + url);
        BasicNameValuePair[] headers = new BasicNameValuePair[1];
        headers[0] = new BasicNameValuePair(SCSConstants.HEADER_X_AUTH_TOKEN,
                scsProps.getProperty(SCSConstants.HEADER_X_AUTH_TOKEN));
        String result = "";
        try {
            HttpResponse response = GetMethod.HttpGetMethodWithMetaInfo(url, headers, null, null);

            switch (response.getStatusLine().getStatusCode()) {
                case SCSConstants.HTTP_STATUS_CODE_NO_CONTENT:
                    scsInfo.getLog().debug(response.toString());
                    result = scsInfo.getContainerName() + " is EMPTY";
                    break;
                case SCSConstants.HTTP_STATUS_CODE_NOT_FOUND:
                    scsInfo.getLog().debug(response.toString());
                    result = scsInfo.getContainerName() + " is NOT FOUND";
                    break;
                case SCSConstants.HTTP_STATUS_CODE_UNAUTHORIZED:
                    scsInfo.getLog().debug(response.toString());
                    result = scsInfo.getSCSProps().getProperty(SCSConstants.HEADER_X_AUTH_TOKEN) + " is EXPIRED";
                    break;
                case SCSConstants.HTTP_STATUS_CODE_OK:
                    String[] contents = EntityUtils.toString(response.getEntity()).split(SCSConstants.LINE_SEPARATOR_LINUX);
                    scsInfo.getLog().debug(response.toString());
                    Arrays.stream(contents).forEach(s -> {
                        try {
                            scsInfo.getLog().info("REQUEST: DELETE " + url + "/" + s);
                            DeleteMethod.HttpDeleteMethod(url + "/" + s, headers, null, null);
                            scsInfo.getLog().info("DELETED: " + s);
                        } catch (Exception e) {
                            scsInfo.getLog().error(e);
                        }
                    });
                    result = Arrays.stream(contents).map(s -> s + SCSConstants.LINE_SEPARATOR).collect(Collectors.joining());
                    break;
            }
        } catch (Exception e) {
            scsInfo.getLog().error(e);
        } finally {

            return result;
        }
    }
}
