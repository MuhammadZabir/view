package my.com.view.web.rest;


import com.codahale.metrics.annotation.Timed;
import my.com.view.config.ApplicationProperties;
import my.com.view.domain.kafka.OperationMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@RestController
@RequestMapping("/api")
public class RequestResource {
    private final Logger log = LoggerFactory.getLogger(RequestResource.class);

    private final Environment env;
    private final ApplicationProperties appProperties;

    public RequestResource(Environment env, ApplicationProperties appProperties){
        this.env = env;
        this.appProperties = appProperties;

        log.info("Connected to the kafka topic {}", env.getProperty("application.operation.kafka.adhoc.group"));
        log.info("Connected to the kafka topic {}", env.getProperty("application.operation.kafka.adhoc.topic"));
    }


    @PostMapping("/request")
    @Timed
    public ResponseEntity<Void> requestOperation(@RequestParam("companyId") String companyId,
                                                 @RequestParam("excel") MultipartFile attachment) {
        String filename = attachment.getOriginalFilename();
        try {
            Path uploadAttachment = Paths.get(System.getProperty("user.dir"), filename);
            if (Files.exists(uploadAttachment)) {
                Files.delete(uploadAttachment);
            }
            attachment.transferTo(uploadAttachment.toFile());
        } catch (IOException e) {
            log.error("Error", e);
            return ResponseEntity.status(500).build();
        }

        Properties properties = new Properties();
        String bootStrapServer = env.getProperty("spring.kafka.bootstrap-servers");
        properties.setProperty("bootstrap.servers", bootStrapServer);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", OperationMessage.OperationMessageSerializer.class.getName());

        OperationMessage message = OperationMessage.OperationMessageBuilder.builder()
            .setId(Long.parseLong(companyId)).setFilename(filename).build();

        try (KafkaProducer<String, OperationMessage> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<String, OperationMessage> producerRecord = new ProducerRecord<>(appProperties.getOperation()
                .getKafka().getAdhoc().getTopic(), message);
            producer.send(producerRecord);
        }

        return ResponseEntity.ok().build();
    }
}
