package util

import groovy.transform.Canonical

class IlpParser {
//https://www.programcreek.com/java-api-examples/index.php@source_dir=flex-blazeds-master/modules/core/src/flex/messaging/cluster/?code=mukatee/kafka-consumer/kafka-consumer-master/src/net/kanstren/kafka/influx/telegraf/InFluxTelegrafConsumer.java
    ParsedMeasure parse(String line) {
        line = line.replaceAll("\\\\ ", "_");
        String[] split = line.split(" ");
        Long time = Long.parseLong(split[2]);
        String keyValue = split[1];
        String[] kvSplit = keyValue.split("=");
        String fieldName = kvSplit[0];
        String fieldValue = kvSplit[1];
        String measureAndTags = split[0];
        Map<String, String> tags = new HashMap<>();
        String measure = parseMeasureAndTags(measureAndTags, tags);
        new ParsedMeasure(measure, time, fieldName, fieldValue, tags);
    }

    String parseMeasureAndTags(String measureAndTags, Map<String, String> tags) {
        String[] split = measureAndTags.split(",");
        for (int i = 1 ; i < split.length ; i++) {
            String tagKeyValue = split[i];
            String[] tagTeam = tagKeyValue.split("=");
            tags.put(tagTeam[0], tagTeam[1]);
        }
       split[0]
    }


     class ParsedMeasure {

        public final String name;
        public final long time;
        public final String fieldName;
        public final String fieldValue;
        public final Map<String, String> tags;

        ParsedMeasure(String name, long time, String fieldName, String fieldValue, Map<String, String> tags) {
            this.name = name
            this.time = time
            this.fieldName = fieldName
            this.fieldValue = fieldValue
            this.tags = tags
        }

     }

}
