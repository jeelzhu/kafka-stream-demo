package com.example.demo.handler;

import com.example.demo.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.errors.ProductionExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
/*
 * This class is used to handle exceptions that occur during the production of records to a Kafka topic.
 * It logs the exception and the record details to the console.
 */
public class LogMessageHandler implements ProductionExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogMessageHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ProductionExceptionHandlerResponse handle(ProducerRecord<byte[], byte[]> record, Exception exception) {
        String key = record.key() != null ? new String(record.key(), StandardCharsets.UTF_8) : "null";
        String valueString = "null";

        if (record.value() != null) {
            try {
                Customer value = objectMapper.readValue(record.value(), Customer.class);
                valueString = value.toString();
            } catch (Exception e) {
                valueString = "Failed to deserialize value";
                logger.error("Error deserializing record value: {}", e.getMessage());
            }
        }

        logger.error("Failed to produce record to topic '{}', key='{}', value='{}'. Exception:",
                record.topic(),
                key,
                valueString,
                exception);

        return ProductionExceptionHandlerResponse.CONTINUE;
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}