package pe.registros.ms.ventanilla.service;

import com.registros.event.TurnoEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pe.registros.ms.ventanilla.dto.TurnoResponseDTO;
import pe.registros.ms.ventanilla.entity.TurnoEntity;
import pe.registros.ms.ventanilla.mappers.GlobalMappers;
import pe.registros.ms.ventanilla.repository.TurnoRepositoryAdapter;


import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class TurnoServiceImpl implements TurnoService{

    @Inject
    GlobalMappers globalMappers;

    @Inject
    TurnoRepositoryAdapter turnoRepository;


    @Override
    public TurnoResponseDTO guardarTurno(TurnoEvent turnoRequestDTO) {
        TurnoEntity res = TurnoEntity.builder()
                .nombreCliente(turnoRequestDTO.getNombreCliente())
                .documentoIdentidad(turnoRequestDTO.getDocumentoIdentidad())
                .codigoTurno(turnoRequestDTO.getCodigoTurno())
                .estado(turnoRequestDTO.getEstado())
                .fechaHoraLlamado(turnoRequestDTO.getFechaHoraLlamado())
                .fechaHoraCreacion(LocalDateTime.now())
                .fechaHoraAtendido(LocalDateTime.now())
                .build();
        
        TurnoEntity saved = turnoRepository.save(res);
        return globalMappers.mapToTurnoResponseDTO(saved);
    }

    @Override
    public List<TurnoResponseDTO> getAllTurnos() {
        List<TurnoEntity> entities = turnoRepository.getAllTurnos();
        return globalMappers.mapToTurnoResponseDTOList(entities);
    }


}
