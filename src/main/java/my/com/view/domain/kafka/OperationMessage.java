package my.com.view.domain.kafka;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OperationMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OperationMessage{" +
            "id=" + id +
            '}';
    }

    public static class OperationMessageBuilder {

        private Long id;

        public static OperationMessageBuilder builder() {
            return new OperationMessageBuilder();
        }

        public OperationMessageBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OperationMessage build() {
            OperationMessage operationMessage = new OperationMessage();

            operationMessage.setId(id);

            return operationMessage;
        }
    }

    public static class OperationMessageSerializer implements Serializer<OperationMessage> {

        private Logger logger = LogManager.getLogger(this.getClass());
        private ObjectMapper objectMapper;

        public OperationMessageSerializer() {
            objectMapper = new ObjectMapper();
            objectMapper.registerSubtypes(OperationMessage.class);
            objectMapper.findAndRegisterModules();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {

        }

        @Override
        public byte[] serialize(String topic, OperationMessage data) {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                logger.error(String.format("An error occured while serializing $s", data), e);
                return new byte[]{};
            }
        }

        @Override
        public void close() {

        }
    }

    public static class OperationMessageDeserializer implements Deserializer<OperationMessage> {

        private Logger logger = LogManager.getLogger(this.getClass());
        private ObjectMapper objectMapper;

        public OperationMessageDeserializer() {
            objectMapper = new ObjectMapper();
            objectMapper.registerSubtypes(OperationMessage.class);
            objectMapper.findAndRegisterModules();
            objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {

        }

        @Override
        public OperationMessage deserialize(String topic, byte[] data) {
            try {
                return objectMapper.readValue(data, OperationMessage.class);
            } catch (IOException e) {
                logger.error(String.format("An error occured while deserializing $s", data), e);
                return null;
            }
        }

        @Override
        public void close() {

        }
    }
}

