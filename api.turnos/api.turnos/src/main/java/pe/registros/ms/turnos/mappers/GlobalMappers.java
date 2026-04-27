package pe.registros.ms.turnos.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import pe.registros.ms.turnos.dto.TurnoResponseDTO;
import pe.registros.ms.turnos.entity.TurnoEntity;

import java.util.List;

@ApplicationScoped
public class GlobalMappers {
    public TurnoResponseDTO mapToTurnoResponseDTO(TurnoEntity saved) {
        if (saved == null) {
            return null;
        }
        return TurnoResponseDTO.builder()
                .codigoTurno(saved.getCodigoTurno())
                .nombreCliente(saved.getNombreCliente())
                .documentoIdentidad(saved.getDocumentoIdentidad())
                .fechaHoraCreacion(saved.getFechaHoraCreacion())
                .estado(saved.getEstado())
                .posicionEnCola(saved.getPosicionEnCola())
                .build();
    }

    public List<TurnoResponseDTO> mapToListTurnoResponseDTO(List<TurnoEntity> all) {
        return all.stream()
                .map(this::mapToTurnoResponseDTO)
                .toList();
    }
}
