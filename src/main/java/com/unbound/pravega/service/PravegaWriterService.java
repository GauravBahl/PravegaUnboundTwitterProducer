package com.unbound.pravega.service;

import io.pravega.client.stream.EventStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PravegaWriterService {

    @Autowired
    private EventStreamWriter<String> writer;

    public CompletableFuture<Void> writeStream(String message) {
        return writer.writeEvent(message);
    }
}
