package pe.registros.ms.turnos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoResponseDTO {
    public String codigoTurno;
    public String nombreCliente;
    public Long documentoIdentidad;
    public LocalDateTime fechaHoraCreacion;
    public EstadoTurno estado;
    public Integer posicionEnCola;
}
