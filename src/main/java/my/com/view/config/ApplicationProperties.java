package my.com.view.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to View.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Operation operation;

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public static class Operation {
        private Kafka kafka;

        public Kafka getKafka() {
            return kafka;
        }

        public void setKafka(Kafka kafka) {
            this.kafka = kafka;
        }
    }

    public static class Kafka {
        private Adhoc adhoc;

        public Adhoc getAdhoc() {
            return adhoc;
        }

        public void setAdhoc(Adhoc adhoc) {
            this.adhoc = adhoc;
        }
    }

    public static class Adhoc {
        private String group;
        private String topic;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }
}
