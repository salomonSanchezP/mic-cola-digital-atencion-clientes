package pe.registros.ms.turnos.messaging;

import com.registros.event.TurnoEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;
import pe.registros.ms.turnos.entity.TurnoEntity;

import java.time.ZoneId;

@ApplicationScoped
public class TurnoKafkaService {

    private static final Logger LOGGER = Logger.getLogger(TurnoKafkaService.class);

    @Inject
    @Named("producer")
    Producer<String, TurnoEvent> producer;

    public void sendEventMessaging(TurnoEntity request) {
        TurnoEvent event = TurnoEvent.newBuilder()
                .setNombreCliente(request.getNombreCliente())
                .setDocumentoIdentidad(request.getDocumentoIdentidad())
                .setCodigoTurno(request.getCodigoTurno())
                .setEstado(com.registros.event.EstadoTurno.valueOf(request.getEstado().name()))
                .setFechaHoraCreacion(request.fechaHoraCreacion.atZone(ZoneId.of("America/Lima")).toInstant())
                .setFechaHoraLlamado(request.getFechaHoraLlamado() != null ? 
                    request.getFechaHoraLlamado().atZone(ZoneId.of("America/Lima")).toInstant() : null)
                .setPosicionEnCola(request.getPosicionEnCola())
                .build();

        ProducerRecord producerRecord = new ProducerRecord<>(
                "turno-topic", event
        );

        producer.send(producerRecord, (metadata, exception) -> {
            if (exception != null) {
                LOGGER.info("### Error al enviar evento a kafka" + exception.getMessage());
            } else {
                LOGGER.infof("### Evento enviado a kafka topic=%s, partition=%s, offset=%d",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }
}
