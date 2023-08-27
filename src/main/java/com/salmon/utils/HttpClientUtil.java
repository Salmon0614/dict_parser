package com.salmon.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Objects;

/**
 * HttpClient工具
 *
 * @author Salmon
 * @since 2023-08-28
 */
@Slf4j
public class HttpClientUtil {

    public static byte[] download(String url) {
        byte[] content = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (Objects.nonNull(response)) {
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        content = EntityUtils.toByteArray(entity);
                    }
                }
            } catch (Exception e) {
                log.error("error url: {}", url, e);
            } finally {
                try {
                    httpGet.releaseConnection();
                } catch (Exception e) {
                    log.error("error url: {}", url, e);
                }
            }
        } catch (Exception e) {
            log.error("httpclient create error", e);
        }
        return content;
    }
}
