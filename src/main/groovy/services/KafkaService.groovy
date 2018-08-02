package services

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import models.WeatherMetric
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.influxdb.dto.QueryResult
import org.influxdb.impl.InfluxDBImpl
import org.influxdb.impl.InfluxDBResultMapper
import ratpack.exec.Blocking
import ratpack.exec.ExecResult
import ratpack.exec.Execution
import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.func.Function
import ratpack.service.StartEvent
import ratpack.service.Service
import util.IlpParser

import javax.management.Query

@Slf4j
@CompileStatic
class KafkaService implements Service, Action<Execution> {

    KafkaConsumer<String, String> consumer

    @Override
    void onStart(StartEvent event) throws Exception {
        Properties props = new Properties()
        props.putAll([
                "bootstrap.servers":"localhost:9092",
                "group.id"               : "metrics-consumers",
                "enable.auto.commit"     : "true",
                "auto.commit.interval.ms": "1000",
                "session.timeout.ms"     : "30000",
                "key.deserializer"       : "org.apache.kafka.common.serialization.StringDeserializer",
                "value.deserializer"     : "org.apache.kafka.common.serialization.StringDeserializer"
        ])

        consumer = new KafkaConsumer<>(props)
        consumer.subscribe(["metrics-topic"])

        Execution.fork().start(this)
    }

    @Override
    void execute(Execution execution) throws Exception {

        recursivePoll().result({ ExecResult result ->
            if (result.isError()) {
                log.error("Error!", result.throwable)
            } else {
                log.info("Consumer unavailable for: ", result.value)
            }
            consumer.close()
        })

    }

    Promise recursivePoll() {
        Blocking.get(this.&pollKafka)
                .flatMap(this.&process as Function)
                .flatMap({ ignore -> this.recursivePoll() } as Function)
    }

    ConsumerRecords<String, String> pollKafka() {
        println "Polling on ${Thread.currentThread().name}"
        consumer.poll(5000)
    }

    Promise process(ConsumerRecords<String, String> records) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (ConsumerRecord<String, String> record : records) {
            println "offset: ${record.offset()}, key: ${record.key()} value: ${record.value()} on ${Thread.currentThread().name}"

            IlpParser parser = new IlpParser()
            def parsedRecord = parser.parse(record.value())
            Map<String, String> map = objectMapper.convertValue(parsedRecord, Map.class)
            println "-------"
                println map
            println "-------"
        }
        return Promise.value("Processed ${records.count()} messages.")
    }


}