package pe.registros.ms.ventanilla.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TurnoEnEsperaDTO {

    @JsonProperty("codigoTurno")
    private String codigoTurno;

    @JsonProperty("nombreCliente")
    private String nombreCliente;

    @JsonProperty("documentoIdentidad")
    private Long documentoIdentidad;

    @JsonProperty("fechaHoraCreacion")
    private String fechaHoraCreacion;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("posicionEnCola")
    private Integer posicionEnCola;

}