package modules

import com.google.inject.AbstractModule
import services.KafkaService

class KafkaServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(KafkaService)
    }
}
