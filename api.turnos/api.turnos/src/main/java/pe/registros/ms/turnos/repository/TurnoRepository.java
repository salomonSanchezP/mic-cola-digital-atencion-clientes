package pe.registros.ms.turnos.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import pe.registros.ms.turnos.entity.TurnoEntity;

import java.util.Optional;

@ApplicationScoped
public class TurnoRepository implements PanacheRepositoryBase<TurnoEntity, String> {

    public Optional<TurnoEntity> findByCodigoTurno(String codigo) {
        return find("codigoTurno", codigo).firstResultOptional();
    }

    public Optional<TurnoEntity> findByDocumentoIdentidad(Long documentoIdentidad) {
        return find("documentoIdentidad", documentoIdentidad).firstResultOptional();
    }
}
