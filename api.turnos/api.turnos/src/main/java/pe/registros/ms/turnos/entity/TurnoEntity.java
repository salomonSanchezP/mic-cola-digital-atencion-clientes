package pe.registros.ms.turnos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.registros.ms.turnos.dto.EstadoTurno;

import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoEntity {

    @Id
    @Column(nullable = false, unique = true)
    public String codigoTurno;

    @Column(nullable = false)
    public String nombreCliente;

    @Column(nullable = false, unique = true)
    public Long documentoIdentidad;

    @Column(nullable = false)
    public LocalDateTime fechaHoraCreacion;

    public LocalDateTime fechaHoraLlamado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public EstadoTurno estado;

    public Integer posicionEnCola;

}

