package com.unbound.pravega.config;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.EventStreamWriter;
import io.pravega.client.stream.EventWriterConfig;
import io.pravega.client.stream.ScalingPolicy;
import io.pravega.client.stream.StreamConfiguration;
import io.pravega.client.stream.impl.JavaSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@ComponentScan("com.unbound.pravega")
public class PravegaProducerConfig {
    @Value("${io.reflectoring.pravega.bootstrap-servers}")
    private String controllerStr;
    @Value("${io.reflectoring.pravega.scope}")
    private String scope;
    @Value("${io.reflectoring.pravega.streamName}")
    private  String streamName;

    @Bean
    public EventStreamWriter<String> streamReaderBean() {

        URI controllerURI = null;
        try {
            controllerURI = new URI(controllerStr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        StreamManager streamManager = StreamManager.create(controllerURI);
        final boolean scopeIsNew = streamManager.createScope(scope);

        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .scalingPolicy(ScalingPolicy.fixed(1))
                .build();
        final boolean streamIsNew = streamManager.createStream(scope, streamName, streamConfig);


        EventStreamClientFactory clientFactory = EventStreamClientFactory.withScope(scope,
                ClientConfig.builder().controllerURI(controllerURI).build());
             EventStreamWriter<String> writer = clientFactory.createEventWriter(streamName,
                     new JavaSerializer<String>(),
                     EventWriterConfig.builder().build());

        return writer;
    }
}
