package models

import org.influxdb.annotation.Column
import org.influxdb.annotation.Measurement

import java.time.Instant

@Measurement(name = "weather")
class WeatherMetric {

    @Column(name="location")
    String location

    @Column(name="season")
    String season

    @Column(name="temperature")
    Integer temperature

    @Column(name="time")
    Instant time
}
