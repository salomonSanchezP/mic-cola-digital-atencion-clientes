package pe.registros.ms.turnos.config;


import com.registros.event.TurnoEvent;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Properties;

@Singleton
public class KafkaConfiguration {

  @ConfigProperty(name = "application.kafka-values.bootstrap-servers")
  String bootstrapServers;

  @Produces
  @Named("producer")
  public Producer<String, TurnoEvent> createProducer() {
    Properties props = new Properties();

    /*
     * Kafka Bootstrap
     */
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    /*
     * Serializers for Keys and Values
     */
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());

    /*
     * Schema registry
     */
    props.put(
        AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
        "http://localhost:8081"
    );
    props.put(
        AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS,
        true
    );
    props.put(
        AbstractKafkaSchemaSerDeConfig.USE_LATEST_VERSION,
        true
    );

    /*
     * Others configs
     */
    props.put(ProducerConfig.ACKS_CONFIG, "1");
    props.put(ProducerConfig.LINGER_MS_CONFIG, 5);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 32768);
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");

    return new KafkaProducer<>(props);
  }
}
