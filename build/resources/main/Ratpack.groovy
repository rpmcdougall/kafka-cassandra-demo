import modules.KafkaServiceModule

import static ratpack.groovy.Groovy.ratpack

ratpack {

  serverConfig {
    threads 1
  }

  bindings {
    module(KafkaServiceModule)
  }

  handlers {
    get {
      render "I'll put some test output here at some point"
    }
  }
}
