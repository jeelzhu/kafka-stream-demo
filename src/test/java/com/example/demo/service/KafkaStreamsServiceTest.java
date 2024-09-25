package com.example.demo.service;

import com.example.demo.entity.Customer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class KafkaStreamsServiceTest {
    private TopologyTestDriver testDriver;
    private TestInputTopic<String, Customer> inputTopic;
    private TestOutputTopic<String, Customer> evenOutputTopic;
    private TestOutputTopic<String, Customer> oddOutputTopic;

    private final String INPUT_TOPIC = "customer-input-topic";
    private final String EVEN_TOPIC = "customer-even-topic";
    private final String ODD_TOPIC = "customer-odd-topic";

    @BeforeEach
    public void setup() {
        // Define the properties for the test driver
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "test-state");

        // Build the topology using the same logic as the production code
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // Instantiate your service and build the topology
        KafkaStreamsService kafkaStreamsService = new KafkaStreamsService();
        // Set the topic names directly since @Value won't work in tests
        kafkaStreamsService.inputTopic = INPUT_TOPIC;
        kafkaStreamsService.evenTopic = EVEN_TOPIC;
        kafkaStreamsService.oddTopic = ODD_TOPIC;
        kafkaStreamsService.kStream(streamsBuilder);
        Topology topology = streamsBuilder.build();

        // Initialize the test driver
        testDriver = new TopologyTestDriver(topology, props);

        // Create input and output topics
        JsonSerde<Customer> customerSerde = new JsonSerde<>(Customer.class);
        inputTopic = testDriver.createInputTopic(INPUT_TOPIC, Serdes.String().serializer(), customerSerde.serializer());
        evenOutputTopic = testDriver.createOutputTopic(EVEN_TOPIC, Serdes.String().deserializer(), customerSerde.deserializer());
        oddOutputTopic = testDriver.createOutputTopic(ODD_TOPIC, Serdes.String().deserializer(), customerSerde.deserializer());
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }

    @Test
    public void testValidEvenCustomer() {
        final Customer customer = new Customer("Elon", "Musk", LocalDate.of(1970, 1, 1));
        inputTopic.pipeInput("key1", customer);

        // Assert that the customer is routed to the even topic
        assertFalse(evenOutputTopic.isEmpty());
        assertTrue(oddOutputTopic.isEmpty());

        TestRecord<String, Customer> record = evenOutputTopic.readRecord();
        assertEquals("key1", record.key());
        assertEquals(customer, record.value());
    }

    @Test
    public void testValidOddCustomer() {
        final Customer customer = new Customer("Jeff", "Bezos", LocalDate.of(1971, 1, 1));
        inputTopic.pipeInput("key2", customer);

        // Assert that the customer is routed to the odd topic
        assertTrue(evenOutputTopic.isEmpty());
        assertFalse(oddOutputTopic.isEmpty());

        TestRecord<String, Customer> record = oddOutputTopic.readRecord();
        assertEquals("key2", record.key());
        assertEquals(customer, record.value());
    }

    @Test
    public void testInvalidCustomer() {
        final Customer customer = new Customer("aaa", "unknown", null);
        inputTopic.pipeInput("key3", customer);

        // Assert that the customer is not routed to any output topic
        assertTrue(evenOutputTopic.isEmpty());
        assertTrue(oddOutputTopic.isEmpty());
    }
}
