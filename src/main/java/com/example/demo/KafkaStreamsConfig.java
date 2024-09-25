package com.example.demo;

import com.example.demo.handler.LogMessageHandler;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {
    @Value("${spring.kafka.streams.application-id}")
    private String applicationId;

    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.streams.retry.max-attempts}")
    private String maxAttempts;

    @Value("${spring.kafka.streams.retry.backoff.ms}")
    private String retryBackoffMs;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class.getName());
        props.put(StreamsConfig.DEFAULT_PRODUCTION_EXCEPTION_HANDLER_CLASS_CONFIG, LogMessageHandler.class);
        props.put(StreamsConfig.RETRIES_CONFIG, maxAttempts);  // Number of retry attempts
        props.put(StreamsConfig.RETRY_BACKOFF_MS_CONFIG, retryBackoffMs);  // Backoff time between retries (in milliseconds)
        props.put(StreamsConfig.producerPrefix(StreamsConfig.RETRIES_CONFIG), maxAttempts);
        props.put(StreamsConfig.producerPrefix(StreamsConfig.RETRY_BACKOFF_MS_CONFIG), retryBackoffMs);
        return new KafkaStreamsConfiguration(props);
    }
}