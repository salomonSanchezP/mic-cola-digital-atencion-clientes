package pe.registros.ms.turnos.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pe.registros.ms.turnos.dto.EstadoTurno;
import pe.registros.ms.turnos.dto.TurnoRequestDTO;
import pe.registros.ms.turnos.dto.TurnoRequestUpdateDTO;
import pe.registros.ms.turnos.dto.TurnoResponseDTO;
import pe.registros.ms.turnos.entity.TurnoEntity;
import pe.registros.ms.turnos.mappers.GlobalMappers;
import pe.registros.ms.turnos.messaging.TurnoKafkaService;
import pe.registros.ms.turnos.repository.TurnoRepositoryAdapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TurnoServiceImpl implements TurnoService{

    private int contador = 0;

    @Inject
    TurnoRepositoryAdapter turnoRepository;

    @Inject
    GlobalMappers globalMappers;

    @Inject
    TurnoKafkaService turnoKafkaService;

    @Override
    public TurnoResponseDTO guardarTurno(TurnoRequestDTO turnoRequestDTO) {
        validarExistenciaTurno(turnoRequestDTO.getDocumentoIdentidad());
        
        Map<String, String> codigoYCola = generarCodigoDeTurnoYCola();
        String codigo = codigoYCola.get("codigo");
        Integer posicionEnCola = Integer.valueOf(codigoYCola.get("cola"));

        TurnoEntity res = TurnoEntity.builder()
                .nombreCliente(turnoRequestDTO.getNombreCliente())
                .documentoIdentidad(turnoRequestDTO.getDocumentoIdentidad())
                .codigoTurno(codigo)
                .estado(EstadoTurno.EN_ESPERA)
                .fechaHoraCreacion(LocalDateTime.now())
                .posicionEnCola(posicionEnCola)
                .build();
        TurnoEntity saved = turnoRepository.save(res);

        turnoKafkaService.sendEventMessaging(res);

        return globalMappers.mapToTurnoResponseDTO(saved);
    }

    private void validarExistenciaTurno(Long documentoIdentidad) {
        turnoRepository.findByDocumentoIdentidad(documentoIdentidad)
                .ifPresent(turno -> {
                    if (turno.getEstado() == EstadoTurno.EN_ESPERA) {
                        throw new IllegalStateException("Ya existe un turno en espera para este documento de identidad: " + documentoIdentidad);
                    }
                    if (turno.getEstado() == EstadoTurno.ATENDIDO || turno.getEstado() == EstadoTurno.CANCELADO) {
                        turnoRepository.delete(turno.getCodigoTurno());
                    }
                });
    }


    private Map<String, String> generarCodigoDeTurnoYCola() {
        Long totalEnEspera = turnoRepository.totalEnEspera();
        String codigo = generarSiguienteCodigo();
        String cola = String.valueOf(totalEnEspera + 1);
        return Map.of("codigo", codigo, "cola", cola);
    }


    private String generarSiguienteCodigo() {
        contador++;
        if (contador > 100) {
            contador = 1;
        }
        return String.format("T-%03d", contador);
    }

    @Override
    public List<TurnoResponseDTO> listarTurnosEnEspera() {
        return globalMappers.mapToListTurnoResponseDTO(turnoRepository.findAllEnEspera());
    }

    @Override
    public TurnoResponseDTO llamarTurno(TurnoRequestUpdateDTO turnoRequestDTO) {
        TurnoEntity existing = turnoRepository.findById(turnoRequestDTO.getCodigoTurno())
                .orElseThrow(() -> new IllegalArgumentException("Turno no encontrado: " + turnoRequestDTO.getCodigoTurno()));

        if (existing.getEstado() != EstadoTurno.EN_ESPERA) {
            throw new IllegalStateException("No se puede llamar el turno. Su estado actual es: " + existing.getEstado());
        }

        Integer posicionActual = existing.getPosicionEnCola();
        if (posicionActual == null || posicionActual != 1) {
            throw new IllegalStateException("No se puede llamar el turno. Debe llamar primero el turno en posición 1");
        }

        existing.setEstado(EstadoTurno.LLAMADO);
        existing.setFechaHoraLlamado(LocalDateTime.now());

        TurnoEntity saved = turnoRepository.save(existing);
        System.out.println("👤 send data saved DB                : " + saved.toString());
        turnoKafkaService.sendEventMessaging(saved);
        return globalMappers.mapToTurnoResponseDTO(saved);
    }


    @Override
    public TurnoResponseDTO atenderTurno(TurnoRequestUpdateDTO turnoRequestDTO) {
        TurnoEntity existing = turnoRepository.findById(turnoRequestDTO.getCodigoTurno())
                .orElseThrow(() -> new IllegalArgumentException("Turno no encontrado: " + turnoRequestDTO.getCodigoTurno()));

        if (existing.getEstado() != EstadoTurno.LLAMADO) {
            throw new IllegalStateException("No se puede atender el turno. Debe estar en estado LLAMADO. Estado actual: " + existing.getEstado());
        }

        existing.setEstado(turnoRequestDTO.getEstado());

        TurnoEntity saved = turnoRepository.save(existing);

        turnoKafkaService.sendEventMessaging(saved);
        actualizarPosicionesCola();

        return globalMappers.mapToTurnoResponseDTO(saved);
    }

    private void actualizarPosicionesCola() {
        List<TurnoEntity> turnosEnEspera = turnoRepository.findAllEnEspera();
        for (TurnoEntity turno : turnosEnEspera) {
            if (turno.getPosicionEnCola() != null && turno.getPosicionEnCola() > 1) {
                turno.setPosicionEnCola(turno.getPosicionEnCola() - 1);
                turnoRepository.save(turno);
            }
        }
    }
}
