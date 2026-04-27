package pe.registros.ms.turnos.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequestDTO {
    public String nombreCliente;
    public long documentoIdentidad;
}
