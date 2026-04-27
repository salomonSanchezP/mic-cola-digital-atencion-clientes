package pe.registros.ms.turnos.service;

import pe.registros.ms.turnos.dto.TurnoRequestDTO;
import pe.registros.ms.turnos.dto.TurnoRequestUpdateDTO;
import pe.registros.ms.turnos.dto.TurnoResponseDTO;

import java.util.List;


public interface TurnoService {

   TurnoResponseDTO guardarTurno(TurnoRequestDTO turnoRequestDTO);
   List<TurnoResponseDTO> listarTurnosEnEspera();
   TurnoResponseDTO llamarTurno(TurnoRequestUpdateDTO turnoRequestDTO);
   TurnoResponseDTO atenderTurno(TurnoRequestUpdateDTO turnoRequestDTO);
}
