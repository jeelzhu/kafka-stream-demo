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

/*
 * This class is used to process data from an input Kafka topic and send it to output topics based on the age of the customer.
 */
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
                this::isValidEvenAgeCustomer, // Valid customer with even age
                this::isValidOddAgeCustomer,  // Valid customer with odd age
                (key, customer) -> true       // Invalid data
        );

        branches[0].to(evenTopic, Produced.with(Serdes.String(), new JsonSerde<>(Customer.class)));
        branches[1].to(oddTopic, Produced.with(Serdes.String(), new JsonSerde<>(Customer.class)));

        handleInvalidData(branches[2]);

        return inputStream;
    }

    private boolean isValidEvenAgeCustomer(String key, Customer customer) {
        return isValidCustomer(customer) && isEvenAge(customer);
    }

    private boolean isValidOddAgeCustomer(String key, Customer customer) {
        return isValidCustomer(customer) && !isEvenAge(customer);
    }

    private void handleInvalidData(KStream<String, Customer> invalidDataStream) {
        invalidDataStream.foreach((key, customer) -> {
            logger.error("Skipping invalid customer data: key={}, value={}", key, customer);
            // Add custom logic here to handle invalid data
            // For example, you can send it to another kafka topic
        });
    }
}
