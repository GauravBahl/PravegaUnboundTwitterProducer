package com.unbound.pravega.PravegaTwitterProducer;

import com.unbound.pravega.service.TwitterKeywordBasedCrawler;
import org.apache.commons.cli.CommandLine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.unbound.pravega")
public class PravegaTwitterProducerApplication {

	public static void main(String[] args) {
		CommandLine cmd = null;
		ApplicationContext applicationContext = SpringApplication.run(PravegaTwitterProducerApplication.class, args);
		TwitterKeywordBasedCrawler twitterKeywordBasedCrawler = applicationContext.getBean(TwitterKeywordBasedCrawler.class);
		twitterKeywordBasedCrawler.crawler();
	}

}
