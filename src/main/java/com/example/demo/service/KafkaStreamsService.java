package com.example.demo.service;

import com.example.demo.entity.Customer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import static com.example.demo.util.CustomerUtil.isEvenAge;
import static com.example.demo.util.CustomerUtil.isValidCustomer;

@Service
public class KafkaStreamsService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamsService.class);

    @Value("${kafka.topic.input}")
    String inputTopic;

    @Value("${kafka.topic.even}")
    String evenTopic;

    @Value("${kafka.topic.odd}")
    String oddTopic;

    @Bean
    public KStream<String, Customer> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, Customer> inputStream = streamsBuilder.stream(inputTopic, Consumed.with(Serdes.String(), new JsonSerde<>(Customer.class)));
        KStream<String, Customer>[] branches = inputStream.branch(
                (key, customer) -> isValidCustomer(customer) && isEvenAge(customer),  // Valid customer with even age
                (key, customer) -> isValidCustomer(customer) && !isEvenAge(customer), // Valid customer with odd age
                (key, customer) -> true  // Invalid data
        );
        // For even ages
        branches[0].foreach((key, customer) -> {
            logger.debug("Sending to CustomerEVEN topic: key={}, value={}", key, customer);
        });
        branches[0].to(evenTopic, Produced.with(Serdes.String(), new JsonSerde<>(Customer.class)));

        // For odd ages
        branches[1].foreach((key, customer) -> {
            logger.debug("Sending to CustomerODD topic: key={}, value={}", key, customer);
        });
        branches[1].to(oddTopic, Produced.with(Serdes.String(), new JsonSerde<>(Customer.class)));

        // Handle invalid data
        branches[2].foreach((key, customer) -> {
            logger.error("Skipping invalid customer data: key={}, value={}", key, customer);
        });
        return inputStream;
    }
}
