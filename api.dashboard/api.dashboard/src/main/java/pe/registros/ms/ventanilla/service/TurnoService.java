package pe.registros.ms.ventanilla.service;


import com.registros.event.TurnoEvent;
import pe.registros.ms.ventanilla.dto.TurnoResponseDTO;

import java.util.List;

public interface TurnoService {

   TurnoResponseDTO guardarTurno(TurnoEvent turnoRequestDTO);
   List<TurnoResponseDTO> getAllTurnos();

}
