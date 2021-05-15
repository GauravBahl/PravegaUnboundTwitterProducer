package com.unbound.pravega.config;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;

@Configuration
public class TwitterAuthConfig {

    @Value("${twitter.consumer.key}")
    private String consumer_key;

    @Value("${twitter.consumer.secret}")
    private String consumer_secret;

    @Value("${twitter.token}")
    private String token;

    @Value("${twitter.token.secret}")
    private String token_secret;

    @Bean
    public BasicClient getTwitterClient(StatusesFilterEndpoint endpoint, BlockingQueue<String> queue) {

        Authentication auth =
                new OAuth1(consumer_key,
                        consumer_secret,
                        token, token_secret);


        BasicClient client = new ClientBuilder()
                .hosts(Constants.STREAM_HOST)
                .endpoint(endpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(queue))
                .build();

        return client;
    }
}
