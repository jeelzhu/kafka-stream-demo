Overview

This project is a Spring Boot application that leverages Kafka Streams to process and route customer data based on specific conditions. The application reads customer data from an input Kafka topic, processes the data to determine whether a customer’s age is even or odd, and then routes the data to corresponding Kafka topics (customerEven, customerOdd). Invalid data is handled separately.

Assumptions

	•	Kafka Cluster: The application assumes that there is a Kafka cluster running and accessible at the configured bootstrap server address.
	•	Kafka Topics: The application assumes that the following Kafka topics already exist:
	•	input-topic: The topic from which customer data is read.
	•	CustomerEVEN: The topic where customer data with even ages is sent.
	•	CustomerODD: The topic where customer data with odd ages is sent.

Features

	•	Stream Processing: Real-time processing of customer data using Kafka Streams.
	•	Conditional Routing: Routes customer data to different Kafka topics based on age.
	•	Error Handling: Logs and optionally processes invalid customer data.
	•	Configurable Topics: Kafka topics are configurable through external properties.
	•	Testing: The Kafka Streams topology can be tested using TopologyTestDriver for unit testing, ensuring the processing logic works correctly before deployment.
Build the Project

Use Maven to build the project:
mvn clean install

Run the Application

Run the Spring Boot application:
mvn spring-boot:run

Configuration

All configurations for Kafka and other settings are managed through the application.properties 

Testing

Unit tests for the Kafka Streams topology can be written using the TopologyTestDriver. To run the tests:
mvn test

Contact

For questions or support, please contact jeelzhu@gmail.com.