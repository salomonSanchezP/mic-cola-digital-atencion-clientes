package pe.registros.ms.ventanilla.entity;

import com.registros.event.EstadoTurno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "turnos-procesados")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    public Long id;

    @Column(nullable = false)
    public String codigoTurno; 

    @Column(nullable = false)
    public String nombreCliente;

    @Column(nullable = false)
    public Long documentoIdentidad;

    @Column(nullable = false)
    public LocalDateTime fechaHoraCreacion;

    public Instant fechaHoraLlamado;

    public LocalDateTime fechaHoraAtendido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public EstadoTurno estado;

}

