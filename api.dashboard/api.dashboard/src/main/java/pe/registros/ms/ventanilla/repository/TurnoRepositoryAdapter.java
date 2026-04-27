package pe.registros.ms.ventanilla.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pe.registros.ms.ventanilla.entity.TurnoEntity;

import java.util.List;


@ApplicationScoped
public class TurnoRepositoryAdapter {

    @Inject
    public TurnoRepository turnoRepository;

    @Transactional
    public TurnoEntity save(TurnoEntity turno) {
        TurnoEntity managed = turnoRepository.getEntityManager().merge(turno);
        return managed;
    }

    public List<TurnoEntity> getAllTurnos() {
        return turnoRepository.findAll().stream().toList();
    }

}
