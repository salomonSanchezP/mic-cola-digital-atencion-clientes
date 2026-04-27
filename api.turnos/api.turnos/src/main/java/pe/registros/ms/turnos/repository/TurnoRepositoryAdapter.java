package pe.registros.ms.turnos.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.registros.ms.turnos.dto.EstadoTurno;
import pe.registros.ms.turnos.entity.TurnoEntity;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TurnoRepositoryAdapter {

    @Inject
    public TurnoRepository turnoRepository;

    @Transactional
    public TurnoEntity save(TurnoEntity turno) {
        TurnoEntity managed = turnoRepository.getEntityManager().merge(turno);
        return managed;
    }

    public List<TurnoEntity> findAllEnEspera() {
        return turnoRepository.list("estado", EstadoTurno.EN_ESPERA);
    }

    public Optional<TurnoEntity> findById(String codigoTurno) {
        return turnoRepository.findByCodigoTurno(codigoTurno);
    }

    public Optional<TurnoEntity> findByDocumentoIdentidad(Long documentoIdentidad) {
        return turnoRepository.findByDocumentoIdentidad(documentoIdentidad);
    }

    public Long totalEnEspera(){
        return turnoRepository.count("estado", EstadoTurno.EN_ESPERA);
    }

    public void delete(String codigoTurno) {
        turnoRepository.delete("codigoTurno", codigoTurno);
    }
}
