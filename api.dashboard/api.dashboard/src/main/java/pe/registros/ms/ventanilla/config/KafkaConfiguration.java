package pe.registros.ms.ventanilla.config;

import com.registros.event.TurnoEvent;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jspecify.annotations.NonNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

@Singleton
public class KafkaConfiguration {

  @ConfigProperty(name = "application.kafka-values.bootstrap-servers")
  String bootstrapServers;

  @Produces
  @Named("consumer")
  public Consumer<String, TurnoEvent> createConsumer() {
    Properties props = getProperties();

    /*
     * Config for consumer
     */
    props.putIfAbsent(ConsumerConfig.GROUP_ID_CONFIG,
        "turno-topic-sc-01");
    props.putIfAbsent(ConsumerConfig.CLIENT_ID_CONFIG,
        "turno-topic-client" + "-" + getHostname());
    props.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
        "earliest"); //latest
    props.putIfAbsent(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
        false);
    props.putIfAbsent(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
        500);

    /*
     * Others configs
     */
    props.putIfAbsent(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG,
        true);

    return new KafkaConsumer<>(props);
  }

  private @NonNull Properties getProperties() {
    Properties props = new Properties();

    /*
     * Kafka Bootstrap
     */
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    /*
     * Serializers for Keys and Values
     */
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());

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
    return props;
  }

  private String getHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "UnknownHost";
    }
  }
}
