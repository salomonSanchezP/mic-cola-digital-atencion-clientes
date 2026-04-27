package pe.registros.ms.ventanilla.dto;

import com.registros.event.EstadoTurno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
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
    public Instant fechaHoraAtendido;
    public EstadoTurno estado;
}
