package pe.registros.ms.ventanilla.messagin;

import com.registros.event.TurnoEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.registros.ms.ventanilla.dto.TurnoEnEsperaDTO;
import pe.registros.ms.ventanilla.service.TurnoServiceImpl;

@ApplicationScoped
public class TurnoKafkaConsumerService {

  private static final Logger LOGGER = Logger.getLogger(TurnoKafkaConsumerService.class);
  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

  @Inject
  @Named("consumer")
  Consumer<String, TurnoEvent> consumer;

  @Inject
  @RestClient
  TurnoClient turnoClient;

  @Inject
  TurnoServiceImpl turnoService;

  boolean running = true;

  public void initialize(@Observes StartupEvent event) {
    LOGGER.info("### -----------------------------Init kafka services ---------------------------------###");
    consumer.subscribe(Collections.singletonList("turno-topic"));
    executor.submit(() -> {
      while (running) {
        try {
          var records = consumer.poll(Duration.ofSeconds(1));
          if (!records.isEmpty()) {
            records.forEach(record ->
                executor.submit(() -> {
                  processRecord(record);
                }));
          }
        } catch (Exception e) {
          LOGGER.error("Error al procesar eventos de kafka", e);
        }
      }
    });
  }

  public void processRecord(ConsumerRecord<String, TurnoEvent> record) {
    var turnoEvent = record.value();
    String estado = turnoEvent.getEstado().toString();
    LOGGER.infof("###---------------------------------------------------------------------------###");

    if (estado.equals("LLAMADO")) {
      mostraMensajeIPTV(turnoEvent);
      mostrarMnesajeIPTVListaPorAtender();
    }

    if (estado.equals("EN_ESPERA") ) {
      mostrarMnesajeIPTVListaPorAtender();
    }

    if (estado.equals("ATENDIDO") || estado.equals("CANCELADO")) {
      mostrarMnesajeIPTVListaPorAtender();
      guardarRegistrosDB(turnoEvent);

    }

  }

  private void guardarRegistrosDB(TurnoEvent event) {
    try {
      LOGGER.info("Intentando guardar turno en BD: ");
      turnoService.guardarTurno(event);
      System.out.println("🔄 Turno atendido");
    } catch (Exception e) {
      LOGGER.error("Error al guardar en BD: " + e.getMessage(), e);
    }
  }

  private void mostraMensajeIPTV(TurnoEvent event) {
    if (event.getEstado().name().equals("LLAMADO")) {
      String color = "\u001B[33m";
      String resetColor = "\u001B[0m";
      System.out.println("\n" + "=".repeat(60));
      System.out.println(color + "📢 EVENTO DE TURNO : " + event.getEstado().name() + resetColor);
      System.out.println("=".repeat(60));
      System.out.println("🎫 Código de Turno        : " + event.getCodigoTurno());
      System.out.println("👤 Cliente                : " + event.getNombreCliente());
      System.out.println("\n💬 MENSAJE               : ¡" + event.getNombreCliente() + ", por favor acérquese a la Ventanilla!");
      System.out.println("\n" + "=".repeat(60));
    }
  }

  private void mostrarMnesajeIPTVListaPorAtender() {
    try {
      // Llama al endpoint : http://localhost:8085/api/v1/turnos/en-espera
      List<TurnoEnEsperaDTO> turnosEnEspera = turnoClient.getTurnosEnEspera();
      
      System.out.println("\n" + "=".repeat(60));
      System.out.println("📋 LISTA DE TURNOS EN ESPERA ");
      System.out.println("=".repeat(60));
      
      int count = 0;
      for (TurnoEnEsperaDTO turno : turnosEnEspera) {
        if (count >= 10) break;
        System.out.println(" codigo : " + turno.getCodigoTurno() + " ....... posicion En Cola : "+ turno.getPosicionEnCola());
        count++;
      }
      
      System.out.println("=".repeat(60));
      System.out.println("Total en cola: " + turnosEnEspera.size());
      System.out.println("=".repeat(60));
    } catch (Exception e) {
      LOGGER.error("Error al obtener turnos en espera", e);
      System.out.println("Error al conectar con el servicio de turnos");
    }
  }
}
