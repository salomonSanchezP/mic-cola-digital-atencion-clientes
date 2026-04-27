package pe.registros.ms.ventanilla.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import pe.registros.ms.ventanilla.dto.TurnoResponseDTO;
import pe.registros.ms.ventanilla.entity.TurnoEntity;


import java.util.List;
import java.util.stream.Collectors;

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
                .build();
    }

    public List<TurnoResponseDTO> mapToTurnoResponseDTOList(List<TurnoEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::mapToTurnoResponseDTO)
                .collect(Collectors.toList());
    }

}
