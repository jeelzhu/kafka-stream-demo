Overview

This project is a Spring Boot application that leverages Kafka Streams to process and route customer data based on specific conditions. The application reads customer data from an input Kafka topic, processes the data to determine whether a customer’s age is even or odd, and then routes the data to corresponding Kafka topics (evenTopic, oddTopic). Invalid data is handled separately.

Features

	•	Stream Processing: Real-time processing of customer data using Kafka Streams.
	•	Conditional Routing: Routes customer data to different Kafka topics based on age.
	•	Error Handling: Logs and optionally processes invalid customer data.
	•	Configurable Topics: Kafka topics are configurable through external properties.
