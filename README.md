Kafka Cassandra Demo
-----------------------------
This project is to demonstrate how to make a simple Kafka data pipeline from a topic into a Cassandra database. This will also contain a simple ETL process for converting ILP from a topic into a POJO to make it more easily consumable. 

To start a test instance of Zookeeper, start the container with the included docker compose file
```
docker-compose up -d
```

To create a test topic download some useful Kafka tools [here](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.0.0/kafka_2.11-2.0.0.tgz).: 

To create a topic with the aforementioned tools run the following:
```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic <topic name>
```

You can send messages to the topic with the following command by entering them into the resulting prompt:
```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic <topic name>
> some message
> another message
```

## TODO
- Convert ILP string into POJO
- Send to Cassandra instance
- Test with multiple instances of this group to verify consumer group behavior


