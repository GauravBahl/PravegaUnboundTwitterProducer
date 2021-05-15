package com.unbound.pravega.service;

import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.httpclient.BasicClient;
import com.unbound.pravega.config.TwitterAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TwitterKeywordBasedCrawler {
    @Autowired
    private TwitterAuthConfig authConfig;
    @Autowired
    private PravegaWriterService pravegaWriter;

    private BlockingQueue<String> queue;
    private StatusesFilterEndpoint endpoint;
    private List<String> keywords;
    private BasicClient client;

    @PostConstruct
    public void init() {
        queue = new LinkedBlockingQueue<String>(1000);
        endpoint = new StatusesFilterEndpoint();
        keywords = new ArrayList<>();
        keywords.add("covid india");
        keywords.add("india");
        keywords.add("oxygen cylinders");
        keywords.add("hospital bed");
        keywords.add("plasma donation");
        keywords.add("remdesivir");
        keywords.add("ventilators");
        keywords.add("icu");
        List<String> languages = new ArrayList<>();
        languages.add("en");
        endpoint.languages(languages);
        endpoint.trackTerms(keywords);
        client = authConfig.getTwitterClient(endpoint, queue);
    }
    public void crawler()  {
        client.connect();

        while(true) {
            try {
                while (!queue.isEmpty()) {
                    String message = queue.take();
                    pravegaWriter.writeStream(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
