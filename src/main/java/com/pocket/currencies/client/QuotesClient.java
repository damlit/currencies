package com.pocket.currencies.client;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class QuotesClient {
    private final Logger LOG = LoggerFactory.getLogger("logger");

    private final Environment environment;

    private static final String URL = "quotes.client.url";
    private static final String ENDPOINT = "quotes.client.endpoint";
    private static final String KEY = "quotes.client.key";

    @Retryable(value = IOException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public String getQuotesFromService() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String endpoint = getEndpoint();
        LOG.info("Update quotes is starting for endpoint " + endpoint);
        Request request = new Request.Builder()
                .url(endpoint)
                .method("GET", null)
                .build();
        return client.newCall(request).execute().body().string();
    }

    private String getEndpoint() {
        return environment.getProperty(URL) + "/"
                + environment.getProperty(ENDPOINT)
                + "?access_key=" + environment.getProperty(KEY);
    }
}
